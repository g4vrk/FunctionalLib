package com.g4vrk.functionalLib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public final class FunctionalLibPlugin extends AbstractPlugin {

    private Loader loader;

    public FunctionalLibPlugin() {
    }

    @Override
    public void onPluginLoad() {
        this.loader = new Loader(this);

        var services = Bukkit.getServicesManager();

        services.register(
                FunctionalLibAPI.class,
                loader,
                this,
                ServicePriority.Highest
        );

        loader.onLoad();
        loader.onLoad();
    }

    @Override
    public void onPluginEnable() {
        loader.onEnable();
    }

    @Override
    public void onPluginDisable() {
        loader.onDisable();
    }
}
