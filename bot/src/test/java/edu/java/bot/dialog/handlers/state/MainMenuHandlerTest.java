package edu.java.bot.dialog.handlers.state;

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
public class MainMenuHandlerTest {
    private static final long USER_ID = 8L;
    private static final String QUERY = "any";
    private static final UserData USER_DATA = new UserData(
        USER_ID,
        BotState.MAIN_MENU,
        Locale.ENGLISH
    );
    @Mock
    private static Update update;
    @Mock
    private static Message message;
    @Autowired
    private UpdateHandler mainMenuHandler;

    @Test
    void givenCorrectUpdate_thenCorrectHandling() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(QUERY);

        var responses = mainMenuHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }

    @Test
    void givenCorrectUpdateWithIncorrectCommand_thenNotEmptyReturned() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("bla");

        var responses = mainMenuHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }

    @Test
    void givenCorrectUpdateWithUnregisteredUser_thenNotEmptyReturned() {
        USER_DATA.setDialogState(BotState.UNINITIALIZED);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(QUERY);

        var responses = mainMenuHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }
}
