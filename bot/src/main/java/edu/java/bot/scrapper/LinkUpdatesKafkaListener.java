package edu.java.bot.scrapper;

import edu.java.bot.dialog.service.LinkUpdateNotifier;
import edu.java.bot.rest.model.LinkUpdateRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkUpdatesKafkaListener {
    private final LinkUpdateNotifier notifier;

    @KafkaListener(topics = "${app.kafka.topic-name}",
                   groupId = "${app.kafka.group-id}",
                   containerFactory = "concurrentKafkaListenerContainerFactory")
    public void listen(@NotNull LinkUpdateRequest request) {
        notifier.notifyAll(request);
    }
}
