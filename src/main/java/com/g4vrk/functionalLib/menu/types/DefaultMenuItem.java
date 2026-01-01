package com.g4vrk.functionalLib.menu.types;

import com.g4vrk.functionalLib.actions.ActionExecutor;
import com.g4vrk.functionalLib.configuration.Configuration;
import com.g4vrk.functionalLib.menu.Menu;
import com.g4vrk.functionalLib.menu.MenuItem;
import com.g4vrk.functionalLib.util.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DefaultMenuItem extends MenuItem {

    private final Configuration configuration;

    public DefaultMenuItem(Menu menu, String key, Configuration configuration) {
        super(menu, key);
        this.configuration = configuration;
    }

    @Override
    public ItemStack getItemStack(Object... placeholders) {
//        String materialName = configuration.getString("items." + key + ".material", "STONE");
//        Material material = Material.matchMaterial(materialName);
//        if (material == null) material = Material.STONE;
//        return new ItemStack(material);
        return ItemCreator.fromConfiguration(configuration.getConfigurationSection("items." + key)).toItemStack();
    }

    @Override
    public void handleClick(Player player, InventoryClickEvent event) {
        String actionPath = switch (event.getClick()) {
            case LEFT, SHIFT_LEFT -> "items." + key + ".left_click_actions";
            case RIGHT, SHIFT_RIGHT -> "items." + key + ".right_click_actions";
            default -> null;
        };

        if (actionPath != null && configuration.contains(actionPath)) {
            ActionExecutor.runActions(actionPath, player, configuration);
        }
    }

    @Override
    public int[] getSlots() {
        return configuration.getIntegerList("items." + key + ".slots")
                .stream().mapToInt(Integer::intValue).toArray();
    }
}