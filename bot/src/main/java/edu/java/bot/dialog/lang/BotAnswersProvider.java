package edu.java.bot.dialog.lang;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public sealed interface BotAnswersProvider permits JsonBotAnswersProvider {
    String mainMenu(@NotNull Locale locale);

    String uninitialized(@NotNull Locale locale);

    String userRegistered(@NotNull Locale locale);

    String userRegisteredSuggestion(@NotNull Locale locale);

    String userAlreadyRegistered(@NotNull Locale locale);

    String resRegisterWaiting(@NotNull Locale locale);

    String resRegisteredSuccess(@NotNull Locale locale);

    String resUnregisterWaiting(@NotNull Locale locale);

    String resUnregisterSuccess(@NotNull Locale locale);

    String resTypingError(@NotNull Locale locale);

    String incorrectMessage(@NotNull Locale locale);

    String commands(@NotNull Locale locale);

    String removeBtn(@NotNull Locale locale);

    String newResBtn(@NotNull Locale locale);

    String remResBtn(@NotNull Locale locale);

    String listResBtn(@NotNull Locale locale);

    String noResYet(@NotNull Locale locale);

    String trackedResources(@NotNull Locale locale);
}
