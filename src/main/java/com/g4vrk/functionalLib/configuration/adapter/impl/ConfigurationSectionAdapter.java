package com.g4vrk.functionalLib.configuration.adapter.impl;

import com.g4vrk.functionalLib.configuration.adapter.Adapter;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurationSectionAdapter implements Adapter<ConfigurationSection> {
    @Override
    public Class<ConfigurationSection> getType() {
        return ConfigurationSection.class;
    }

    @Override
    public ConfigurationSection adapt(Object value) {
        if (value instanceof ConfigurationSection configurationSection) {
            return configurationSection;
        }

        throw new IllegalStateException("Value is not ConfigurationSection: " + value);
    }
}
