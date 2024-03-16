package edu.java.bot.dialog.handlers.independent;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.data.UserDataStorage;
import edu.java.bot.dialog.handlers.UpdateHandler;
import org.junit.jupiter.api.BeforeEach;
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
public class HelpHandlerTest {
    private static final long USER_ID = 1L;
    private static final String CORRECT_COMMAND = "/help";
    private static final UserData USER_DATA = UserData.constructInitialFromId(USER_ID);
    @Mock
    private static Update update;
    @Mock
    private static Message message;
    @Autowired
    private UpdateHandler helpHandler;
    @Autowired
    private UserDataStorage userDataStorage;

    @BeforeEach
    void tearUp() {
        userDataStorage.addUser(USER_DATA);
        userDataStorage.setUserState(USER_DATA, BotState.MAIN_MENU);
    }

    @Test
    void givenCorrectUpdate_thenCorrectHandling() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(CORRECT_COMMAND);

        var responses = helpHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }

    @Test
    void givenCorrectUpdate_thenCorrectUserStateTransition() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(CORRECT_COMMAND);
        helpHandler.handle(update, USER_DATA);

        BotState expectedState = BotState.MAIN_MENU;
        BotState actualState = USER_DATA.getDialogState();

        assertThat(actualState).isEqualTo(expectedState);
    }

    @Test
    void givenCorrectUpdateWithIncorrectCommand_thenEmptyReturned() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("bla");

        var responses = helpHandler.handle(update, USER_DATA);

        assertThat(responses).isEmpty();
    }

    @Test
    void givenCorrectUpdateWithUnregisteredUser_thenEmptyReturned() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(CORRECT_COMMAND);
        userDataStorage.setUserState(USER_DATA, BotState.UNINITIALIZED);

        var responses = helpHandler.handle(update, USER_DATA);

        assertThat(responses).isEmpty();
    }
}
