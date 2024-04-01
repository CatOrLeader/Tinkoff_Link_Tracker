package edu.java.scrapper.kafka;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.rest.model.LinkUpdateRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapperQueueProducer {
    private final ApplicationConfig config;
    private final KafkaTemplate<String, LinkUpdateRequest> template;

    public void send(@NotNull LinkUpdateRequest request) {
        template.send(config.kafka().topicName(), request);
        log.debug("The request is successfully queued in the kafka: " + request);
    }
}
