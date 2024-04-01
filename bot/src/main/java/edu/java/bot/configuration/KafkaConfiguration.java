package edu.java.bot.configuration;

import edu.java.bot.rest.model.LinkUpdateRequest;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@ConditionalOnProperty(prefix = "app", name = "kafka.enabled", havingValue = "true")
@Configuration(proxyBeanMethods = false)
@EnableKafka
public class KafkaConfiguration {
    private static final int POLL_TIMEOUT = 3000;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic dlq(ApplicationConfig config) {
        return TopicBuilder
            .name(config.kafka().dlqName())
            .partitions(1)
            .build();
    }

    @Bean
    public DefaultErrorHandler defaultErrorHandler(
        @NotNull KafkaOperations<Object, Object> operations,
        @NotNull ApplicationConfig config
    ) {
        var recover = new DeadLetterPublishingRecoverer(
            operations,
            (consumerRecord, exception) -> new TopicPartition(config.kafka().dlqName(), 0)
        );

        var errorHandler = new DefaultErrorHandler(recover);
        errorHandler.addNotRetryableExceptions(ValidationException.class);
        return errorHandler;
    }

    @Bean
    public ConsumerFactory<String, LinkUpdateRequest> consumerFactory(@NotNull ApplicationConfig applicationConfig) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, applicationConfig.kafka().groupId());

        var deserializer = new JsonDeserializer<>(LinkUpdateRequest.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.trustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        return new DefaultKafkaConsumerFactory<>(
            properties,
            new StringDeserializer(),
            deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest>
    concurrentKafkaListenerContainerFactory(@NotNull ConsumerFactory<String, LinkUpdateRequest> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setPollTimeout(POLL_TIMEOUT);
        return factory;
    }
}
