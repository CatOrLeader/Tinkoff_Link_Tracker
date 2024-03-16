package edu.java.bot.rest.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record ApiErrorResponse(String description,
                               String code,
                               String exceptionName,
                               String exceptionMessage,
                               List<String> stacktrace) {

    public ApiErrorResponse(@NotNull HttpStatus status, @NotNull Exception exception) {
        this(
            status.getReasonPhrase(),
            String.valueOf(status.value()),
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList())
        );
    }
}
