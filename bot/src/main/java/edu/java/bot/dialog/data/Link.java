package edu.java.bot.dialog.data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record Link(
    @NotEmpty String url,
    @NotNull Instant createdAt
) {
    public static @NotNull Link constructLink(@NotEmpty String url) {
        return new Link(url, Instant.now());
    }

    @Override public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Link link = (Link) object;

        return url.equals(link.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
