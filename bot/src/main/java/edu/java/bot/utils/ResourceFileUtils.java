package edu.java.bot.utils;

import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public final class ResourceFileUtils {
    private ResourceFileUtils() {
    }

    public static @NotNull String getFileNameWithoutExtension(@NotNull String filename) {
        String extPattern = "(?<!^)[.]" + ".*";
        return filename.replaceAll(extPattern, "");
    }

    public static @NotNull Resource[] getAllLocalizations() throws IOException {
        var resolver = new PathMatchingResourcePatternResolver();
        return resolver.getResources("classpath:/lang/*.json");
    }
}
