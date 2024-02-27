package edu.java.bot.dialog.lang;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.utils.ResourceFileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public final class JsonBotAnswersProvider implements BotAnswersProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<Locale, Map<String, String>> runtimeAnswersMap;

    public JsonBotAnswersProvider() {
        runtimeAnswersMap = new HashMap<>();
        preloadLocalizations();
    }

    private void preloadLocalizations() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Map<String, String>> typeReference = new TypeReference<>() {
        };

        try {
            File rootFolder = ResourceFileUtils.getResourceRootFolder();
            File localizationFolder = Paths.get(rootFolder.getAbsolutePath(), "lang").toFile();

            for (File localizationSource : Objects.requireNonNull(localizationFolder.listFiles())) {
                Locale locale =
                    Locale.forLanguageTag(ResourceFileUtils.getFileNameWithoutExtension(localizationSource.getName()));
                var answers = mapper.readValue(localizationSource, typeReference);

                runtimeAnswersMap.put(locale, answers);
            }
        } catch (URISyntaxException | IOException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String mainMenu(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("main_menu");
    }

    @Override
    public String uninitialized(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("user_uninitialized");
    }

    @Override
    public String userRegistered(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("user_registered");
    }

    @Override
    public String userRegisteredSuggestion(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("user_registered_suggestion");
    }

    @Override
    public String userAlreadyRegistered(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("user_already_registered");
    }

    @Override
    public String resRegisterWaiting(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("res_track_waiting");
    }

    @Override
    public String resRegisteredSuccess(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("res_track_success");
    }

    @Override
    public String resUnregisterWaiting(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("res_untrack_waiting");
    }

    @Override
    public String resUnregisterSuccess(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("res_untrack_success");
    }

    @Override
    public String resTypingError(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("res_typing_error");
    }

    @Override
    public String incorrectMessage(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("incorrect_msg");
    }

    @Override
    public String commands(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("commands");
    }

    @Override
    public String removeBtn(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("btn_remove");
    }

    @Override
    public String newResBtn(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("btn_new_res");
    }

    @Override
    public String remResBtn(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("btn_res_rem");
    }

    @Override
    public String listResBtn(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("btn_list");
    }

    @Override
    public String noResYet(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("no_res_yet");
    }

    @Override
    public String trackedResources(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("tracked_res");
    }
}
