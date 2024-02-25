package edu.java.scrapper.api.rest;

import edu.java.scrapper.api.model.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public interface TgChatApi {
    @Operation(summary = "Register chat")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chat registered successfully"),

        @ApiResponse(responseCode = "400",
                     description = "Incorrect request parameters",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ApiErrorResponse.class)))})
    @PostMapping(value = "/{id}",
                 produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> registerChat(@PathVariable int id);

    @Operation(summary = "Remove chat")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chat deleted successfully"),

        @ApiResponse(responseCode = "400",
                     description = "Incorrect request parameters",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ApiErrorResponse.class))),

        @ApiResponse(responseCode = "404",
                     description = "Chat doesn't exists",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ApiErrorResponse.class)))})
    @DeleteMapping(value = "/{id}",
                   produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteChat(@PathVariable int id);
}
