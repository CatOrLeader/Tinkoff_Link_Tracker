package edu.java.bot.dialog.data;

import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public final class UserLinksTracker {
    private final Map<Long, Set<Link>> linkLib;

    public UserLinksTracker() {
        linkLib = new HashMap<>();
    }

    public void addUserLink(long userId, @NotNull Link link) {
        if (!linkLib.containsKey(userId)) {
            linkLib.put(userId, new HashSet<>());
        }

        linkLib.get(userId).add(link);
    }

    public void removeUserLinkByUrl(long userId, @NotNull String url) {
        linkLib.get(userId).removeIf(link -> link.url().equals(url));
    }

    public @NotNull Set<Link> getUserLinks(long userId) {
        linkLib.computeIfAbsent(userId, k -> new HashSet<>());
        return linkLib.get(userId);
    }
}
