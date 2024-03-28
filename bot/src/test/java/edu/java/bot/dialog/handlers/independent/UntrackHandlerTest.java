package edu.java.bot.dialog.handlers.independent;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.Link;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.handlers.UpdateHandler;
import edu.java.bot.rest.service.LinksService;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UntrackHandlerTest {
    private static final long USER_ID = 7L;
    private static final String CORRECT_COMMAND = "/untrack";
    private static final UserData USER_DATA = new UserData(
        USER_ID,
        BotState.UNINITIALIZED,
        Locale.ENGLISH
    );
    private static final Link LINK = new Link(URI.create("https://github.com"));
    @Mock
    private static Update update;
    @Mock
    private static Message message;
    @Autowired
    private UpdateHandler untrackHandler;
    @Autowired
    private LinksService linksService;

    @Test
    void givenCorrectUpdate_thenCorrectHandling() {
        USER_DATA.setDialogState(BotState.MAIN_MENU);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(CORRECT_COMMAND);
        Mockito.when(linksService.getLinks(USER_ID)).thenReturn(Optional.of(List.of(LINK)));

        var responses = untrackHandler.handle(update, USER_DATA);

        assertThat(responses).isNotEmpty();
    }

    @Test
    @Order(1)
    void givenCorrectUpdate_whenNoUserLinks_thenCorrectUserStateTransition() {
        USER_DATA.setDialogState(BotState.MAIN_MENU);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(CORRECT_COMMAND);
        Mockito.when(linksService.getLinks(USER_ID)).thenReturn(Optional.empty());
        untrackHandler.handle(update, USER_DATA);

        BotState expectedState = BotState.MAIN_MENU;
        BotState actualState = USER_DATA.getDialogState();

        assertThat(actualState).isEqualTo(expectedState);
    }

    @Test
    @Order(2)
    void givenCorrectUpdate_whenUserHasLinks_thenCorrectUserStateTransition() {
        USER_DATA.setDialogState(BotState.MAIN_MENU);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(CORRECT_COMMAND);
        Mockito.when(linksService.getLinks(USER_ID)).thenReturn(Optional.of(List.of(LINK)));
        untrackHandler.handle(update, USER_DATA);

        BotState expectedState = BotState.RES_UNTRACK_WAITING;
        BotState actualState = USER_DATA.getDialogState();

        assertThat(actualState).isEqualTo(expectedState);
    }

    @Test
    void givenCorrectUpdateWithIncorrectCommand_thenEmptyReturned() {
        USER_DATA.setDialogState(BotState.MAIN_MENU);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("bla");

        var responses = untrackHandler.handle(update, USER_DATA);

        assertThat(responses).isEmpty();
    }

    @Test
    void givenCorrectUpdateWithUnregisteredUser_thenEmptyResponse() {
        USER_DATA.setDialogState(BotState.UNINITIALIZED);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(CORRECT_COMMAND);

        var responses = untrackHandler.handle(update, USER_DATA);

        assertThat(responses).isEmpty();
    }
}
