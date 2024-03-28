package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.TgChatRepository;
import edu.java.scrapper.domain.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.domain.repository.jooq.JooqTgChatRepository;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.domain.service.TgChatService;
import edu.java.scrapper.domain.service.jooq.JooqLinkService;
import edu.java.scrapper.domain.service.jooq.JooqTgChatService;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqDatabaseAccessConfiguration {
    @Bean
    public LinkRepository linkRepository(
        @NotNull DSLContext dslContext
    ) {
        return new JooqLinkRepository(dslContext);
    }

    @Bean
    public TgChatRepository tgChatRepository(
        @NotNull DSLContext dslContext
    ) {
        return new JooqTgChatRepository(dslContext);
    }

    @Bean
    public LinkService linkService(@NotNull LinkRepository repository) {
        return new JooqLinkService(repository);
    }

    @Bean
    public TgChatService tgChatService(@NotNull TgChatRepository repository) {
        return new JooqTgChatService(repository);
    }
}
