package edu.java.bot.dialog.handlers.independent;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.handlers.UpdateHandler;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "app.on-startup.skip-updates=false")
@ExtendWith(MockitoExtension.class)
public class TrackHandlerTest {
    private static final long USER_ID = 5L;
    private static final String CORRECT_COMMAND = "/track";
    private static final UserData USER_DATA = new UserData(
        USER_ID,
        BotState.UNINITIALIZED,
        Locale.ENGLISH
    );
    @Mock
    private static Update update;
    @Mock
    private static Message message;
    @Autowired
    private UpdateHandler trackHandler;

    @Test
    void givenCorrectUpdate_thenCorrectHandling() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(CORRECT_COMMAND);
        USER_DATA.setDialogState(BotState.MAIN_MENU);

        var responses = trackHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }

    @Test
    void givenCorrectUpdate_thenCorrectUserStateTransition() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(CORRECT_COMMAND);
        USER_DATA.setDialogState(BotState.MAIN_MENU);
        trackHandler.handle(update, USER_DATA);

        BotState expectedState = BotState.RES_TRACK_WAITING;
        BotState actualState = USER_DATA.getDialogState();

        assertThat(actualState).isEqualTo(expectedState);
    }

    @Test
    void givenCorrectUpdateWithIncorrectCommand_thenEmptyReturned() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("bla");
        USER_DATA.setDialogState(BotState.MAIN_MENU);

        var responses = trackHandler.handle(update, USER_DATA);

        assertThat(responses).isEmpty();
    }

    @Test
    void givenCorrectUpdateWithUnregisteredUser_thenEmptyResponse() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(CORRECT_COMMAND);
        USER_DATA.setDialogState(BotState.UNINITIALIZED);

        var responses = trackHandler.handle(update, USER_DATA);

        assertThat(responses).isEmpty();
    }
}
