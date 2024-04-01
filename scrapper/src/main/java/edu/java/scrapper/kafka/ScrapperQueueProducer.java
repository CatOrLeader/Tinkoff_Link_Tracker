package edu.java.scrapper.kafka;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.rest.model.LinkUpdateRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {
    private final ApplicationConfig config;
    private final KafkaTemplate<String, LinkUpdateRequest> template;

    public void send(@NotNull LinkUpdateRequest request) {
        template.send(config.kafka().topicName(), request);
    }
}
