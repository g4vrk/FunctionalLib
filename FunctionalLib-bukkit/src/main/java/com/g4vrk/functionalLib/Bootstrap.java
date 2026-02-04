package com.g4vrk.functionalLib;

import com.g4vrk.functionalLib.logging.PluginLogger;
import com.g4vrk.functionalLib.logging.PluginLoggerFactory;
import com.g4vrk.functionalLib.menu.listener.MenuClickListener;
import com.g4vrk.functionalLib.util.MinecraftVersion;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

final class Bootstrap implements FunctionalLibAPI {

    private final JavaPlugin plugin;
    private final PluginLogger logger;

    private BukkitAudiences audiences;

    Bootstrap(FunctionalLibPlugin plugin) {
        this.plugin = plugin;
        this.logger = PluginLoggerFactory.getLogger(plugin);
    }

    public void onLoad() {
        // pass
    }

    public void onEnable() {
        if (!MinecraftVersion.isPaper())
            throw new IllegalStateException("Для работы плагина используйте Paper и его форки!");

        new MenuClickListener().registerEvents(plugin);

        MinecraftVersion minecraftVersion = MinecraftVersion.current();
        if (MinecraftVersion.below(MinecraftVersion.v1_18_2)) {
            logger.error("Вы используете не поддерживаемую версию! ({}.{}.{})",
                    minecraftVersion.getMajor(),
                    minecraftVersion.getMinor(),
                    minecraftVersion.getPatch());
            logger.error("Пожалуйста, используйте 1.18.2 и выше для стабильной работы!");
        }

        if (this.audiences == null) this.audiences = BukkitAudiences.create(plugin);
    }

    public void onDisable() {
        if (audiences != null) audiences.close();
    }

    @Override
    public @NotNull BukkitAudiences getAudiences() {
        return audiences;
    }

    @Override
    public @NotNull JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull PluginLogger getLogger() {
        return logger;
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @NotNull PluginDescriptionFile getDescription() {
        return plugin.getDescription();
    }
}
