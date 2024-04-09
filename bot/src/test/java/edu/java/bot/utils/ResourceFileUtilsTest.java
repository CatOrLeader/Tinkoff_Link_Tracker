package edu.java.bot.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
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
    void getCorrectLocalization() throws IOException {
        Resource[] actualLocalizations = ResourceFileUtils.getAllLocalizations();

        var engRes = Mockito.mock(Resource.class);
        Mockito.when(engRes.getFilename()).thenReturn("en.json");
        Resource[] expectedLocalizations = new Resource[] {engRes};

        assertThat(Arrays.stream(actualLocalizations).map(Resource::getFilename).toList())
            .containsExactlyElementsOf(Arrays.stream(expectedLocalizations).map(Resource::getFilename).toList());
    }
}
