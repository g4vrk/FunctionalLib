package com.g4vrk.functionalLib.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EventListener implements Listener {

    private final PluginManager pluginManager = Bukkit.getPluginManager();

    public void registerEvents(JavaPlugin plugin) {
        pluginManager.registerEvents(this, plugin);
    }
}
