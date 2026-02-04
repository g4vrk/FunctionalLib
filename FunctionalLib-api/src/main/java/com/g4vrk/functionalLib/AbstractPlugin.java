package com.g4vrk.functionalLib;

import com.g4vrk.functionalLib.logging.PluginLogger;import com.g4vrk.functionalLib.logging.PluginLoggerFactory;import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractPlugin extends JavaPlugin {

    protected final PluginManager pluginManager = Bukkit.getPluginManager();
    protected PluginLogger logger;

    @Override
    public final void onLoad() {
        this.onPluginLoad();
    }

    @Override
    public final void onEnable() {
        long startTime = System.currentTimeMillis();
        getDataFolder().mkdirs();
        this.logger = PluginLoggerFactory.getLogger(this);

        this.onPluginEnable();

        long startupTime = System.currentTimeMillis() - startTime;

        logger.info("Плагин {} был успешно включен! ({} мс)", getName() + " v" + getDescription().getVersion(), startupTime);
        logger.info("Спасибо за использование! ~{}", String.join(", ", getDescription().getAuthors()));
    }

    @Override
    public final void onDisable() {
        this.onPluginDisable();
    }

    protected boolean checkLib() {
        if (pluginManager.getPlugin("FunctionalLib") == null) {
            logger.error("Плагин FunctionalLib не найден!");
            logger.error("Скачайте его для работы плагина.");
            logger.error("Репозиторий github -> https://github.com/g4vrk/FunctionalLib");
            this.setEnabled(false);
            return false;
        }
        return true;
    }

    protected abstract void onPluginLoad();
    protected abstract void onPluginEnable();
    protected abstract void onPluginDisable();
}
