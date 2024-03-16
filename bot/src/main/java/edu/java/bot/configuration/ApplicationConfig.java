package edu.java.bot.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    Clients clients
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
}
