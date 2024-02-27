package edu.java.scrapper.rest.api;

import edu.java.scrapper.rest.model.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@RequestMapping("/tg-chat")
public interface TgChatApi {
    @Operation(summary = "Register chat")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chat registered successfully"),

        @ApiResponse(responseCode = "400",
                     description = "Incorrect request parameters",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ApiErrorResponse.class))),

        @ApiResponse(responseCode = "409",
                     description = "The entry already exists",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(value = "/{id}",
                 produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> registerChat(@PathVariable long id);

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
    ResponseEntity<Void> deleteChat(@PathVariable long id);
}
