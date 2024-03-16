package edu.java.bot.configuration;

import edu.java.bot.rest.model.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@RequiredArgsConstructor
public class ClientConfiguration {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ExchangeFilterFunction ERROR_RESPONSE_FILTER =
        ExchangeFilterFunction.ofResponseProcessor(ClientConfiguration::exchangeFilterResponseProcessor);

    private static Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
        var statusCode = response.statusCode();
        if (statusCode.is4xxClientError()) {
            return response.bodyToMono(ApiErrorResponse.class)
                .flatMap(body -> {
                    LOGGER.error(body);
                    return Mono.error(
                        HttpClientErrorException.create(
                            statusCode,
                            body.getDescription(),
                            response.headers().asHttpHeaders(),
                            null, null
                        )
                    );
                });
        }
        return Mono.just(response);
    }

    @Bean
    public WebClient scrapperWebClient(
        @Value("${app.clients.scrapper-url}") String baseUrl
    ) {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .filter(ERROR_RESPONSE_FILTER)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}
