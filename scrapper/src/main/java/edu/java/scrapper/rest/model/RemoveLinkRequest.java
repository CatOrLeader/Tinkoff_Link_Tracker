package edu.java.scrapper.rest.model;

import jakarta.validation.constraints.NotEmpty;
import java.net.URI;

public record RemoveLinkRequest(@NotEmpty URI link) {
}
