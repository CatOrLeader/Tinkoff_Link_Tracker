package edu.java.scrapper.scheduler;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.ResponseType;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.github.model.IssueResponse;
import edu.java.scrapper.github.service.EventService;
import edu.java.scrapper.stackoverflow.service.QuestionService;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true")
@Service
@RequiredArgsConstructor
public final class LinkUpdateScheduler implements UpdateScheduler {
    private final EventService githubEventService;
    private final QuestionService sfoQuestionService;
    private final LinkService linkService;

    @Override
    @Scheduled(
        fixedRateString = "#{@scheduler.forceCheckDelay().toMillis()}"
    )
    public void update() {
        final long offset = 5L;
        List<Link> links = new ArrayList<>(linkService.findAll());
        OffsetDateTime now = OffsetDateTime.now().minusMinutes(offset);
        for (var link : links) {
            if (link.getLastCheckedAt().isBefore(now)) {
                IssueResponse response = githubEventService.getIssueByOwnerNameNumber(
                    link.getCreatedBy(),
                    link.getUpdatedBy(),
                    Integer.parseInt(link.getUri().toString().split("/")[(int) offset])
                );

                linkService.update(
                    new Link(
                        link.getId(),
                        response.url(), "Issue updated", null, response.updatedAt(),
                        null, null, response.title(), link.getEtag(), now, ResponseType.GITHUB_ISSUE
                    )
                );
            }
        }
    }
}

