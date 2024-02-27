package edu.java.bot.dialog.data;

import java.time.Instant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserLinksTrackerTest {
    private static final long USER_ID = 1L;
    private static final String LINK_URL = "https://github.com";
    private static Link link;
    private static UserLinksTracker linksTracker;

    @BeforeAll
    static void init() {
        linksTracker = new UserLinksTracker();
        link = new Link(LINK_URL, Instant.now());
    }

    @Test
    @Order(1)
    void givenLink_thenAddedCorrectly() {
        linksTracker.addUserLink(USER_ID, link);

        assertThat(linksTracker.getUserLinks(USER_ID)).containsExactly(link);
    }

    @Test
    @Order(2)
    void givenSameLink_thenOnlyOneLinkStored() {
        linksTracker.addUserLink(USER_ID, link);

        assertThat(linksTracker.getUserLinks(USER_ID)).containsExactly(link);
    }

    @Test
    @Order(3)
    void givenLinkToDelete_thenDeletedCorrectly() {
        linksTracker.removeUserLinkByUrl(USER_ID, link.url());

        assertThat(linksTracker.getUserLinks(USER_ID)).isEmpty();
    }
}
