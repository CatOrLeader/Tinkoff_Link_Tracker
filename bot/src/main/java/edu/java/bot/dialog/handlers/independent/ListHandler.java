package edu.java.bot.dialog.handlers.independent;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.Link;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.handlers.UpdateHandler;
import edu.java.bot.dialog.lang.BotAnswersProvider;
import edu.java.bot.dialog.lang.Keyboard;
import edu.java.bot.rest.service.LinksService;
import edu.java.bot.utils.MessagesApprovalUtils;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
@Slf4j
@RequiredArgsConstructor
public final class ListHandler implements UpdateHandler {
    private final BotAnswersProvider answersProvider;
    private final Keyboard keyboard;
    private final LinksService linksService;

    @Override
    public Optional<BaseRequest[]> handle(@NotNull Update update, @NotNull UserData userData) {
        if (isInappropriateRequest(update, userData)) {
            return Optional.empty();
        }

        var response = constructTemplateResponse(update, userData);
        setStateToLogicallyNext(response, userData);

        return Optional.of(response);
    }

    @Override
    public BaseRequest[] constructTemplateResponse(@NotNull Update update, @NotNull UserData userData) {
        var userId = userData.getUserId();
        var userLocale = userData.getLocale();
        Optional<List<Link>> maybeLinks = Optional.empty();

        try {
            maybeLinks = linksService.getLinks(userId);
        } catch (HttpClientErrorException e) {
            log.error("User links are not retrieved from the server (list case)", e);
        }

        if (maybeLinks.isEmpty()) {
            return new SendMessage[] {new SendMessage(userId, answersProvider.noResYet(userLocale))};
        }
        var links = maybeLinks.get();

        return new BaseRequest[] {
            new SendMessage(
                userId,
                answersProvider.transitionList(userLocale)
            ).replyMarkup(keyboard.goBack(userLocale)),
            new SendMessage(
                userId, answersProvider.stateList(userLocale)
            ).replyMarkup(keyboard.userLinks(links))
        };
    }

    @Override
    public void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData) {
        if (responses.length > 1) {
            userData.setDialogState(BotState.RES_LIST);
        } else {
            userData.setDialogState(BotState.MAIN_MENU);
        }
    }

    private boolean isInappropriateRequest(Update update, UserData userData) {
        return !MessagesApprovalUtils.equalsTextMessageContent(update.message(), "/list")
               || !userData.isRegistered();
    }
}
