package com.g4vrk.functionalLib.menu.types;

import com.g4vrk.functionalLib.configuration.Configuration;
import com.g4vrk.functionalLib.menu.Menu;

public class DefaultMenu extends Menu {

    private final Configuration configuration;

    public DefaultMenu(Configuration configuration) {
        this.configuration = configuration;
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
            addItem(new DefaultMenuItem(this, key, configuration));
        }
    }
}