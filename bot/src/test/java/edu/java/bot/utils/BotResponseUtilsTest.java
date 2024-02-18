package edu.java.bot.utils;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
public class BotResponseUtilsTest {
    @Test
    void givenCorrectQueryData_thenReceiveLink() {
        String queryContent = "cancel https://github.com";
        CallbackQuery query = Mockito.mock(CallbackQuery.class);
        Mockito.when(query.data()).thenReturn(queryContent);

        String expectedValue = "https://github.com";
        String actualValue = BotResponsesUtils.extractLinkFromCallbackQuery(query);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void givenIncorrectQueryData_thenThrow() {
        String queryContent = "canc";
        CallbackQuery query = Mockito.mock(CallbackQuery.class);
        Mockito.when(query.data()).thenReturn(queryContent);

        assertThatExceptionOfType(IndexOutOfBoundsException.class)
            .isThrownBy(() -> BotResponsesUtils.extractLinkFromCallbackQuery(query));
    }

    @Test
    void givenLongLink_thenCorrectDecoration() {
        final String link = "https://unsatisfactory_long_web_adress/?why=it_is_even#exists";
        final int maxSize = 10;

        String expectedValue = link.substring(0, maxSize) + "...";
        String actualValue = BotResponsesUtils.decorateLink(link, maxSize);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void givenShortLink_thenCorrectDecoration() {
        final String link = "https://lol";
        final int maxSize = 32;

        String actualValue = BotResponsesUtils.decorateLink(link, maxSize);

        assertThat(actualValue).isEqualTo(link);
    }

    @Test
    void givenLongLinkWithIncorrectMaxSize_thenCorrectDecoration() {
        final String link = "https://unsatisfactory_long_web_adress/?why=it_is_even#exists";
        final int maxSize = -5;

        assertThatExceptionOfType(IndexOutOfBoundsException.class)
            .isThrownBy(() -> BotResponsesUtils.decorateLink(link, maxSize));
    }

    @Test
    void givenSingleArray_thenReceiveItBack() {
        BaseRequest[] responses = new BaseRequest[] {new SendMessage(null, null)};

        assertThat(BotResponsesUtils.concatenate(responses)).containsAll(Arrays.asList(responses));
    }

    @Test
    void givenMultipleArrays_thenReceiveItBack() {
        BaseRequest[] responsesOne = new BaseRequest[] {new SendMessage(null, null)};
        BaseRequest[] responsesTwo = new BaseRequest[] {new SendMessage(null, null),
            new SendMessage(null, null),
            new SendMessage(null, null)};

        var actualResponses = BotResponsesUtils.concatenate(responsesOne, responsesTwo);

        assertThat(actualResponses).containsAll(Arrays.asList(responsesOne));
        assertThat(actualResponses).containsAll(Arrays.asList(responsesTwo));
        assertThat(actualResponses.length).isEqualTo(responsesOne.length + responsesTwo.length);
    }
}
