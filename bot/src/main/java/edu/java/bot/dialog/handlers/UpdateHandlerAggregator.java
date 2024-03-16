package edu.java.bot.dialog.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.rest.service.TgChatService;
import edu.java.bot.utils.UserInfoUtils;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
@RequiredArgsConstructor
public final class UpdateHandlerAggregator {
    private final HandlerStorage handlerStorage;
    private final TgChatService tgChatService;

    public @NotNull Optional<BaseRequest[]> process(Update update) {
        var maybeUserId = UserInfoUtils.extractUserId(update);
        if (maybeUserId.isEmpty()) {
            return Optional.empty();
        }
        long userId = maybeUserId.get();

        UserData userData = null;

        try {
            userData = tgChatService.getChat(userId).orElseThrow();
        } catch (HttpClientErrorException | NoSuchElementException e) {
            log.warn("User is not registered", e);
        }

        if (userData == null) {
            try {
                tgChatService.registerNewChat(userId);
                userData = tgChatService.getChat(userId).orElseThrow();
            } catch (HttpClientErrorException | NoSuchElementException e) {
                log.warn("After registering user it does not exists", e);
                return Optional.empty();
            }
        }

        var response = handle(update, userData);
        try {
            tgChatService.updateChat(userData);
        } catch (HttpClientErrorException e) {
            log.warn("It is impossible to change the state of the user on the server side", e);
        }
        log.info(userData.getDialogState().name() + " <--- " + Arrays.toString(response));
        return Optional.of(response);
    }

    private BaseRequest[] handle(Update update, UserData userData) {
        return handleIndependently(update, userData).orElseGet(
            () -> handleStateWise(update, userData).orElseGet(
                () -> handlerStorage.unresolvedMsgHandler.handle(update, userData)
                    .orElseThrow()
            )
        );
    }

    private Optional<BaseRequest[]> handleIndependently(Update update, UserData userData) {
        for (UpdateHandler handler : handlerStorage.independentHandlers) {
            var response = handler.handle(update, userData);
            if (response.isPresent()) {
                return response;
            }
        }

        return Optional.empty();
    }

    private Optional<BaseRequest[]> handleStateWise(Update update, UserData userData) {
        return handlerStorage.stateHandlers.get(userData.getDialogState()).handle(update, userData);
    }
}
