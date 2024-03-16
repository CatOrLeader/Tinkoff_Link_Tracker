package edu.java.bot.dialog.handlers.state;

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
public class UninitializedHandlerTest {
    private static final long USER_ID = 11L;
    private static final String ANY = "any";
    private static final UserData USER_DATA = UserData.constructInitialFromId(USER_ID);
    @Mock
    private static Update update;
    @Mock
    private static Message message;
    @Autowired
    private UpdateHandler uninitializedHandler;
    @Autowired
    private UserDataStorage userDataStorage;

    @BeforeEach
    void tearUp() {
        userDataStorage.addUser(USER_DATA);
        userDataStorage.setUserState(USER_DATA, BotState.UNINITIALIZED);
    }

    @Test
    void givenCorrectUpdate_thenCorrectHandling() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(ANY);

        var responses = uninitializedHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }

    @Test
    void givenCorrectUpdate_thenCorrectUserStateTransition() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(ANY);
        uninitializedHandler.handle(update, USER_DATA);

        BotState expectedState = BotState.UNINITIALIZED;
        BotState actualState = USER_DATA.getDialogState();

        assertThat(actualState).isEqualTo(expectedState);
    }

    @Test
    void givenCorrectUpdateWithIncorrectCommand_thenEmptyReturned() {
        Mockito.when(update.message()).thenReturn(null);

        var responses = uninitializedHandler.handle(update, USER_DATA);

        assertThat(responses).isEmpty();
    }
}
