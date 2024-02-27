package edu.java.bot.api.rest;

import edu.java.bot.api.model.ApiErrorResponse;
import edu.java.bot.api.model.LinkUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
public interface LinkUpdatesApi {
    @Operation(summary = "Send the update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Update is proceeded"),

        @ApiResponse(responseCode = "400",
                     description = "Parameters are incorrect",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(
                         implementation = ApiErrorResponse.class))),

        @ApiResponse(responseCode = "404",
                     description = "There is no TG IDs registered",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(
                         implementation = ApiErrorResponse.class
                     )))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> updatesPost(@RequestBody LinkUpdateRequest request);
}
