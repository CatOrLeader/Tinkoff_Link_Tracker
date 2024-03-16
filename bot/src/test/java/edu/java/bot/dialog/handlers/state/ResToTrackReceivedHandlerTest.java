package edu.java.bot.dialog.handlers.state;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.Link;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.data.UserDataStorage;
import edu.java.bot.dialog.data.UserLinksTracker;
import edu.java.bot.dialog.handlers.UpdateHandler;
import java.time.Instant;
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
public class ResToTrackReceivedHandlerTest {
    private static final long USER_ID = 9L;
    private static final String CORRECT_RES = "https://github.com";
    private static final UserData USER_DATA = UserData.constructInitialFromId(USER_ID);
    private static final Link LINK = new Link(CORRECT_RES, Instant.now());

    @Mock
    private static Update update;
    @Mock
    private static Message message;
    @Autowired
    private UpdateHandler resToTrackReceivedHandler;
    @Autowired
    private UserDataStorage userDataStorage;
    @Autowired
    private UserLinksTracker linksTracker;

    @BeforeEach
    void tearUp() {
        userDataStorage.addUser(USER_DATA);
        userDataStorage.setUserState(USER_DATA, BotState.RES_TRACK_WAITING);
    }

    @Test
    void givenCorrectUpdate_thenCorrectHandling() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(CORRECT_RES);

        var responses = resToTrackReceivedHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
        assertThat(linksTracker.getUserLinks(USER_ID)).containsExactly(LINK);
    }

    @Test
    void givenCorrectUpdate_thenCorrectUserStateTransition() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(CORRECT_RES);
        resToTrackReceivedHandler.handle(update, USER_DATA);

        BotState expectedState = BotState.MAIN_MENU;
        BotState actualState = USER_DATA.getDialogState();

        assertThat(actualState).isEqualTo(expectedState);
    }

    @Test
    void givenIncorrectUpdate_thenCorrectUserStateTransition() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("blabla_res");
        resToTrackReceivedHandler.handle(update, USER_DATA);

        BotState expectedState = BotState.RES_TRACK_WAITING;
        BotState actualState = USER_DATA.getDialogState();

        assertThat(actualState).isEqualTo(expectedState);
    }
}
