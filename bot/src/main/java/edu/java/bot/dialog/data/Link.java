package edu.java.bot.dialog.data;

import edu.java.bot.rest.model.LinkResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public final class Link {
    private final @NotEmpty URI url;
    private long id;

    public Link(@NotNull LinkResponse response) {
        this(response.url(), response.id());
    }
}
