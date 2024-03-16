package edu.java.bot.utils;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MessagesApprovalUtilsTest {
    @Mock
    private Message message;
    @Mock
    private Update update;
    @Mock
    private CallbackQuery query;

    @Test
    void givenExistingMsg_thenTrue() {
        Mockito.when(message.text()).thenReturn("temp");

        assertThat(MessagesApprovalUtils.isMsgExists(message)).isTrue();
    }

    @Test
    void givenNonExistingMsg_thenFalse() {
        Message messageFake = Mockito.mock(Message.class);

        assertThat(MessagesApprovalUtils.isMsgExists(messageFake)).isFalse();
    }

    @Test
    void givenExistingMsgWithNoText_thenFalse() {
        Mockito.when(message.text()).thenReturn(null);

        assertThat(MessagesApprovalUtils.isMsgExists(message)).isFalse();
    }

    @Test
    void givenCorrectResource_thenTrue() {
        Mockito.when(message.text()).thenReturn("https://some_link");

        assertThat(MessagesApprovalUtils.isCorrectResource(message)).isTrue();
    }

    @Test
    void givenIncorrectResource_thenFalse() {
        Mockito.when(message.text()).thenReturn("abrakadabra");

        assertThat(MessagesApprovalUtils.isCorrectResource(message)).isFalse();
    }

    @Test
    void givenExistingUpdWithCallbackQuery_thenTrue() {
        Mockito.when(update.callbackQuery()).thenReturn(query);
        Mockito.when(query.data()).thenReturn("some data");

        assertThat(MessagesApprovalUtils.isCallbackQueryExists(update)).isTrue();
    }

    @Test
    void givenNonExistingUpd_thenFalse() {
        Update updateFake = Mockito.mock(Update.class);

        assertThat(MessagesApprovalUtils.isCallbackQueryExists(updateFake)).isFalse();
    }

    @Test
    void givenExistingUpdWithNoCQuery_thenFalse() {
        Mockito.when(update.callbackQuery()).thenReturn(null);

        assertThat(MessagesApprovalUtils.isCallbackQueryExists(update)).isFalse();
    }

    @Test
    void givenExistingMsgWithEqualContent_thenTrue() {
        String content = "data";
        Mockito.when(message.text()).thenReturn(content);

        assertThat(MessagesApprovalUtils.equalsTextMessageContent(message, content)).isTrue();
    }

    @Test
    void givenNonExistingMsgWithNoData_thenFalse() {
        Message messageFake = Mockito.mock(Message.class);

        assertThat(MessagesApprovalUtils.equalsTextMessageContent(messageFake, "")).isFalse();
    }

    @Test
    void givenExistingMsgWithNotEqualText_thenFalse() {
        String content = "data";
        Mockito.when(message.text()).thenReturn(content + content);

        assertThat(MessagesApprovalUtils.equalsTextMessageContent(message, content)).isFalse();
    }
}
