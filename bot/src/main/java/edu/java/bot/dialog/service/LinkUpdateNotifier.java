package edu.java.bot.dialog.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.lang.BotAnswersProvider;
import edu.java.bot.rest.model.LinkUpdateRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkUpdateNotifier {
    private final TelegramBot telegramBot;
    private final BotAnswersProvider answersProvider;

    public void notifyAll(@NotNull LinkUpdateRequest request) {
        request.tgChatIds().stream()
            .map(UserData::new)
            .map(userData -> constructNotifyMessage(userData, request.url(), request.description()))
            .forEach(telegramBot::execute);
    }

    private SendMessage constructNotifyMessage(UserData chatResponse, URI link, String description) {
        return new SendMessage(
            chatResponse.getUserId(),
            String.format("%s%s\n\n%s", answersProvider.msgNotification(chatResponse.getLocale()), link, description)
        );
    }
}
