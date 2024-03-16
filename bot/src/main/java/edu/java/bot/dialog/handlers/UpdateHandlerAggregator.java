package edu.java.bot.dialog.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.data.UserDataStorage;
import edu.java.bot.utils.UserInfoUtils;
import java.util.Arrays;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class UpdateHandlerAggregator {
    private final HandlerStorage handlerStorage;
    private final UserDataStorage userDataStorage;

    @Autowired
    public UpdateHandlerAggregator(
        @NotNull HandlerStorage handlerStorage,
        @NotNull UserDataStorage userDataStorage
    ) {
        this.handlerStorage = handlerStorage;
        this.userDataStorage = userDataStorage;
    }

    public @NotNull Optional<BaseRequest[]> process(Update update) {
        var maybeUserId = UserInfoUtils.extractUserId(update);
        if (maybeUserId.isEmpty()) {
            return Optional.empty();
        }
        long userId = maybeUserId.get();

        UserData userData;
        var maybeUserData = userDataStorage.getUserById(userId);
        if (maybeUserData.isPresent()) {
            userData = maybeUserData.get();
        } else {
            userData = UserData.constructInitialFromId(userId);
            userDataStorage.addUser(userData);
        }

        var response = handle(update, userData);
        LogManager.getLogger().info(userData.getDialogState().name() + " <--- " + Arrays.toString(response));
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
