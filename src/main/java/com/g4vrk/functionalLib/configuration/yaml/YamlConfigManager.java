package com.g4vrk.functionalLib.configuration.yaml;

import com.g4vrk.functionalLib.FunctionalLibPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class YamlConfigManager {

    private final JavaPlugin plugin;
    private final Map<String, YamlConfig> yamlConfigMap = new ConcurrentHashMap<>();

    public YamlConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private @Nullable YamlConfig createConfig(String fileName) {
        try {
            return YamlConfigFactory.createConfiguration(fileName, plugin);
        } catch (IOException e) {
            FunctionalLibPlugin.logger().error("Не удалось создать конфигурацию {}", fileName, e);
            return null;
        }
    }

    public @Nullable YamlConfig getConfig(String fileName) {
        return yamlConfigMap.computeIfAbsent(fileName, this::createConfig);
    }

    public @Nullable YamlConfig getConfig(File file) {
        return yamlConfigMap.computeIfAbsent(file.getName(), this::createConfig);
    }

    public void reload(String fileName) {
        YamlConfig config = yamlConfigMap.get(fileName);
        if (config != null) config.reload();
    }

    public void reloadAll() {
        yamlConfigMap.forEach((name, yamlConfig) -> yamlConfig.reload());
    }
}

