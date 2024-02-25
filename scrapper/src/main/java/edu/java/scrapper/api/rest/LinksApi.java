package edu.java.scrapper.api.rest;

import edu.java.scrapper.api.model.AddLinkRequest;
import edu.java.scrapper.api.model.ApiErrorResponse;
import edu.java.scrapper.api.model.LinkResponse;
import edu.java.scrapper.api.model.ListLinksResponse;
import edu.java.scrapper.api.model.RemoveLinkRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface LinksApi {
    @Operation(summary = "Get user links by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Link successfully retrieved",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ListLinksResponse.class))),

        @ApiResponse(responseCode = "400",
                     description = "Incorrect request parameters",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ApiErrorResponse.class)))})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") int tgChatId);

    @Operation(summary = "Add link to track")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Link successfully added",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = LinkResponse.class))),

        @ApiResponse(responseCode = "400",
                     description = "Incorrect request parameters",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ApiErrorResponse.class)))})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
                 consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") int tgChatId,
        @RequestBody AddLinkRequest request
    );

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Link successfully removed",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = LinkResponse.class))),

        @ApiResponse(responseCode = "400",
                     description = "Incorrect request parameters",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ApiErrorResponse.class))),

        @ApiResponse(responseCode = "404",
                     description = "Link not found",
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE,
                   consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader("Tg-Chat-Id") int tgChatId,
        @RequestBody RemoveLinkRequest request
    );
}
