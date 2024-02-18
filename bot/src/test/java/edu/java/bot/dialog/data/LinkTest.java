package edu.java.bot.dialog.data;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LinkTest {
    @Test
    void givenCorrectUrl_thenConstructLink() {
        String url = "https://github.com";

        assertThat(Link.constructLink(url)).isNotNull();
    }

    @Test
    void givenTwoEqualsLinks_thenCompareTrue() {
        String sameUrl = "https://github.com";
        Link link = Link.constructLink(sameUrl);
        Link linkAgain = Link.constructLink(sameUrl);

        assertThat(link.equals(linkAgain)).isTrue();
    }

    @Test
    void givenTwoNotEqualsLinks_thenCompareFalse() {
        String sameUrl = "https://github.com";
        Link link = Link.constructLink(sameUrl);
        Link linkAgain = Link.constructLink(sameUrl + sameUrl);

        assertThat(link.equals(linkAgain)).isFalse();
    }
}
