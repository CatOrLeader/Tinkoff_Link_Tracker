package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.ResponseType;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.TgChatRepository;
import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JdbcLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private TgChatRepository tgChatRepository;

    @Test
    @Transactional
    @Rollback(false)
    @Order(1)
    void givenEmptyDB_whenTryingToFetchAny_thenNothing() {
        assertThat(linkRepository.find("https://localhost")).isEmpty();
        assertThat(linkRepository.findById(1)).isEmpty();
        assertThat(linkRepository.findAll()).isEmpty();
        assertThat(linkRepository.findAllByTgChatId("1")).isEmpty();
        assertThat(linkRepository.findAllBefore(Timestamp.from(Instant.now()))).isEmpty();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(2)
    void whenAddLinkAndAddChat_thenCorrectlyAdded() {
        Link link = new Link();
        link.setUri(URI.create("https://localhost"));
        tgChatRepository.register("1");

        Link expectedValue = linkRepository.add("1", link).get();

        assertThat(linkRepository.find(link.getUri().toString())).contains(expectedValue);
        assertThat(linkRepository.findById(expectedValue.getId())).contains(expectedValue);
        assertThat(linkRepository.findAll()).containsExactly(expectedValue);
        assertThat(linkRepository.findAllByTgChatId("1")).containsExactly(expectedValue);
        assertThat(linkRepository.findAllBefore(Timestamp.from(Instant.now()))).containsExactly(expectedValue);
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(3)
    void whenAddAgainTheSameLinkWithNoBond_thenEmpty() {
        Link link = new Link();
        link.setUri(URI.create("https://localhost"));

        assertThat(linkRepository.add("1", link)).isEmpty();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(4)
    void whenAddAgainToAnotherTgChat_thenCorrectlyAdded() {
        Link link = new Link();
        link.setUri(URI.create("https://localhost"));
        tgChatRepository.register("2");

        Link expectedValue = linkRepository.add("2", link).get();

        assertThat(linkRepository.find(link.getUri().toString())).contains(expectedValue);
        assertThat(linkRepository.findById(expectedValue.getId())).contains(expectedValue);
        assertThat(linkRepository.findAll()).containsExactly(expectedValue);
        assertThat(linkRepository.findAllByTgChatId("2")).containsExactly(expectedValue);
        assertThat(linkRepository.findAllBefore(Timestamp.from(Instant.now()))).contains(expectedValue);
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(5)
    void whenUpdateNonExistingLink_thenFalse() {
        Link mockLink = new Link();
        mockLink.setUri(URI.create("https://local"));
        mockLink.setDescription("description");
        mockLink.setCreatedAt(OffsetDateTime.now());
        mockLink.setUpdatedAt(null);
        mockLink.setCreatedBy("no one");
        mockLink.setLastCheckedAt(OffsetDateTime.now());
        mockLink.setType(ResponseType.GITHUB_ISSUE);

        boolean isUpdated = linkRepository.update(mockLink);

        assertThat(isUpdated).isFalse();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(6)
    void whenUpdateExistingLink_thenCorrectlyUpdated() {
        Link updatedLink = new Link();
        updatedLink.setUri(URI.create("https://localhost"));
        updatedLink.setDescription("description");
        updatedLink.setCreatedAt(OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.ofHours(3)));
        updatedLink.setUpdatedAt(null);
        updatedLink.setCreatedBy("no one");
        updatedLink.setLastCheckedAt(OffsetDateTime.now());
        updatedLink.setType(ResponseType.GITHUB_ISSUE);

        boolean isUpdated = linkRepository.update(updatedLink);
        var fetchedLink = linkRepository.find(updatedLink.getUri().toString());

        assertThat(isUpdated).isTrue();
        assertThat(fetchedLink).isPresent();
        assertThat(fetchedLink.get().getUri()).isEqualByComparingTo(updatedLink.getUri());
    }

//    @Test
//    @Transactional
//    @Rollback(false)
//    @Order(7)
//    void whenFetchExistingLinkBeforeDateTime_thenCorrectlyFetched() {
//        Link inDBLink = linkRepository.find("https://localhost").get();
//
//        assertThat(linkRepository.findAllBefore(Timestamp.from(Instant.from(inDBLink.getLastCheckedAt())
//            .plusSeconds(1)))).contains(inDBLink);
//        assertThat(linkRepository.findAllBefore(Timestamp.from(Instant.from(inDBLink.getLastCheckedAt())
//            .minusSeconds(1)))).doesNotContain(inDBLink);
//
//    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(8)
    void whenRemoveNonExistingLink_thenEmpty() {
        assertThat(linkRepository.remove("1", 25)).isEmpty();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(9)
    void whenRemoveExistingLink_thenCorrectlyRemoved() {
        Link link = linkRepository.find("https://localhost").get();

        assertThat(linkRepository.remove("1", link.getId())).contains(link);
        assertThat(linkRepository.remove("2", link.getId())).contains(link);

        tgChatRepository.remove("1");
        tgChatRepository.remove("2");
    }
}
