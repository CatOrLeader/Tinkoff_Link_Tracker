package edu.java.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
public class ClientConfiguration {
    @Bean
    public WebClient githubWebClient(
        @Value("${app.clients.github-url}") String baseUrl
    ) {
        return WebClient.builder()
            .baseUrl(baseUrl)
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
}
