package edu.java.bot.dialog.data;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserDataTest {
    @Test
    void givenUserID_thenConstructedUserData() {
        assertThat(UserData.constructInitialFromId(1L)).isNotNull();
    }

    @Test
    void givenRegisteredUser_thenTrue() {
        UserData userData = UserData.constructInitialFromId(1L);
        userData.setDialogState(BotState.MAIN_MENU);

        assertThat(userData.isRegistered()).isTrue();
    }
}
