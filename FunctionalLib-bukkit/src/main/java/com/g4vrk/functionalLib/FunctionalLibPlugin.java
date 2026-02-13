package com.g4vrk.functionalLib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public final class FunctionalLibPlugin extends AbstractPlugin {

    private Bootstrap bootstrap;

    public FunctionalLibPlugin() {
    }

    @Override
    public void onPluginLoad() {
        this.bootstrap = new Bootstrap(this);

        var services = Bukkit.getServicesManager();

        services.register(
                FunctionalLibAPI.class,
                bootstrap,
                this,
                ServicePriority.Highest
        );

        bootstrap.onLoad();
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
