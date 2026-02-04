package com.g4vrk.functionalLib.actions;

import com.g4vrk.functionalLib.FunctionalLibAPI;
import com.g4vrk.functionalLib.actions.impl.*;
import com.g4vrk.functionalLib.configuration.Configuration;
import com.g4vrk.functionalLib.util.TaskUtil;
import com.g4vrk.functionalLib.util.text.ReplaceUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ActionExecutor {

    private final Map<String, Action> actionMap = new ConcurrentHashMap<>();

    // default actions, provided by plugin
    {
        registerAction(new ActionBarAction());
        registerAction(new CloseMenuAction());
        registerAction(new ConsoleAction());
        registerAction(new MessageAction());
        registerAction(new SoundAction());
        registerAction(new TitleAction());
        registerAction(new UpdateInventoryAction());
    }

    public ActionExecutor() {
    }

    @Deprecated(forRemoval = true)
    public void runActions(String path, Player player, Configuration configuration, @Nullable Object[] placeholders) {
        List<String> rawList = PlaceholderAPI.setPlaceholders(player, configuration.getStringList(path)) ;

        if (placeholders != null) {
            rawList = ReplaceUtil.formatStringList(rawList, placeholders);
        }

        executeActions(player, rawList);
    }

    @Deprecated(forRemoval = true)
    public void runActions(String path, Player player, Configuration configuration) {
        List<String> rawList = PlaceholderAPI.setPlaceholders(player, configuration.getStringList(path)) ;

        executeActions(player, rawList);
    }

    public void runActions(Player player, List<String> actionList, @Nullable Object[] placeholders) {
        List<String> list = PlaceholderAPI.setPlaceholders(player, actionList);

        if (placeholders != null && placeholders.length != 0) {
            list = ReplaceUtil.formatStringList(list, placeholders);
        }

        executeActions(player, list);
    }

    public void runActions(Player player, List<String> actionList) {
        List<String> list = PlaceholderAPI.setPlaceholders(player, actionList);

        executeActions(player, list);
    }

    public void runAction(String actionLine, Player player, @Nullable Object... placeholders) {
        String line = actionLine;

        line = PlaceholderAPI.setPlaceholders(player, line);

        if (placeholders != null) {
            line = ReplaceUtil.format(line, placeholders);
        }

        executeActions(player, List.of(line));
    }

    private void executeActions(Player player, List<String> actionList) {
        for (String actionStr : actionList) {
            String[] parts = actionStr.split(" ", 2);

            String actionKey = parts[0].toLowerCase();
            String args = parts.length > 1 ? parts[1] : "";

            Action action = actionMap.get(actionKey);
            if (action == null) {
                FunctionalLibAPI.getAPI().ifPresent(functionalLibAPI -> {
                  functionalLibAPI.getLogger().error("Неизвестное действие: {}", actionKey);
                });
                continue;
            }

            Runnable actionTask = () -> action.execute(new ActionContext(player), args);

            if (action.runAsync())
                TaskUtil.runAsync(actionTask);
            else
                TaskUtil.runSync(actionTask);
        }
    }

    public void registerAction(Action action) {
        if (actionMap.containsKey(action.getActionKey().toLowerCase())) {
            throw new IllegalArgumentException(
                    "Действие " + action.getActionKey().toLowerCase() + " уже зарегистрировано классом " + actionMap.get(action.getActionKey()).getClass().getSimpleName()
            );
        }
        actionMap.put("[" + action.getActionKey().toLowerCase() + "]", action);
    }
}
