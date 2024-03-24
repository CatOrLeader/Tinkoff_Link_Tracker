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
import java.time.Instant;
import java.time.LocalDateTime;
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
import static edu.java.scrapper.utils.DateTimeUtils.OFFSET_HOURS;

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
        linkService.findAllBefore(toCheck).forEach(
            link -> fetchLinkFromExternalSource(link).filter(this::updateLinkAndLastCheckedTime).ifPresentOrElse(
                this::postToUsers,
                () -> updateLinkAndLastCheckedTime(link)
            )
        );
    }

    private Optional<Link> fetchLinkFromExternalSource(Link link) {
        switch (link.getType()) {
            case GITHUB_ISSUE -> {
                return fetchGithubIssue(link);
            }
            case GITHUB_PULL -> {
                return fetchGithubPull(link);
            }
            case SFO_QUESTION -> {
                return fetchSFOQuestion(link);
            }
            case null, default -> {
                log.error("Error when fetching the link: " + link);
                return Optional.empty();
            }
        }
    }

    private Optional<Link> fetchGithubIssue(Link link) {
        String url = link.getUri().toString();
        List<String> params = LinkUtils.extractOwnerNameNumber(url);

        return eventService.getIssueByOwnerNameNumber(
                link.getUri(),
                params.getFirst(),
                params.get(1),
                Integer.parseInt(params.getLast())
            )
            .map(Link::new);
    }

    private Optional<Link> fetchGithubPull(Link link) {
        String url = link.getUri().toString();
        List<String> params = LinkUtils.extractOwnerNameNumber(url);

        return eventService.getPullByOwnerNameNumber(
                link.getUri(),
                params.getFirst(),
                params.get(1),
                Integer.parseInt(params.getLast())
            )
            .map(Link::new);
    }

    private Optional<Link> fetchSFOQuestion(Link link) {
        return
            Optional.of(new Link(
                questionService.getQuestionById(LinkUtils.extractQuestionId(link.getUri().toString()))
            ));
    }

    private boolean updateLinkAndLastCheckedTime(Link link) {
        link.setLastCheckedAt(LocalDateTime.now().atOffset(ZoneOffset.ofHours(OFFSET_HOURS)));
        return linkService.update(link);
    }

    private void postToUsers(Link link) {
        updatesService.postLinkUpdate(
            new LinkUpdateRequest(link, tgChatService.findAllByLinkUrl(link.getUri()))
        );
    }
}

