package edu.java.bot.dialog.handlers.independent;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.handlers.UpdateHandler;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "app.on-startup.skip-updates=false")
@ExtendWith(MockitoExtension.class)
public class UnknownMessageHandlerTest {
    private static final long USER_ID = 6L;
    private static final UserData USER_DATA = new UserData(
        USER_ID,
        BotState.UNINITIALIZED,
        Locale.ENGLISH
    );
    @Mock
    private static Update update;
    @Autowired
    private UpdateHandler unknownMessageHandler;

    @Test
    void givenCorrectUpdate_thenCorrectHandling() {
        USER_DATA.setDialogState(BotState.MAIN_MENU);
        var responses = unknownMessageHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }

    @Test
    void givenCorrectUpdate_thenCorrectUserStateTransition() {
        USER_DATA.setDialogState(BotState.MAIN_MENU);

        BotState expectedState = USER_DATA.getDialogState();
        unknownMessageHandler.handle(update, USER_DATA);
        BotState actualState = USER_DATA.getDialogState();

        assertThat(actualState).isEqualTo(expectedState);
    }

    @Test
    void givenCorrectUpdateWithIncorrectCommand_thenNotEmptyReturned() {
        USER_DATA.setDialogState(BotState.MAIN_MENU);
        var responses = unknownMessageHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }

    @Test
    void givenCorrectUpdateWithUnregisteredUser_thenNotEmptyReturned() {
        USER_DATA.setDialogState(BotState.UNINITIALIZED);

        var responses = unknownMessageHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }
}
