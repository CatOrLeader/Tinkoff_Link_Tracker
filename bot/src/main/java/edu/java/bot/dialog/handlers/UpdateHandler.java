package edu.java.bot.dialog.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import edu.java.bot.dialog.data.UserData;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface UpdateHandler {
    Optional<BaseRequest[]> handle(@NotNull Update update, @NotNull UserData userData);

    @NotNull BaseRequest[] constructTemplateResponse(@NotNull Update update, @NotNull UserData userData);

    void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData);
}
