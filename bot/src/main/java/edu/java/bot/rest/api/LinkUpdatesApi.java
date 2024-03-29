package edu.java.bot.rest.api;

import edu.java.bot.rest.model.ApiErrorResponse;
import edu.java.bot.rest.model.LinkUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@RequestMapping("/updates")
public interface LinkUpdatesApi {
    @Operation(summary = "Send the update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Update is proceeded"),

        @ApiResponse(responseCode = "400",
                     description = "Parameters are incorrect",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(
                         implementation = ApiErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> updatesPost(@RequestBody @Valid LinkUpdateRequest request);
}
