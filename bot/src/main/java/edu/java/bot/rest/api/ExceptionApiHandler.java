package edu.java.bot.rest.api;

import edu.java.bot.rest.model.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
        MethodArgumentConversionNotSupportedException.class})
    public ResponseEntity<ApiErrorResponse> incorrectMethodArgument(Exception exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
            .status(status)
            .body(new ApiErrorResponse(status, exception));
    }
}
