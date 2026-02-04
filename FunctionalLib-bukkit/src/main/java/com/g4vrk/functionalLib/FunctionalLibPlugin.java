package com.g4vrk.functionalLib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public final class FunctionalLibPlugin extends AbstractPlugin {

    private final Bootstrap bootstrap;

    public FunctionalLibPlugin() {
        this.bootstrap = new Bootstrap(this);
        var services = Bukkit.getServicesManager();

        services.register(FunctionalLibAPI.class, bootstrap, this, ServicePriority.Highest);
    }

    @Override
    public void onPluginLoad() {
        bootstrap.onLoad();
    }

    @Override
    public void onPluginEnable() {
        bootstrap.onEnable();
    }

    @Override
    public void onPluginDisable() {
        bootstrap.onDisable();
    }
}
