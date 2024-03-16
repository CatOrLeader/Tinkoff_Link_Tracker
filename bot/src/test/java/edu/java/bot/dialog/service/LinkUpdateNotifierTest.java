package edu.java.bot.dialog.service;

import edu.java.bot.dialog.data.BotState;
import edu.java.bot.rest.model.GetChatResponse;
import edu.java.bot.rest.model.LinkUpdateRequest;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest(properties = "app.on-startup.skip-updates=false")
@ExtendWith(MockitoExtension.class)
public class LinkUpdateNotifierTest {

    @Autowired
    private LinkUpdateNotifier notifier;

    @Test
    void givenUpdateRequest_thenCorrectlyNotifyAll() {
        final LinkUpdateRequest request = new LinkUpdateRequest(
            1L,
            URI.create("https://localhost"),
            "Nothing here",
            List.of(new GetChatResponse(15L, BotState.MAIN_MENU.name(), Locale.ENGLISH.toLanguageTag()))
        );

        assertThatNoException().isThrownBy(() -> notifier.notifyAll(request));
    }
}
