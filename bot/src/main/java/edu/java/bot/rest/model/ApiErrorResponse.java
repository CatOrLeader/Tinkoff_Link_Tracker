package edu.java.bot.rest.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public final class ApiErrorResponse {
    private String description;
    private String code;
    private String exceptionName;
    private String exceptionMessage;
    private List<String> stacktrace;

    public ApiErrorResponse(@NotNull HttpStatus status, @NotNull Exception exception) {
        this.description = status.getReasonPhrase();
        this.code = String.valueOf(status.value());
        this.exceptionName = exception.getClass().getName();
        this.exceptionMessage = exception.getMessage();
        this.stacktrace =
            Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList());
    }
}
