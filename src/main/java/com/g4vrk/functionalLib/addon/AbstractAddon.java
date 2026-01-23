package com.g4vrk.functionalLib.addon;

import com.g4vrk.functionalLib.configuration.Configuration;
import com.g4vrk.functionalLib.configuration.yaml.YamlConfig;
import com.g4vrk.functionalLib.logging.PluginLogger;
import com.g4vrk.functionalLib.logging.PluginLoggerFactory;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class AbstractAddon {
    private boolean enabled = false;

    private JavaPlugin parent;
    private File dataFolder;
    protected AddonDescription description;
    protected PluginLogger logger;

    private final Map<String, Configuration> configs = new ConcurrentHashMap<>();

    public AbstractAddon() {}

    final void init(JavaPlugin parent, File dataFolder, AddonDescription description) {
        this.parent = parent;
        this.dataFolder = dataFolder;
        this.description = description;
        this.logger = PluginLoggerFactory.getLogger(description.getName());
    }

    protected void onEnable() {}

    protected void onDisable() {}

    public final void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;

        this.enabled = enabled;

        if (enabled) {
            logger.info("Включение аддона " + description.getName() + " v" + description.getVersion());
            onEnable();
        } else {
            logger.info("Выключение аддона {}", description.getName());
            onDisable();
        }
    }

    public final JavaPlugin getParent() {
        return parent;
    }

    public Configuration getResource(String fileName) {
        return configs.computeIfAbsent(fileName, name -> {
            try {
                File file = new File(dataFolder, name);
                if (!file.exists()) {
                    file.createNewFile();
                }
                return new YamlConfig(file.getName(), parent);
            } catch (Throwable e) {
                LogUtil.throwable(e, "Не удалось загрузить файл " + fileName + " для аддона " + getName());
                return null;
            }
        });
    }

    public final File getDataFolder() {
        return dataFolder;
    }

    public final Logger getLogger() {
        return logger;
    }

    public final AddonDescription getDescription() {
        return description;
    }

    public final String getName() {
        return description.getName();
    }

    public final String getVersion() {
        return description.getVersion();
    }
}
