package edu.java.scrapper.api.model;

import jakarta.validation.constraints.NotEmpty;

public record AddLinkRequest(@NotEmpty String link) {
}
