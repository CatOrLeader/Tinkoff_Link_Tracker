package edu.java.bot.dialog.handlers.state;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.handlers.UpdateHandler;
import edu.java.bot.dialog.lang.BotAnswersProvider;
import edu.java.bot.utils.MessagesApprovalUtils;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainMenuHandler implements UpdateHandler {
    private final BotAnswersProvider answersProvider;
    private final UpdateHandler trackHandler;
    private final UpdateHandler untrackHandler;
    private final UpdateHandler listHandler;
    private final UpdateHandler unknownMessageHandler;

    @Override
    public Optional<BaseRequest[]> handle(@NotNull Update update, @NotNull UserData userData) {
        if (!MessagesApprovalUtils.isMsgExists(update.message())) {
            return Optional.empty();
        }

        String maybeQuery = update.message().text();
        UpdateHandler handler = getAppropriateHandler(maybeQuery, userData);

        var response = handler.constructTemplateResponse(update, userData);
        handler.setStateToLogicallyNext(response, userData);

        return Optional.of(response);
    }

    @Override
    public @NotNull BaseRequest[] constructTemplateResponse(@NotNull Update update, @NotNull UserData userData) {
        String maybeQuery = update.message().text();
        return getAppropriateHandler(maybeQuery, userData).constructTemplateResponse(update, userData);
    }

    @Override
    public void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData) {
    }

    private UpdateHandler getAppropriateHandler(String query, UserData userData) {
        Locale userLocale = userData.getLocale();
        if (query.contentEquals(answersProvider.newResBtn(userLocale))) {
            return trackHandler;
        } else if (query.contentEquals(answersProvider.remResBtn(userLocale))) {
            return untrackHandler;
        } else if (query.contentEquals(answersProvider.listResBtn(userLocale))) {
            return listHandler;
        } else {
            return unknownMessageHandler;
        }
    }
}
