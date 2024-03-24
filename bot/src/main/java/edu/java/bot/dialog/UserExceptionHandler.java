package edu.java.bot.dialog;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserExceptionHandler implements ExceptionHandler {
    @Override
    public void onException(TelegramException e) {
        log.error("Something went wrong since user request", e);
    }
}
