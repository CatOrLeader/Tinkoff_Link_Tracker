package edu.java.scrapper.configuration;

import edu.java.scrapper.rest.model.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration(proxyBeanMethods = false)
@Log4j2
@RequiredArgsConstructor
public class ClientConfiguration {
    private final ApplicationConfig config;

    private static final ExchangeFilterFunction ERROR_RESPONSE_FILTER =
        ExchangeFilterFunction.ofResponseProcessor(ClientConfiguration::exchangeFilterResponseProcessor);

    private static Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
        var statusCode = response.statusCode();
        if (statusCode.is4xxClientError()) {
            return response.bodyToMono(ApiErrorResponse.class)
                .flatMap(body -> {
                    log.error(body.toString());
                    return Mono.error(
                        HttpClientErrorException.create(
                            statusCode,
                            body.description(),
                            response.headers().asHttpHeaders(),
                            null, null
                        )
                    );
                });
        }
        return Mono.just(response);
    }

    @Bean
    public WebClient githubWebClient(
        @Value("${app.clients.github-url}") String baseUrl
    ) {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
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
            .filter(ERROR_RESPONSE_FILTER)
            .build();
    }
}
