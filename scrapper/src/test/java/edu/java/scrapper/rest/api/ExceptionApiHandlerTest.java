package edu.java.scrapper.rest.api;

import edu.java.scrapper.rest.model.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ExceptionApiHandler.class)
public class ExceptionApiHandlerTest {
    @Autowired
    private ExceptionApiHandler handler;

    @Test
    void givenRequest_whenIncorrectParameterType_then400() {
        Exception exception = new RuntimeException("incorrect parameters");

        var expectedResponse =
            ResponseEntity.badRequest().body(new ApiErrorResponse(HttpStatus.BAD_REQUEST, exception));
        var actualResponse = handler.incorrectParameterException(exception);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void givenRequest_whenEntryDoesntExist_then404() {
        HttpClientErrorException exception =
            HttpClientErrorException.create(HttpStatus.NOT_FOUND, "entry already exist", null, null, null);

        var expectedResponse =
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse(HttpStatus.NOT_FOUND, exception));
        var actualResponse = handler.entryDoesntExist(exception);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void givenRequest_whenEntryAlreadyExists_then409() {
        HttpClientErrorException exception =
            HttpClientErrorException.Conflict.create(
                "entry already exists",
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.getReasonPhrase(),
                null,
                null,
                null
            );

        var expectedResponse =
            ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorResponse(HttpStatus.CONFLICT, exception));
        var actualResponse = handler.entryAlreadyExist(exception);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
