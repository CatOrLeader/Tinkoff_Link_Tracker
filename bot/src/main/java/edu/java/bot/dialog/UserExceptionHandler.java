package edu.java.bot.dialog;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;

@Component
public class UserExceptionHandler implements ExceptionHandler {
    @Override
    public void onException(TelegramException e) {
        LogManager.getLogger().error(e);
    }
}
