package edu.java.bot.dialog.handlers.state;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.handlers.UpdateHandler;
import edu.java.bot.dialog.lang.BotAnswersProvider;
import edu.java.bot.rest.service.LinksService;
import edu.java.bot.utils.BotResponsesUtils;
import edu.java.bot.utils.MessagesApprovalUtils;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
@Slf4j
public final class ResToUntrackReceivedHandler implements UpdateHandler {
    private final BotAnswersProvider answersProvider;
    private final UpdateHandler menuHandler;
    private final LinksService linksService;

    @Override
    public Optional<BaseRequest[]> handle(@NotNull Update update, @NotNull UserData userData) {
        if (MessagesApprovalUtils.isMsgExists(update.message())) {
            var maybeHandler = getAppropriateHandler(update.message().text(), userData);
            if (maybeHandler.isPresent()) {
                var handler = maybeHandler.get();
                var response = handler.constructTemplateResponse(update, userData);
                handler.setStateToLogicallyNext(response, userData);
                return Optional.ofNullable(response);
            }
        }

        if (!MessagesApprovalUtils.isCallbackQueryExists(update)) {
            return Optional.empty();
        }

        var callbackQuery = update.callbackQuery();
        var userId = userData.getUserId();

        try {
            linksService.deleteLink(userId, BotResponsesUtils.extractLinkCodeFromCallbackQuery(callbackQuery));
        } catch (HttpClientErrorException e) {
            log.error("Link cannot be removed due to the server exception", e);
            return Optional.empty();
        }

        var response = constructTemplateResponse(update, userData);
        setStateToLogicallyNext(response, userData);

        return Optional.of(response);
    }

    @Override
    public @NotNull BaseRequest[] constructTemplateResponse(@NotNull Update update, @NotNull UserData userData) {
        var innerResponse = new SendMessage[] {new SendMessage(
            userData.getUserId(),
            answersProvider.resUnregisterSuccess(userData.getLocale())
        ).parseMode(ParseMode.Markdown)};
        var outerResponse = menuHandler.constructTemplateResponse(update, userData);

        return BotResponsesUtils.concatenate(innerResponse, outerResponse);
    }

    @Override
    public void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData) {
        userData.setDialogState(BotState.MAIN_MENU);
    }

    private Optional<UpdateHandler> getAppropriateHandler(String query, UserData userData) {
        Locale userLocale = userData.getLocale();
        if (query.contentEquals(answersProvider.goBackBtn(userLocale))) {
            return Optional.of(menuHandler);
        } else {
            return Optional.empty();
        }
    }
}
