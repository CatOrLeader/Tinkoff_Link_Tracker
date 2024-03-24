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
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@RequiredArgsConstructor
public class JooqDatabaseAccessConfiguration {
    private final DataSource dataSource;

    @Bean
    public DataSourceConnectionProvider dataSourceConnectionProvider() {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    @Bean
    public DSLContext dsl(DataSourceConnectionProvider dataSourceConnectionProvider) {
        return new DefaultDSLContext(buildConfiguration(dataSourceConnectionProvider));
    }

    public org.jooq.Configuration buildConfiguration(DataSourceConnectionProvider connectionProvider) {
        return new DefaultConfiguration()
            .set(connectionProvider)
            .set(SQLDialect.POSTGRES)
            .set(new Settings()
                .withRenderQuotedNames(RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED)
                .withRenderSchema(false)
                .withRenderFormatted(true)
            )
            .set(new DefaultExecuteListenerProvider(new JooqExceptionTranslator()));
    }

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
