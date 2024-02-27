package edu.java.scrapper.rest.model;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiErrorResponseTest {
    @Test
    void givenExceptionAndStatus_whenObjectConstructed_thenAllFieldsCorrect() {
        final String message = "Not found exception";
        Exception exception = new Exception(message);
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorResponse expectedValue = new ApiErrorResponse(
            "Not Found",
            "404",
            "java.lang.Exception",
            message,
            Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).collect(
                Collectors.toList())
        );
        ApiErrorResponse actualValue = new ApiErrorResponse(exception, status);

        assertThat(actualValue).isEqualTo(expectedValue);
    }
}
