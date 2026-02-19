package com.g4vrk.functionalLib.command.util;

import com.g4vrk.functionalLib.FunctionalLibAPI;
import com.g4vrk.functionalLib.command.AbstractCommand;
import com.g4vrk.functionalLib.command.requirement.CommandRequirement;
import com.g4vrk.functionalLib.command.registrator.CommandRegistrator;
import com.g4vrk.functionalLib.command.subCommand.SubCommand;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandBuilder {

    private final JavaPlugin plugin;
    private final String name;
    private String description = "Not provided";
    private String usage = "";
    private String permission;
    private final List<String> aliases = new ArrayList<>();
    @Getter
    private final Map<String, SubCommand> subCommands = new LinkedHashMap<>();
    @Getter
    private final List<CommandRequirement> requirements = new ArrayList<>();
    private CommandExecutor executor;
    private TabCompleter tabCompleter;
    private boolean runAsync = false;
    private boolean overlapExisting = false;

    public CommandBuilder(@NotNull JavaPlugin plugin, @NotNull String name) {
        this.plugin = Objects.requireNonNull(plugin, "plugin cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
    }

    public static CommandBuilder of(@NotNull JavaPlugin plugin, @NotNull Command command) {
        CommandBuilder builder = new CommandBuilder(plugin, command.getName());
        builder.description = command.getDescription();
        builder.usage = command.getUsage();
        builder.aliases.addAll(command.getAliases());
        builder.executor = (sender, cmd, label, args) -> command.execute(sender, label, args);
        builder.tabCompleter = (sender, cmd, alias, args) -> command.tabComplete(sender, alias, args);
        builder.overlapExisting = true;
        return builder;
    }

    public static CommandBuilder of(@NotNull JavaPlugin plugin, @NotNull AbstractCommand command) {
        CommandBuilder builder = new CommandBuilder(plugin, command.getName());
        builder.description = command.getDescription();
        builder.usage = command.getUsage();
        builder.aliases.addAll(command.getAliases());
        builder.subCommands.putAll(command.getSubCommands());
        builder.requirements.addAll(command.getRequirements());
        builder.executor = (sender, cmd, label, args) -> command.execute(sender, label, args);
        builder.tabCompleter = (sender, cmd, alias, args) -> command.tabComplete(sender, alias, args);
        builder.runAsync = command.runAsync();
        builder.permission = command.getPermission();
        builder.overlapExisting = true;
        return builder;
    }

    public CommandBuilder description(String description) {
        this.description = description;
        return this;
    }

    public CommandBuilder usage(String usage) {
        this.usage = usage;
        return this;
    }

    public CommandBuilder permission(String permission) {
        this.permission = permission;
        return this;
    }

    public CommandBuilder alias(String alias) {
        if (alias != null) this.aliases.add(alias);
        return this;
    }

    public CommandBuilder aliases(Collection<String> aliases) {
        if (aliases != null) this.aliases.addAll(aliases);
        return this;
    }

    public CommandBuilder subCommand(@NotNull SubCommand subCommand) {
        this.subCommands.put(subCommand.getName().toLowerCase(), subCommand);
        for (String alias : subCommand.getAliases()) {
            this.subCommands.put(alias.toLowerCase(), subCommand);
        }
        return this;
    }

    public CommandBuilder subCommands(@NotNull Collection<SubCommand> subCommands) {
        subCommands.forEach(this::subCommand);
        return this;
    }

    public CommandBuilder requirement(@NotNull CommandRequirement requirement) {
        this.requirements.add(requirement);
        return this;
    }

    public CommandBuilder requirements(@NotNull Collection<CommandRequirement> requirements) {
        this.requirements.addAll(requirements);
        return this;
    }

    public CommandBuilder executor(CommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    public CommandBuilder tabCompleter(TabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
        return this;
    }

    public CommandBuilder runAsync(boolean runAsync) {
        this.runAsync = runAsync;
        return this;
    }

    public CommandBuilder overlapExisting(boolean overlap) {
        this.overlapExisting = overlap;
        return this;
    }

    public AbstractCommand build() {
        AbstractCommand command = new AbstractCommand(name, description, usage, aliases) {
            @Override
            public boolean runAsync() {
                return CommandBuilder.this.runAsync;
            }
            @Override
            protected void onExecuteNoArgs(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {}
        };

        if (permission != null) command.setPermission(permission);
        command.addSubCommands(subCommands.values());
        command.setRequirements(requirements);

        return command;
    }

    public AbstractCommand register() {
        AbstractCommand command = build();
        var api = FunctionalLibAPI.getAPI().orElseThrow(() -> new NullPointerException("API не было найдено!"));

        CommandRegistrator registrator = api.getCommandRegistrator();

        if (overlapExisting)
            registrator.getCommand(command.getName()).ifPresent(existing -> registrator.override(existing, command));
        else
            registrator.register(plugin, command);

        api.getLogger().info("Команда /{} (Алиасы: {}) успешно зарегистрирована плагином {}.",
                        command.getName(),
                        String.join(", ", command.getAliases()),
                        plugin.getName()
        );

        return command;
    }
}
