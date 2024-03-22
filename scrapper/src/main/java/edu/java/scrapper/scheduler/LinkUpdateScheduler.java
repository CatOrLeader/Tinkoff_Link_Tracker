package edu.java.scrapper.scheduler;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.domain.service.TgChatService;
import edu.java.scrapper.github.service.EventService;
import edu.java.scrapper.rest.model.LinkUpdateRequest;
import edu.java.scrapper.rest.service.UpdatesService;
import edu.java.scrapper.stackoverflow.service.QuestionService;
import edu.java.scrapper.utils.LinkUtils;
import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true")
@Service
@Log4j2
@RequiredArgsConstructor
public final class LinkUpdateScheduler implements UpdateScheduler {
    private static final int OFFSET = 3;

    private final ApplicationConfig.Scheduler scheduler;
    private final LinkService linkService;
    private final TgChatService tgChatService;
    private final EventService eventService;
    private final QuestionService questionService;
    private final UpdatesService updatesService;

    @Override
    @Scheduled(
        fixedRateString = "#{@scheduler.forceCheckDelay().toMillis()}"
    )
    public void update() {
        OffsetDateTime toCheck = Instant.now().atOffset(ZoneOffset.ofHours(OFFSET)).minus(scheduler.interval());
        var linksToUpdate = linkService.findAllBefore(toCheck);
        for (Link link : linksToUpdate) {
            switch (link.getType()) {
                case GITHUB_ISSUE -> fetchGithubIssue(link.getUri()).ifPresent(this::postLinkUpdate);
                case GITHUB_PULL -> fetchGithubPull(link.getUri()).ifPresent(this::postLinkUpdate);
                case SFO_QUESTION -> fetchSFOQuestion(link.getUri()).ifPresent(this::postLinkUpdate);
                case null, default -> log.error("Error when fetching the link: " + link);
            }
        }
    }

    private Optional<Link> fetchGithubIssue(URI link) {
        List<String> params = LinkUtils.extractOwnerNameNumber(link.toString());

        return eventService.getIssueByOwnerNameNumber(
                link,
                params.getFirst(),
                params.get(1),
                Integer.parseInt(params.getLast())
            )
            .map(Link::new);
    }

    private Optional<Link> fetchGithubPull(URI link) {
        List<String> params = LinkUtils.extractOwnerNameNumber(link.toString());

        return eventService.getPullByOwnerNameNumber(
                link,
                params.getFirst(),
                params.get(1),
                Integer.parseInt(params.getLast())
            )
            .map(Link::new);
    }

    private Optional<Link> fetchSFOQuestion(URI link) {
        return
            Optional.of(new Link(
                questionService.getQuestionById(LinkUtils.extractQuestionId(link.toString()))
            ));
    }

    private void postLinkUpdate(Link link) {
        linkService.update(link);
        updatesService.postLinkUpdate(
            new LinkUpdateRequest(link, tgChatService.findAllByLinkUrl(link.getUri()))
        );
    }
}

