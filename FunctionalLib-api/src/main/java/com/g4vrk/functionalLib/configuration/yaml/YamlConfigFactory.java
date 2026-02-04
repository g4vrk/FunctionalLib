package com.g4vrk.functionalLib.configuration.yaml;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@UtilityClass
public class YamlConfigFactory {

    public static YamlConfig createConfiguration(String fileName, JavaPlugin plugin) throws IOException {
        File file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            if (plugin.getResource(fileName) != null) {
                plugin.saveResource(fileName, false);
            }
        }

        return new YamlConfig(file, plugin.getResource(fileName));
    }

    public static YamlConfig createConfiguration(File file, JavaPlugin plugin) throws IOException {
        String fileName = file.getName();

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            if (plugin.getResource(fileName) != null) {
                plugin.saveResource(fileName, false);
            }
        }

        return new YamlConfig(file, plugin.getResource(fileName));
    }
}
