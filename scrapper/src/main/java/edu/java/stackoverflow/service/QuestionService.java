package edu.java.stackoverflow.service;

import edu.java.stackoverflow.dto.QuestionDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class QuestionService {
    private final WebClient stackOverflowWebClient;

    public QuestionDTO getQuestionById(@NotEmpty String id) {
        return stackOverflowWebClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/2.3/questions/" + id)
                .queryParam("site", "stackoverflow")
                .queryParam("filter", "!6VClQr9fY7evMd5wsutFmZL5T")
                .build())
            .retrieve().bodyToMono(QuestionDTO.class)
            .doOnError(throwable -> LogManager.getLogger().error(throwable))
            .block();
    }
}
