package com.g4vrk.functionalLib.util.command;

import com.g4vrk.functionalLib.FunctionalLibAPI;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Builder
public class CommandBuilder {

    @NonNull
    private final JavaPlugin plugin;

    @NonNull
    private final String name;

    @Builder.Default
    private final boolean forceRegister = false;

    @Builder.Default
    private final String description = "Not provided";

    @Builder.Default
    private final String usage = "";

    private final String permission;

    @Singular("alias")
    private final List<String> aliases;

    @NonNull
    private final CommandExecutor executor;

    private final TabCompleter tabCompleter;

    public void register() {
        PluginCommand command;

        if (!forceRegister || plugin.getCommand(name) != null) {
            command = plugin.getCommand(name);
        } else {
            command = CommandUtil.create(name, plugin);
        }

        if (command == null) return;

        command.setDescription(description);
        command.setUsage(usage.isEmpty() ? "/" + name : usage);
        command.setPermission(permission);

        if (aliases != null && !aliases.isEmpty()) {
            command.setAliases(aliases);
        }

        command.setExecutor(executor);

        if (tabCompleter != null) {
            command.setTabCompleter(tabCompleter);
        }
        CommandUtil.register(plugin, command);
        FunctionalLibAPI.getAPI().orElseThrow(NullPointerException::new).getLogger().info("Команда /{} (Алиасы: {}) успешно зарегистрирована плагином {}.",
                command.getLabel(),
                String.join(", ", command.getAliases()),
                plugin.getName());
    }
}