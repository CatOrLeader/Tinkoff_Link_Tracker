package edu.java.bot.dialog.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "app.on-startup.skip-updates=false")
@ExtendWith(MockitoExtension.class)
public class HandlerStorageTest {
    @Autowired
    private HandlerStorage storage;

    @Test
    void givenHandlers_thenAllHandlersAreInjectedInStorage() {
        assertThat(storage.unresolvedMsgHandler).isNotNull();
        assertThat(storage.independentHandlers).isNotEmpty();
        assertThat(storage.stateHandlers).isNotEmpty();
    }

}
