package edu.java.bot.dialog.handlers.independent;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.data.UserDataStorage;
import edu.java.bot.dialog.handlers.UpdateHandler;
import org.junit.jupiter.api.BeforeEach;
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
    private static final UserData USER_DATA = UserData.constructInitialFromId(USER_ID);
    @Mock
    private static Update update;
    @Autowired
    private UpdateHandler unknownMessageHandler;
    @Autowired
    private UserDataStorage userDataStorage;

    @BeforeEach
    void tearUp() {
        userDataStorage.addUser(USER_DATA);
        userDataStorage.setUserState(USER_DATA, BotState.MAIN_MENU);
    }

    @Test
    void givenCorrectUpdate_thenCorrectHandling() {
        var responses = unknownMessageHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }

    @Test
    void givenCorrectUpdate_thenCorrectUserStateTransition() {
        BotState expectedState = USER_DATA.getDialogState();
        unknownMessageHandler.handle(update, USER_DATA);
        BotState actualState = USER_DATA.getDialogState();

        assertThat(actualState).isEqualTo(expectedState);
    }

    @Test
    void givenCorrectUpdateWithIncorrectCommand_thenNotEmptyReturned() {
        var responses = unknownMessageHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }

    @Test
    void givenCorrectUpdateWithUnregisteredUser_thenNotEmptyReturned() {
        userDataStorage.setUserState(USER_DATA, BotState.UNINITIALIZED);

        var responses = unknownMessageHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }
}
