package edu.java.scrapper.configuration;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    boolean useQueue,
    @NotNull
    @Bean
    Scheduler scheduler,
    @NotNull
    @Bean
    Clients clients,
    @Name("database-access-type")
    DatabaseAccessType databaseAccessType,
    @NotNull
    Retry retryPolitics,
    @NotNull
    Kafka kafka
) {
    public enum DatabaseAccessType {
        JDBC, JPA, JOOQ
    }

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record Clients(
        @NotBlank String githubUrl,
        @NotBlank String githubApiToken,
        @NotBlank String stackOverflowUrl,
        @NotBlank String botUrl
    ) {
    }

    @Validated
    public record Retry(@Min(1) int maxAttempts, @NotEmpty Set<Integer> statusCodes, @NotNull Type type,
                        @NotNull Config config) {
        public enum Type {
            CONSTANT, LINEAR, EXPONENTIAL
        }

        public record Config(@NotNull Constant constant, @NotNull Linear linear, @NotNull Exponential exponential) {
            public record Constant(@Min(0) long backOffPeriod) {
            }

            public record Linear(@Min(0) long initialInterval, @Min(0) long maxInterval) {
            }

            public record Exponential(@Min(0) long initialInterval, @Min(0) long mult, @Min(0) long maxInterval) {
            }
        }
    }

    @Validated
    public record Kafka(boolean enabled, @NotBlank String topicName) {
    }
}
