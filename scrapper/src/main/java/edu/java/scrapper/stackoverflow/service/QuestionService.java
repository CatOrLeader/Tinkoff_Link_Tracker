package edu.java.scrapper.stackoverflow.service;

import edu.java.scrapper.stackoverflow.model.QuestionResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {
    private final WebClient stackOverflowWebClient;

    public QuestionResponse getQuestionById(@NotEmpty String id) {
        return stackOverflowWebClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/2.3/questions/" + id)
                .queryParam("site", "stackoverflow")
                .queryParam("filter", "!6VClQr9fY7evMd5wsutFmZL5T")
                .build())
            .retrieve().bodyToMono(QuestionResponse.class)
            .doOnError(throwable -> log.error("Something went wrong durin parsing from SOF", throwable))
            .block();
    }
}
