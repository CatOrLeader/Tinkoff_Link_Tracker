package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.TgChatRepository;
import edu.java.scrapper.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.domain.repository.jdbc.JdbcTgChatRepository;
import edu.java.scrapper.domain.repository.jdbc.mappers.LinkRowMapper;
import edu.java.scrapper.domain.repository.jdbc.mappers.TgChatRowMapper;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.domain.service.TgChatService;
import edu.java.scrapper.domain.service.jdbc.JdbcLinkService;
import edu.java.scrapper.domain.service.jdbc.JdbcTgChatService;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcDatabaseAccessConfiguration {
    @Bean
    public LinkRepository linkRepository(
        @NotNull TransactionTemplate transactionTemplate,
        @NotNull JdbcClient jdbcClient,
        @NotNull LinkRowMapper mapper
    ) {
        return new JdbcLinkRepository(transactionTemplate, jdbcClient, mapper);
    }

    @Bean
    public TgChatRepository tgChatRepository(
        @NotNull TransactionTemplate transactionTemplate,
        @NotNull JdbcClient jdbcClient,
        @NotNull TgChatRowMapper mapper
    ) {
        return new JdbcTgChatRepository(jdbcClient, transactionTemplate, mapper);
    }

    @Bean
    public LinkService linkService(@NotNull LinkRepository repository) {
        return new JdbcLinkService(repository);
    }

    @Bean
    public TgChatService tgChatService(@NotNull TgChatRepository repository) {
        return new JdbcTgChatService(repository);
    }
}
