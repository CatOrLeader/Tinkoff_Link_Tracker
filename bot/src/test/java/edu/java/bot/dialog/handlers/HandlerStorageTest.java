package edu.java.bot.dialog.handlers;

import javax.cache.CacheManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
    "app.on-startup.skip-updates=false",
    "bucket4j.enabled=false",
    "app.kafka.enabled=false",
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"
})
@MockBean(CacheManager.class)
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
