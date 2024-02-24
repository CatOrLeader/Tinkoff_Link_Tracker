package edu.java.bot.dialog.data;

import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public final class UserBotMessageHistory {
    private static final int MAX_CAPACITY = 10;
    private final HashMap<Long, List<Integer>> botMessagesHistory;

    public UserBotMessageHistory() {
        this.botMessagesHistory = new HashMap<>();
    }

    public void addNewBotMessagesId(long userId, @NotEmpty Integer... ids) {
        if (!botMessagesHistory.containsKey(userId)) {
            botMessagesHistory.put(userId, new ArrayList<>());
        }

        resolveCapacityLimit(botMessagesHistory.get(userId), ids);
    }

    public int retrieveLastBotMessage(long userId) {
        return botMessagesHistory.get(userId).getLast();
    }

    private void resolveCapacityLimit(List<Integer> list, Integer... ids) {
        list.addAll(List.of(ids));
        int diff = list.size() - MAX_CAPACITY;
        while (diff-- > 0) {
            list.removeFirst();
        }
    }
}
