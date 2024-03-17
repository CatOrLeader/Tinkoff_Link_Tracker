//package edu.java.scrapper.rest.api;
//
//import edu.java.scrapper.rest.model.AddLinkRequest;
//import edu.java.scrapper.rest.model.LinkResponse;
//import edu.java.scrapper.rest.model.RemoveLinkRequest;
//import java.net.URI;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
//import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import org.springframework.web.reactive.function.BodyInserters;
//import static org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
//
//@WebFluxTest(controllers = LinksController.class)
//@AutoConfigureWebTestClient
//public class LinksControllerTest {
//    @Autowired
//    private WebTestClient client;
//
//    @Test
//    void givenCorrectGetRequest_whenTgChatIdHeaderIsPresented_thenResponseStatusIsOk() {
//        client.get()
//            .uri("/links")
//            .header("Tg-Chat-Id", "123")
//            .exchange().expectStatus().isOk();
//    }
//
//    @Test
//    void givenCorrectGetRequest_whenTgChatIdHeaderIsNotPresented_thenResponseStatusIs400() {
//        client.get()
//            .uri("/links")
//            .exchange().expectStatus().isBadRequest();
//    }
//
//    @Test
//    void givenCorrectPostRequest_whenTgChatIdHeaderIsPresented_thenResponseStatusIsOk() {
//        URI url = URI.create("https://github.com");
//
//        ResponseSpec spec = client.post()
//            .uri("/links")
//            .header("Tg-Chat-Id", "123")
//            .contentType(MediaType.APPLICATION_JSON)
//            .body(BodyInserters.fromValue(new AddLinkRequest(url)))
//            .exchange();
//
//        spec.expectStatus().isOk();
//    }
//
//    @Test
//    void givenCorrectPostRequest_whenTgChatIdHeaderIsPresented_thenResponseBodyIsCorrect() {
//        URI url = URI.create("https://github.com");
//        LinkResponse response = new LinkResponse(0, url);
//
//        ResponseSpec spec = client.post()
//            .uri("/links")
//            .header("Tg-Chat-Id", "123")
//            .contentType(MediaType.APPLICATION_JSON)
//            .body(BodyInserters.fromValue(new AddLinkRequest(url)))
//            .exchange();
//
//        spec.expectBody(LinkResponse.class).isEqualTo(response);
//    }
//
//    @Test
//    void givenCorrectPostRequest_whenTgChatIdHeaderIsNotPresented_thenResponseStatusIs400() {
//        URI url = URI.create("https://github.com");
//
//        ResponseSpec spec = client.post()
//            .uri("/links")
//            .contentType(MediaType.APPLICATION_JSON)
//            .body(BodyInserters.fromValue(new AddLinkRequest(url)))
//            .exchange();
//
//        spec.expectStatus().isBadRequest();
//    }
//
//    @Test
//    void givenCorrectDelRequest_whenTgChatIdHeaderIsPresented_thenResponseStatusIsOk() {
//        URI url = URI.create("https://github.com");
//
//        ResponseSpec spec = client.method(HttpMethod.DELETE)
//            .uri("/links")
//            .header("Tg-Chat-Id", "123")
//            .contentType(MediaType.APPLICATION_JSON)
//            .body(BodyInserters.fromValue(new RemoveLinkRequest(url)))
//            .exchange();
//
//        spec.expectStatus().isOk();
//    }
//
//    @Test
//    void givenCorrectDelRequest_whenTgChatIdHeaderIsPresented_thenResponseBodyIsCorrect() {
//        URI url = URI.create("https://github.com");
//        LinkResponse response = new LinkResponse(0, url);
//
//        ResponseSpec spec = client.method(HttpMethod.DELETE)
//            .uri("/links")
//            .header("Tg-Chat-Id", "123")
//            .contentType(MediaType.APPLICATION_JSON)
//            .body(BodyInserters.fromValue(new RemoveLinkRequest(url)))
//            .exchange();
//
//        spec.expectBody(LinkResponse.class).isEqualTo(response);
//    }
//
//    @Test
//    void givenCorrectDelRequest_whenTgChatIdHeaderIsPresented_thenResponseStatusIs400() {
//        URI url = URI.create("https://github.com");
//
//        ResponseSpec spec = client.method(HttpMethod.DELETE)
//            .uri("/links")
//            .contentType(MediaType.APPLICATION_JSON)
//            .body(BodyInserters.fromValue(new RemoveLinkRequest(url)))
//            .exchange();
//
//        spec.expectStatus().isBadRequest();
//    }
//}
