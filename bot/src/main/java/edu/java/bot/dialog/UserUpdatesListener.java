package edu.java.bot.dialog;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dialog.handlers.UpdateHandlerAggregator;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdatesListener implements UpdatesListener {
    private final UpdateHandlerAggregator aggregator;
    private final TelegramBot bot;
    private Set<Integer> updatesIdsToSkip;

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

    private boolean hasToIgnore(Update update) {
        if (updatesIdsToSkip != null) {
            return updatesIdsToSkip.removeIf(id -> id.equals(update.updateId()));
        }
        return false;
    }
}
