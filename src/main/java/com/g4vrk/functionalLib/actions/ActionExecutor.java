package com.g4vrk.functionalLib.actions;

import com.g4vrk.functionalLib.FunctionalLibPlugin;
import com.g4vrk.functionalLib.actions.impl.*;
import com.g4vrk.functionalLib.configuration.Configuration;
import com.g4vrk.functionalLib.util.TaskUtil;
import com.g4vrk.functionalLib.util.text.ReplaceUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Arrays;
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

    public void runActions(String path, Player player, Configuration configuration, @Nullable Object[] placeholders) {
        List<String> rawList = PlaceholderAPI.setPlaceholders(player, configuration.getStringList(path)) ;

        if (placeholders != null) {
            rawList = ReplaceUtil.formatStringList(rawList, placeholders);
        }

        executeActions(path, player, rawList);
    }

    public void runActions(String path, Player player, Configuration configuration) {
        List<String> rawList = PlaceholderAPI.setPlaceholders(player, configuration.getStringList(path)) ;

        executeActions(path, player, rawList);
    }

    public void runAction(String actionLine, Player player, @Nullable Object... placeholders) {
        String line = actionLine;

        line = PlaceholderAPI.setPlaceholders(player, line);

        if (placeholders != null) {
            line = ReplaceUtil.format(line, placeholders);
        }

        executeActions("direct", player, List.of(line));
    }

    private void executeActions(String path, Player player, List<String> rawList) {
        for (String array : rawList) {
            String[] parts = array.split(" ", 2);

            String actionKey = parts[0].toLowerCase();
            String args = parts.length > 1 ? parts[1] : "";

            Action action = actionMap.get(actionKey);
            if (action == null) {
                FunctionalLibPlugin.getInstance().getSLF4JLogger().error("Не найдено действие: {} (путь: {})", actionKey, path);
                continue;
            }

            Runnable actionTask = () -> action.execute(new ActionContext(player), args);

            if (action.runAsync()) {
                TaskUtil.runAsync(actionTask);
            } else {
                TaskUtil.runSync(actionTask);
            }
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
