package edu.java.bot.dialog.handlers.state;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.Link;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.handlers.UpdateHandler;
import edu.java.bot.dialog.lang.BotAnswersProvider;
import edu.java.bot.rest.service.LinksService;
import edu.java.bot.utils.BotResponsesUtils;
import edu.java.bot.utils.MessagesApprovalUtils;
import java.net.URI;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
@RequiredArgsConstructor
public final class ResToTrackReceivedHandler implements UpdateHandler {
    private final BotAnswersProvider answersProvider;
    private final UpdateHandler menuHandler;
    private final LinksService linksService;

    @Override
    public Optional<BaseRequest[]> handle(@NotNull Update update, @NotNull UserData userData) {
        if (!MessagesApprovalUtils.isMsgExists(update.message())) {
            return Optional.empty();
        }

        String maybeQuery = update.message().text();
        Optional<UpdateHandler> maybeHandler = getAppropriateHandler(maybeQuery, userData);
        BaseRequest[] response;

        if (maybeHandler.isPresent()) {
            var handler = maybeHandler.get();
            response = handler.constructTemplateResponse(update, userData);
            handler.setStateToLogicallyNext(response, userData);
        } else {
            response = constructTemplateResponse(update, userData);
            setStateToLogicallyNext(response, userData);
        }

        return Optional.of(response);
    }

    @Override
    public @NotNull BaseRequest[] constructTemplateResponse(@NotNull Update update, @NotNull UserData userData) {
        if (MessagesApprovalUtils.isCorrectResource(update.message())) {
            Link receivedLink = new Link(URI.create(update.message().text().strip()));

            try {
                linksService.postLink(userData.getUserId(), receivedLink);
            } catch (HttpClientErrorException e) {
                log.warn("This link is not approved by the server: {}", receivedLink.getUrl());
                return incorrectResourceProvided(update, userData);
            }

            var response = new BaseRequest[] {new SendMessage(
                userData.getUserId(),
                answersProvider.resRegisteredSuccess(userData.getLocale())
            ).replyToMessageId(update.message().messageId()).parseMode(ParseMode.Markdown)};
            return BotResponsesUtils.concatenate(response, menuHandler.constructTemplateResponse(update, userData));
        } else {
            return incorrectResourceProvided(update, userData);
        }
    }

    private BaseRequest[] incorrectResourceProvided(Update update, UserData userData) {
        return new BaseRequest[] {
            new SendMessage(
                userData.getUserId(),
                answersProvider.resTypingError(userData.getLocale())
            ).replyToMessageId(update.message().messageId()).parseMode(ParseMode.Markdown)
        };
    }

    @Override
    public void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData) {
        if (responses.length > 1) {
            userData.setDialogState(BotState.MAIN_MENU);
        }
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
