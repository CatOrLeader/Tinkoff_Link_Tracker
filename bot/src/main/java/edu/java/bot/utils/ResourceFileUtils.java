package edu.java.bot.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public final class ResourceFileUtils {
    private ResourceFileUtils() {
    }

    public static @NotNull String getFileNameWithoutExtension(@NotNull String filename) {
        String extPattern = "(?<!^)[.]" + ".*";
        return filename.replaceAll(extPattern, "");
    }

    public static @NotNull File getResourceRootFolder() throws URISyntaxException {
        URL rootUri = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("."));
        return new File(rootUri.toURI());
    }
}
