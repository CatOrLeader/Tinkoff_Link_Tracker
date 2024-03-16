package edu.java.bot.dialog.data;

import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

@Repository
public class UserDataStorage {
    private final Map<Long, UserData> users;

    public UserDataStorage() {
        users = new Hashtable<>();
    }

    public void addUser(@NotNull UserData userData) {
        users.putIfAbsent(userData.getUserID(), userData);
    }

    public @NotNull Optional<UserData> getUserById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public void setUserState(@NotNull UserData userData, @NotNull BotState state) {
        users.get(userData.getUserID()).setDialogState(state);
    }
}
