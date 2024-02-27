package edu.java.bot.dialog.handlers.state;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.handlers.UpdateHandler;
import edu.java.bot.dialog.lang.BotAnswersProvider;
import edu.java.bot.utils.MessagesApprovalUtils;
import java.util.Locale;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public final class ResListHandler implements UpdateHandler {
    private final BotAnswersProvider answersProvider;
    private final UpdateHandler menuHandler;
    private final UpdateHandler unknownMessageHandler;

    public ResListHandler(
        @NotNull BotAnswersProvider answersProvider,
        @NotNull UpdateHandler menuHandler,
        @NotNull UpdateHandler unknownMessageHandler
    ) {
        this.answersProvider = answersProvider;
        this.menuHandler = menuHandler;
        this.unknownMessageHandler = unknownMessageHandler;
    }

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
        if (query.contentEquals(answersProvider.goBackBtn(userLocale))) {
            return menuHandler;
        } else {
            return unknownMessageHandler;
        }
    }
}
