package edu.java.bot.api.rest;

import edu.java.bot.api.model.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    ResponseEntity<ApiErrorResponse> notFoundTgIds(HttpClientErrorException.NotFound exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity
            .status(status)
            .body(new ApiErrorResponse(status, exception));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
        MethodArgumentConversionNotSupportedException.class})
    ResponseEntity<ApiErrorResponse> incorrectMethodArgument(Exception exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
            .status(status)
            .body(new ApiErrorResponse(status, exception));
    }
}
