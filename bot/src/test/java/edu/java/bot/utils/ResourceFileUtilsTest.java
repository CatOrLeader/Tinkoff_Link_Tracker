package edu.java.bot.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ResourceFileUtilsTest {
    @Test
    void givenPackageOfNames_thenGetRightFilenamesWithoutExtension() {
        final List<String> filenamesWithExtensions = List.of(
            "filename", "filename.txt", ".filename", ".filename.conf", "filename.tar.gz", "filename.conf.bak"
        );

        Pattern pattern = Pattern.compile("\\.?filename");
        List<String> processedNames = filenamesWithExtensions.stream()
            .map(ResourceFileUtils::getFileNameWithoutExtension)
            .toList();

        assertThat(processedNames).allMatch(string -> pattern.matcher(string).matches());
    }

    @Test
    void getCorrectResourceFolder() throws URISyntaxException {
        File expectedFolder =
            new File(Objects.requireNonNull(Objects.requireNonNull(ResourceFileUtilsTest.class.getResource("."))
                .getFile()));
        File actualFolder = ResourceFileUtils.getResourceRootFolder();

        assertThat(
            Path.of(actualFolder.getAbsolutePath(), "edu", "java", "bot", "utils")
                .toString()).isEqualTo(expectedFolder.getAbsolutePath());
    }
}
