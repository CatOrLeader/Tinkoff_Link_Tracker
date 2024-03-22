package edu.java.scrapper.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.util.regex.Pattern;

public enum ResponseType {
    GITHUB_ISSUE(Pattern.compile("https://github.com/.+/.+/issues/[0-9]+")),
    GITHUB_PULL(Pattern.compile("https://github.com/.+/.+/pull/[0-9]+")),
    SFO_QUESTION(Pattern.compile("https://stackoverflow.com/questions/[0-9]+.*"));

    public final Pattern pattern;

    ResponseType(@NotNull Pattern pattern) {
        this.pattern = pattern;
    }
}
