package edu.java.bot.dialog.handlers.state;

import com.pengrad.telegrambot.model.Message;
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
public class ResToTrackReceivedHandlerTest {
    private static final long USER_ID = 9L;
    private static final String CORRECT_RES = "https://github.com";
    private static final UserData USER_DATA = new UserData(
        USER_ID,
        BotState.RES_TRACK_WAITING,
        Locale.ENGLISH
    );
    private static final Link LINK = new Link(URI.create(CORRECT_RES));

    @Mock
    private static Update update;
    @Mock
    private static Message message;
    @Autowired
    private UpdateHandler resToTrackReceivedHandler;
    @Autowired
    private LinksService linksService;

    @Test
    void givenCorrectUpdate_thenCorrectHandling() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(CORRECT_RES);
        Mockito.when(linksService.postLink(USER_ID, LINK)).thenReturn(new Link(URI.create(CORRECT_RES)));

        var responses = resToTrackReceivedHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }

    @Test
    void givenCorrectUpdate_thenCorrectUserStateTransition() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(CORRECT_RES);
        Mockito.when(linksService.postLink(USER_ID, LINK)).thenReturn(new Link(LINK.getUrl()));
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
