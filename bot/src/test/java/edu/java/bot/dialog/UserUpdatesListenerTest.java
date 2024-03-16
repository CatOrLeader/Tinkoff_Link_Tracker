package edu.java.bot.dialog;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "app.on-startup.skip-updates=false")
@ExtendWith(MockitoExtension.class)
public class UserUpdatesListenerTest {
    @Autowired
    private UserUpdatesListener userUpdatesListener;

    @Test
    void givenAnything_thenReturnedCorrectSignal() {
        List<Update> updateList = List.of(
            Mockito.mock(Update.class)
        );

        assertThat(userUpdatesListener.process(updateList)).isEqualTo(UpdatesListener.CONFIRMED_UPDATES_ALL);
    }
}
