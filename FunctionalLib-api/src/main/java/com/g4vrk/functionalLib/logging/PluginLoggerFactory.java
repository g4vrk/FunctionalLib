package com.g4vrk.functionalLib.logging;

import com.g4vrk.functionalLib.logging.impl.BukkitLogger;
import com.g4vrk.functionalLib.logging.impl.PaperLogger;
import com.g4vrk.functionalLib.util.MinecraftVersion;
import com.g4vrk.functionalLib.util.text.TextUtil;
import lombok.experimental.UtilityClass;
import org.bukkit.plugin.java.JavaPlugin;

@UtilityClass
public class PluginLoggerFactory {
    public PluginLogger getLogger(String name, boolean debug) {
        if (MinecraftVersion.hasComponentLogger()) {
            return new PaperLogger(name, debug);
        }

        return new BukkitLogger(name, debug);
    }

    public PluginLogger getLogger(String name) {
        return getLogger(name, false);
    }

    public PluginLogger getLogger(Class<?> clazz) {
        return getLogger(clazz.getSimpleName());
    }

    public PluginLogger getLogger(JavaPlugin plugin) {
        return getLogger(plugin.getName());
    }
}
