package edu.java.bot.dialog.handlers;

import edu.java.bot.dialog.data.BotState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HandlerStorage {
    protected final UpdateHandler unresolvedMsgHandler;
    protected final Map<BotState, UpdateHandler> stateHandlers;
    protected final List<UpdateHandler> independentHandlers;

    @Autowired
    public HandlerStorage(
        @NotNull Map<String, UpdateHandler> handlerMap
    ) {
        unresolvedMsgHandler = handlerMap.get("unknownMessageHandler");
        stateHandlers = preloadStateHandlers(handlerMap);
        independentHandlers = preloadIndependentHandlers(handlerMap);
    }

    private Map<BotState, UpdateHandler> preloadStateHandlers(Map<String, UpdateHandler> handlerMap) {
        Map<BotState, UpdateHandler> handlers = new HashMap<>();

        handlers.put(BotState.UNINITIALIZED, handlerMap.get("uninitializedHandler"));
        handlers.put(BotState.MAIN_MENU, handlerMap.get("mainMenuHandler"));
        handlers.put(BotState.RES_TRACK_WAITING, handlerMap.get("resToTrackReceivedHandler"));
        handlers.put(BotState.RES_UNTRACK_WAITING, handlerMap.get("resToUntrackReceivedHandler"));

        return handlers;
    }

    private List<UpdateHandler> preloadIndependentHandlers(Map<String, UpdateHandler> handlerMap) {
        List<UpdateHandler> handlers = new ArrayList<>();

        handlers.add(handlerMap.get("helpHandler"));
        handlers.add(handlerMap.get("listHandler"));
        handlers.add(handlerMap.get("menuHandler"));
        handlers.add(handlerMap.get("startHandler"));
        handlers.add(handlerMap.get("trackHandler"));
        handlers.add(handlerMap.get("untrackHandler"));

        return handlers;
    }
}
