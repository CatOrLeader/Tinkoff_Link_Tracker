package edu.java.bot.dialog.handlers.state;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.Link;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.handlers.UpdateHandler;
import edu.java.bot.rest.service.LinksService;
import java.net.URI;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(properties = "app.on-startup.skip-updates=false")
@ExtendWith(MockitoExtension.class)
public class ResToUntrackReceivedHandlerTest {
    private static final long USER_ID = 10L;
    private static final String CORRECT_RES = "https://github.com";
    private static final String CORRECT_QUERY = "cancel " + CORRECT_RES.hashCode();
    private static final UserData USER_DATA = new UserData(
        USER_ID,
        BotState.RES_UNTRACK_WAITING,
        Locale.ENGLISH
    );
    private static final Link LINK = new Link(URI.create(CORRECT_RES));

    @Mock
    private static Update update;
    @Mock
    private static CallbackQuery query;
    @Autowired
    private UpdateHandler resToUntrackReceivedHandler;
    @Autowired
    private LinksService linksService;

    @Test
    void givenCorrectUpdate_thenCorrectHandling() {
        Mockito.when(update.callbackQuery()).thenReturn(query);
        Mockito.when(query.data()).thenReturn(CORRECT_QUERY);
        Mockito.when(linksService.deleteLink(USER_ID, 1L)).thenReturn(LINK);

        var responses = resToUntrackReceivedHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }

    @Test
    void givenCorrectUpdate_thenCorrectUserStateTransition() {
        Mockito.when(update.callbackQuery()).thenReturn(query);
        Mockito.when(query.data()).thenReturn(CORRECT_QUERY);
        Mockito.when(linksService.deleteLink(USER_ID, 1L)).thenReturn(LINK);
        resToUntrackReceivedHandler.handle(update, USER_DATA);

        BotState expectedState = BotState.MAIN_MENU;
        BotState actualState = USER_DATA.getDialogState();

        assertThat(actualState).isEqualTo(expectedState);
    }

    @Test
    void givenIncorrectUpdate_thenEmpty() {
        Mockito.when(update.callbackQuery()).thenReturn(query);
        Mockito.when(query.data()).thenReturn(null);

        assertThat(resToUntrackReceivedHandler.handle(update, USER_DATA)).isEmpty();
    }
}
