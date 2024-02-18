package edu.java.bot.dialog;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dialog.handlers.UpdateHandlerAggregator;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUpdatesListener implements UpdatesListener {
    private final UpdateHandlerAggregator aggregator;
    private TelegramBot bot;
    private Set<Integer> updatesIdsToSkip;

    @Autowired
    public UserUpdatesListener(
        @NotNull UpdateHandlerAggregator aggregator
    ) {
        this.aggregator = aggregator;
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (hasToIgnore(update)) {
                continue;
            }

            aggregator.process(update)
                .ifPresent(baseRequests -> Arrays.stream(baseRequests).forEach(bot::execute));
        }
        return CONFIRMED_UPDATES_ALL;
    }

    public void receiveUpdatesToSkip(@NotNull Set<Integer> updatesIdsToSkip) {
        this.updatesIdsToSkip = updatesIdsToSkip;
    }

    public void receiveBot(@NotNull TelegramBot bot) {
        this.bot = bot;
    }

    private boolean hasToIgnore(Update update) {
        if (updatesIdsToSkip != null) {
            return updatesIdsToSkip.removeIf(id -> id.equals(update.updateId()));
        }
        return false;
    }
}
