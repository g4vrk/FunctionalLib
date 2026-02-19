package com.g4vrk.functionalLib.actions;

import com.g4vrk.functionalLib.FunctionalLibAPI;
import com.g4vrk.functionalLib.actions.registry.ActionRegistry;
import com.g4vrk.functionalLib.util.TaskUtil;

import java.util.List;

public final class ActionExecutor<T> {

    private final ActionRegistry<T> registry;

    public ActionExecutor(ActionRegistry<T> registry) {
        this.registry = registry;
    }

    public void runActions(T context, List<String> actionList) {
        for (String action : actionList) {
            executeSingle(context, action);
        }
    }

    public void runAction(String actionLine, T context) {
        executeSingle(context, actionLine);
    }

    private void executeSingle(T context, String actionStr) {

        if (actionStr == null || actionStr.isBlank())
            return;

        actionStr = actionStr.trim();

        String[] parts = splitAction(actionStr);

        String rawKey = parts[0];
        String key = normalizeKey(rawKey);
        String args = parts[1];

        var optionalAction = registry.getAction(key);

        if (optionalAction.isEmpty()) {
            FunctionalLibAPI.getAPI().ifPresent(api ->
                    api.getLogger().error("Неизвестное действие: {}", rawKey)
            );
            return;
        }

        Action<T> action = optionalAction.get();

        Runnable task = () -> action.execute(context, args);

        if (action.runAsync())
            TaskUtil.runAsync(task);
        else
            TaskUtil.runSync(task);
    }

    private String[] splitAction(String input) {

        int colonIndex = input.indexOf(':');
        int spaceIndex = input.indexOf(' ');

        if (colonIndex == -1 && spaceIndex == -1) {
            return new String[]{input, ""};
        }

        int splitIndex;

        if (colonIndex == -1) splitIndex = spaceIndex;
        else if (spaceIndex == -1) splitIndex = colonIndex;
        else splitIndex = Math.min(colonIndex, spaceIndex);

        String key = input.substring(0, splitIndex).trim();
        String args = input.substring(splitIndex + 1).trim();

        return new String[]{key, args};
    }

    private String normalizeKey(String input) {
        String key = input.toLowerCase().trim();

        if (key.endsWith(":")) {
            key = key.substring(0, key.length() - 1);
        }

        if ((key.startsWith("[") && key.endsWith("]")) ||
                (key.startsWith("(") && key.endsWith(")"))) {
            key = key.substring(1, key.length() - 1);
        }

        return key;
    }
}
