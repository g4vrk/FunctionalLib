package com.g4vrk.functionalLib.menu.types;

import com.g4vrk.functionalLib.actions.ActionExecutor;
import com.g4vrk.functionalLib.configuration.Configuration;
import com.g4vrk.functionalLib.menu.Menu;
import org.bukkit.entity.Player;

public class DefaultMenu extends Menu {

    protected final Configuration configuration;
    protected final ActionExecutor<Player> actionExecutor;

    public DefaultMenu(Configuration configuration, ActionExecutor<Player> actionExecutor) {
        this.configuration = configuration;
        this.actionExecutor = actionExecutor;
    }

    @Override
    protected int getSize() {
        return configuration.getInt("size");
    }

    @Override
    protected String getTitle() {
        return configuration.getString("title");
    }

    @Override
    protected void loadItems() {
        if (!configuration.contains("items")) return;

        for (String key : configuration.getConfigurationSection("items").getKeys(false)) {
            addItem(new DefaultMenuItem(this, key, configuration, actionExecutor));
        }
    }
}