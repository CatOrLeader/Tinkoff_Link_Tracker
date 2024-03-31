package edu.java.bot.configuration;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotBlank
    String telegramToken,
    @NotNull
    OnStartup onStartup,
    @NotNull
    Clients clients,
    @NotNull
    Retry retryPolitics
) {
    @Validated
    public record OnStartup(
        @DefaultValue("true")
        boolean skipUpdates
    ) {
    }

    @Validated
    public record Clients(
        @NotBlank String scrapperUrl
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
}
