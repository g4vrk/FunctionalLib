package com.g4vrk.functionalLib.configuration.yaml;

import com.g4vrk.functionalLib.FunctionalLibPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class YamlConfigManager {

    private final JavaPlugin plugin;
    private final Map<String, YamlConfig> yamlConfigMap = new ConcurrentHashMap<>();

    public YamlConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public @Nullable YamlConfig createDefaultConfig() {
        return createConfig("config.yml");
    }

    public @Nullable YamlConfig createConfig(String fileName) {
        try {
            YamlConfig yamlConfig = YamlConfigFactory.createConfiguration(fileName, plugin);
            this.yamlConfigMap.put(fileName, yamlConfig);
            return yamlConfig;
        } catch (IOException e) {
            FunctionalLibPlugin.getInstance().getSLF4JLogger().error("Не удалось создать конфигурацию {}", fileName, e);
            return null;
        }
    }

    public void reloadAll() {
        yamlConfigMap.forEach((name, yamlConfig) -> yamlConfig.reload());
    }
}

