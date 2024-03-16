package edu.java.bot;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dialog.UserUpdatesListener;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class Bot {
    private final TelegramBot bot;
    private final ApplicationConfig config;
    private final UserUpdatesListener updatesListener;
    private final ExceptionHandler exceptionHandler;

    @Autowired
    public Bot(
        @NotNull ApplicationConfig config,
        @NotNull UserUpdatesListener updatesListener,
        @NotNull ExceptionHandler exceptionHandler
    ) {
        this.config = config;
        this.updatesListener = updatesListener;
        this.exceptionHandler = exceptionHandler;
        bot = new TelegramBot(this.config.telegramToken());
    }

    @PostConstruct
    public void bootstrap() {
        if (config.onStartup().skipUpdates()) {
            updatesListener.receiveUpdatesToSkip(updates());
        }

        updatesListener.receiveBot(bot);
        bot.setUpdatesListener(updatesListener, exceptionHandler);
    }

    @PreDestroy
    public void destroy() {
        bot.removeGetUpdatesListener();
        bot.shutdown();
    }

    public @NotNull Set<Integer> updates() {
        return bot.execute(new GetUpdates()).updates().stream()
            .mapToInt(Update::updateId)
            .boxed()
            .collect(Collectors.toSet());
    }
}
