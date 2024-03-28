package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.TgChatRepository;
import edu.java.scrapper.domain.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.repository.jpa.JpaLinkRepositoryInterface;
import edu.java.scrapper.domain.repository.jpa.JpaTgChatRepository;
import edu.java.scrapper.domain.repository.jpa.JpaTgChatRepositoryInterface;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.domain.service.TgChatService;
import edu.java.scrapper.domain.service.jpa.JpaLinkService;
import edu.java.scrapper.domain.service.jpa.JpaTgChatService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "JPA")
public class JpaDatabaseAccessConfiguration {
    @Bean
    public LinkRepository linkRepository(
        @NotNull JpaLinkRepositoryInterface linkRepositoryInterface,
        @NotNull JpaTgChatRepositoryInterface tgChatRepository
    ) {
        return new JpaLinkRepository(linkRepositoryInterface, tgChatRepository);
    }

    @Bean
    public TgChatRepository tgChatRepository(
        @NotNull JpaTgChatRepositoryInterface tgChatRepositoryInterface
    ) {
        return new JpaTgChatRepository(tgChatRepositoryInterface);
    }

    @Bean
    public LinkService linkService(@NotNull LinkRepository repository) {
        return new JpaLinkService(repository);
    }

    @Bean
    public TgChatService tgChatService(@NotNull TgChatRepository repository) {
        return new JpaTgChatService(repository);
    }
}
