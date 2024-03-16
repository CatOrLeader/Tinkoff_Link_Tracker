package edu.java.bot.dialog.data;

import java.util.Locale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDataStorageTest {
    private static final long USER_ID = 1L;
    private static UserDataStorage userDataStorage;
    private static UserData userData;

    @BeforeAll
    static void init() {
        userDataStorage = new UserDataStorage();
        userData = new UserData(USER_ID, BotState.UNINITIALIZED, Locale.ENGLISH);
    }

    @Test
    @Order(1)
    void givenEmptyStorage_thenNoUserReturned() {
        assertThat(userDataStorage.getUserById(USER_ID)).isEmpty();
    }

    @Test
    @Order(2)
    void givenUser_thenAddedCorrectly() {
        userDataStorage.addUser(userData);

        assertThat(userDataStorage.getUserById(USER_ID)).contains(userData);
    }

    @Test
    @Order(3)
    void givenUserInStorage_whenSetNewState_thenUserStateChanged() {
        var newState = BotState.MAIN_MENU;

        userDataStorage.setUserState(userData, newState);

        assertThat(userData.getDialogState()).isEqualTo(newState);
    }
}
