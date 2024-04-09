package edu.java.bot.dialog.lang;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.utils.ResourceFileUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
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
            Resource[] localizations = ResourceFileUtils.getAllLocalizations();

            for (var localizationSource : localizations) {
                Locale locale =
                    Locale.forLanguageTag(ResourceFileUtils.getFileNameWithoutExtension(Objects.requireNonNull(
                        localizationSource.getFilename())));
                var answers = mapper.readValue(localizationSource.getContentAsByteArray(), typeReference);

                runtimeAnswersMap.put(locale, answers);
            }
        } catch (IOException e) {
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
        return runtimeAnswersMap.get(locale).get("state_track_waiting");
    }

    @Override
    public String resRegisteredSuccess(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("state_track_success");
    }

    @Override
    public String transitionUntrackWaiting(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("transition_untrack_waiting");
    }

    @Override
    public String resUnregisterSuccess(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("state_untrack_success");
    }

    @Override
    public String resTypingError(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("state_typing_error");
    }

    @Override
    public String transitionTrackWaiting(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("transition_track_waiting");
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
    public String goBackBtn(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("btn_go_back");
    }

    @Override
    public String transitionList(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("transition_list");
    }

    @Override
    public String stateList(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("state_list");
    }

    @Override
    public String noResYet(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("no_res_yet");
    }

    @Override
    public String trackedResources(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("state_tracked_res");
    }

    @Override
    public String msgNotification(@NotNull Locale locale) {
        return runtimeAnswersMap.get(locale).get("msg_notification");
    }
}
