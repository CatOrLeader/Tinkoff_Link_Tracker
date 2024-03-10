package edu.java.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class ClientConfiguration {
    private final ApplicationConfig config;

    @Bean
    public WebClient githubWebClient(
        @Value("${app.clients.github-url}") String baseUrl
    ) {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
            .defaultHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", config.clients().githubApiToken()))
            .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
            .build();
    }

    @Bean
    public WebClient stackOverflowWebClient(
        @Value("${app.clients.stackoverflow-url}") String baseUrl
    ) {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    @Bean
    public WebClient botWebClient(
        @Value("${app.clients.bot-url}") String baseUrl
    ) {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }
}
