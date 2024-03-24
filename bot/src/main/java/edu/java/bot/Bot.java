package edu.java.bot;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dialog.UserUpdatesListener;
import jakarta.annotation.PreDestroy;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class Bot {
    private final TelegramBot telegramBot;
    private final ApplicationConfig config;
    private final UserUpdatesListener updatesListener;
    private final ExceptionHandler exceptionHandler;

    @EventListener(ApplicationReadyEvent.class)
    public void bootstrap() {
        if (config.onStartup().skipUpdates()) {
            updatesListener.receiveUpdatesToSkip(updates());
        }

        telegramBot.setUpdatesListener(updatesListener, exceptionHandler);
    }

    @PreDestroy
    public void destroy() {
        telegramBot.removeGetUpdatesListener();
        telegramBot.shutdown();
    }

    public @NotNull Set<Integer> updates() {
        return telegramBot.execute(new GetUpdates()).updates().stream()
            .mapToInt(Update::updateId)
            .boxed()
            .collect(Collectors.toSet());
    }
}
