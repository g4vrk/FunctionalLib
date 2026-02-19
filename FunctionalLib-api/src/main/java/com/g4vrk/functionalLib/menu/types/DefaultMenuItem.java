package com.g4vrk.functionalLib.menu.types;

import com.g4vrk.functionalLib.actions.ActionExecutor;
import com.g4vrk.functionalLib.configuration.Configuration;
import com.g4vrk.functionalLib.menu.Menu;
import com.g4vrk.functionalLib.menu.MenuItem;
import com.g4vrk.functionalLib.util.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DefaultMenuItem extends MenuItem {

    private final Configuration configuration;
    private final ActionExecutor<Player> actionExecutor;

    public DefaultMenuItem(Menu menu, String key, Configuration configuration, ActionExecutor<Player> actionExecutor) {
        super(menu, key);
        this.configuration = configuration;
        this.actionExecutor = actionExecutor;
    }

    @Override
    public ItemStack getItemStack(Object... placeholders) {
        return ItemBuilder.fromConfiguration(configuration.getConfigurationSection("items." + key))
                .placeholders(placeholders)
                .itemStack();
    }

    @Override
    public void handleClick(Player player, InventoryClickEvent event) {
        event.setCancelled(true);
        player.setItemOnCursor(null);
        String actionPath = switch (event.getClick()) {
            case LEFT, SHIFT_LEFT -> "items." + key + ".left_click_actions";
            case RIGHT, SHIFT_RIGHT -> "items." + key + ".right_click_actions";
            default -> null;
        };

        if (actionPath != null && configuration.contains(actionPath)) {
            actionExecutor.runActions(player, configuration.getStringList(actionPath));
        }
    }

    @Override
    public int[] getSlots() {
        return configuration.getIntegerList("items." + key + ".slots")
                .stream().mapToInt(Integer::intValue).toArray();
    }
}