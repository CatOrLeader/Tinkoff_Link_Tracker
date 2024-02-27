package edu.java.bot.utils;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ExtendWith(MockitoExtension.class)
public class UserInfoUtilsTest {
    @Mock
    private static Update update;
    @Mock
    private static Message message;
    @Mock
    private static CallbackQuery query;
    @Mock
    private static User user;

    @Test
    void getCorrectUpdateWithNotNullMsg() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.from()).thenReturn(user);
        Mockito.when(user.id()).thenReturn(123L);

        Optional<Long> actualId = UserInfoUtils.extractUserId(update);

        assertThat(actualId).contains(123L);
        Mockito.reset(message);
    }

    @Test
    void getCorrectUpdateWithNotNullCallbackQuery() {
        Mockito.when(update.callbackQuery()).thenReturn(query);
        Mockito.when(query.from()).thenReturn(user);
        Mockito.when(user.id()).thenReturn(123L);

        Optional<Long> actualId = UserInfoUtils.extractUserId(update);

        assertThat(actualId).contains(123L);
        Mockito.reset(query);
    }

    @Test
    void getCorrectUpdateButNoDataInto() {
        Update updateEmpty = Mockito.mock(Update.class);

        assertThat(UserInfoUtils.extractUserId(updateEmpty)).isEmpty();
    }

    @Test
    void getCorrectUpdate_whenSomethingWentWrongInside_thenSystemIsStable() {
        Update updateEmpty = Mockito.mock(Update.class);
        Message messageIncorrect = Mockito.mock(Message.class);
        Mockito.when(updateEmpty.message()).thenReturn(messageIncorrect);

        assertThatNoException().isThrownBy(() -> UserInfoUtils.extractUserId(updateEmpty));
    }
}
