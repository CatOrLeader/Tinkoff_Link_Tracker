package edu.java.scrapper.bot;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.kafka.ScrapperQueueProducer;
import edu.java.scrapper.rest.model.LinkUpdateRequest;
import edu.java.scrapper.rest.service.UpdatesService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateNotifier {
    private final ApplicationConfig config;
    private final UpdatesService httpService;
    private final ScrapperQueueProducer kafkaService;

    public void send(@NotNull LinkUpdateRequest request) {
        if (config.useQueue()) {
            kafkaService.send(request);
        } else {
            httpService.send(request);
        }
    }
}
