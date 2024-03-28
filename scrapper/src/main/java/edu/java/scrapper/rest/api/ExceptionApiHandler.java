package edu.java.scrapper.rest.api;

import edu.java.scrapper.rest.api.exception.LinkUnsupportedException;
import edu.java.scrapper.rest.model.ApiErrorResponse;
import jakarta.validation.ValidationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(value = {
        MethodArgumentNotValidException.class, TypeMismatchException.class, BindException.class,
        ValidationException.class, LinkUnsupportedException.class, InternalError.class
    })
    public ResponseEntity<ApiErrorResponse> incorrectParameterException(Exception exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
            .status(status)
            .body(new ApiErrorResponse(status, exception));
    }

    @ExceptionHandler(WebClientResponseException.NotFound.class)
    public ResponseEntity<ApiErrorResponse> entryDoesntExist(WebClientResponseException exception) {
        return ResponseEntity
            .status(exception.getStatusCode())
            .body(new ApiErrorResponse(HttpStatus.valueOf(exception.getStatusCode().value()), exception));
    }

    @ExceptionHandler(value = WebClientResponseException.Conflict.class)
    public ResponseEntity<ApiErrorResponse> entryAlreadyExist(WebClientResponseException exception) {
        return ResponseEntity
            .status(exception.getStatusCode())
            .body(new ApiErrorResponse(HttpStatus.valueOf(exception.getStatusCode().value()), exception));
    }
}
