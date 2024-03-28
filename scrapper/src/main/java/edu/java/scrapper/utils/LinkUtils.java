package edu.java.scrapper.utils;

import edu.java.scrapper.domain.dto.ResponseType;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public final class LinkUtils {
    private LinkUtils() {
    }

    public static @NotNull Optional<ResponseType> getResponseType(@NotNull URI link) {
        for (var type : ResponseType.values()) {
            if (type.pattern.matcher(link.toString()).matches()) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    public static @NotNull List<String> extractOwnerNameNumber(@NotNull String link) {
        var arr = List.of(link.split("/"));
        final int posFromEnd = 4;
        return arr.subList(arr.size() - posFromEnd, arr.size());
    }

    public static @NotNull String extractQuestionId(@NotNull String link) {
        List<String> arr = List.of(link.split("/"));
        var indexOfQuestion = arr.indexOf("questions");
        var questionId = arr.get(indexOfQuestion + 1);
        return questionId.substring(0, Math.max(arr.getLast().indexOf("?"), questionId.length()));
    }
}
