package edu.java.scrapper.api.model;

import jakarta.validation.constraints.NotEmpty;

public record RemoveLinkRequest(@NotEmpty String link) {
}
