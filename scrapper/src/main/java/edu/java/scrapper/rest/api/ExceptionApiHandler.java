package edu.java.scrapper.rest.api;

import edu.java.scrapper.rest.api.exceptions.EntryAlreadyExistException;
import edu.java.scrapper.rest.model.ApiErrorResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import static org.springframework.web.client.HttpClientErrorException.NotFound;

@RestControllerAdvice(basePackages = "edu/java/scrapper/rest")
public class ExceptionApiHandler {
    @ExceptionHandler(value = {
        MethodArgumentNotValidException.class, TypeMismatchException.class, BindException.class
    })
    public ResponseEntity<ApiErrorResponse> incorrectParameterException(Exception exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
            .status(status)
            .body(new ApiErrorResponse(exception, status));
    }

    @ExceptionHandler(value = NotFound.class)
    public ResponseEntity<ApiErrorResponse> entryDoesntExist(HttpClientErrorException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity
            .status(status)
            .body(new ApiErrorResponse(exception, status));
    }

    @ExceptionHandler(value = EntryAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> entryAlreadyExist(EntryAlreadyExistException exception) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity
            .status(status)
            .body(new ApiErrorResponse(exception, status));
    }
}
