package edu.java.bot.dialog;

import edu.java.bot.dialog.data.Link;
import edu.java.bot.rest.service.LinksService;
import edu.java.bot.rest.service.TgChatService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.net.URI;
import java.util.List;

@Profile("test")
@Configuration
public class ScrapperMockTestConfiguration {
    @Bean
    @Primary
    public LinksService linksService() {
        return Mockito.mock(LinksService.class);
    }

    @Bean
    @Primary
    public TgChatService tgChatService() {
        return Mockito.mock(TgChatService.class);
    }
}
