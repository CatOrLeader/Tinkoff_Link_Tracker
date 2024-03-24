package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    @Bean
    Scheduler scheduler,
    @NotNull
    @Bean
    Clients clients,
    @Name("database-access-type")
    DatabaseAccessType databaseAccessType
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
}
