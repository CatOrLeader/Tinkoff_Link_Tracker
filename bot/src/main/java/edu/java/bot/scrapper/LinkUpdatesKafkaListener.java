package edu.java.bot.scrapper;

import edu.java.bot.dialog.service.LinkUpdateNotifier;
import edu.java.bot.rest.model.LinkUpdateRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkUpdatesKafkaListener {
    private final LinkUpdateNotifier notifier;

    @KafkaListener(topics = "${app.kafka.topic-name}",
                   groupId = "${app.kafka.group-id}",
                   containerFactory = "concurrentKafkaListenerContainerFactory")
    public void listen(@NotNull LinkUpdateRequest request, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        notifier.notifyAll(request);
        log.debug("The request from topic '" + topic + "' is received and successfully sent to the users");
    }
}
