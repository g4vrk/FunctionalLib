package com.g4vrk.functionalLib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public abstract class BasePlugin extends JavaPlugin {

    protected final PluginManager pluginManager = Bukkit.getPluginManager();
    protected static FunctionalLibPlugin functionalLib;

    public abstract void onEnabling();

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        File folder = this.getDataFolder();

        if (!folder.exists()) {
            folder.mkdirs();
        }

        this.onEnabling();

        long startupTime = System.currentTimeMillis() - startTime;


        getSLF4JLogger().info("Плагин {} был успешно включен! ({} мс)", getName() + " v" + getDescription().getVersion(), startupTime);
        getSLF4JLogger().info("Спасибо за использование! ~{}", String.join(", ", getDescription().getAuthors()));
    }

    protected boolean checkLib() {
        if (pluginManager.getPlugin(FunctionalLibPlugin.NAME) == null) {
            getSLF4JLogger().error("Плагин FunctionalLib не найден!");
            getSLF4JLogger().error("Скачайте его для работы плагина.");
            getSLF4JLogger().error("Репозиторий github -> https://github.com/g4vrk/FunctionalLib");
            this.setEnabled(false);
            return false;
        }
        return true;
    }

    public static void setLib(FunctionalLibPlugin functionalLibPlugin) {
        if (functionalLib != null) {
            FunctionalLibPlugin.logger().error("Библиотека не может быть инициализирована дважды");
            return;
        }
        functionalLib = functionalLibPlugin;
    }
}
