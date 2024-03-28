package edu.java.bot.dialog.handlers.independent;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
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
public final class UntrackHandler implements UpdateHandler {
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
    public @NotNull BaseRequest[] constructTemplateResponse(@NotNull Update update, @NotNull UserData userData) {
        var userId = userData.getUserId();
        Locale userLocale = userData.getLocale();
        Optional<List<Link>> maybeLinks = Optional.empty();

        try {
            maybeLinks = linksService.getLinks(userId);
        } catch (HttpClientErrorException e) {
            log.error("User links are not retrieved from the server (untrack case)", e);
        }

        return maybeLinks.map(links -> new SendMessage[] {
            new SendMessage(
                userId,
                answersProvider.transitionUntrackWaiting(userLocale)
            ).replyMarkup(keyboard.goBack(userLocale)).parseMode(ParseMode.Markdown),
            new SendMessage(
                userId,
                answersProvider.trackedResources(userLocale)
            ).replyMarkup(keyboard.userLinksWithRemoveButton(userLocale, links))
                .parseMode(ParseMode.Markdown)
        }).orElseGet(() -> new SendMessage[] {new SendMessage(
            userId,
            answersProvider.noResYet(userLocale)
        ).parseMode(ParseMode.Markdown)});
    }

    @Override
    public void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData) {
        if (responses.length > 1) { // IT is truly a nightmare, BTW I for now cannot imagine something else
            userData.setDialogState(BotState.RES_UNTRACK_WAITING);
        }
    }

    private boolean isInappropriateRequest(Update update, UserData userData) {
        return !MessagesApprovalUtils.equalsTextMessageContent(update.message(), "/untrack")
               || !userData.isRegistered();
    }
}
