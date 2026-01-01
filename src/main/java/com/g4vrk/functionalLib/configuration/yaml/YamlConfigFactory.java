package com.g4vrk.functionalLib.configuration.yaml;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@UtilityClass
public class YamlConfigFactory {

    public static YamlConfig createConfiguration(String fileName, JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            if (plugin.getResource(fileName) != null) {
                plugin.saveResource(fileName, false);
            }
        }

        try {
            return new YamlConfig(file, plugin.getResource(fileName));
        } catch (Throwable t) {
            throw new RuntimeException("Произошла ошибка при инициализации конфигурации: " + fileName, t);
        }
    }

    public static YamlConfig createConfiguration(File file, JavaPlugin plugin) {
        String fileName = file.getName();

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            if (plugin.getResource(fileName) != null) {
                plugin.saveResource(fileName, false);
            }
        }

        try {
            return new YamlConfig(file, plugin.getResource(fileName));
        } catch (Throwable t) {
            throw new RuntimeException("Произошла ошибка при инициализации конфигурации: " + fileName, t);
        }
    }
}
