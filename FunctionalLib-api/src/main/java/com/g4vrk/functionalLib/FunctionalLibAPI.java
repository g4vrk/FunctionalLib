package com.g4vrk.functionalLib;

import com.g4vrk.functionalLib.logging.PluginLogger;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface FunctionalLibAPI {

    @NotNull BukkitAudiences getAudiences();
    @NotNull JavaPlugin getPlugin();
    @NotNull PluginLogger getLogger();

    @NotNull String getVersion();
    @NotNull PluginDescriptionFile getDescription();

    static Optional<FunctionalLibAPI> getAPI() {
        var api = Bukkit.getServicesManager().getRegistration(FunctionalLibAPI.class);

        return api != null ? Optional.of(api.getProvider()) : Optional.empty();
    }
}
