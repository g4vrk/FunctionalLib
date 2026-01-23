package com.g4vrk.functionalLib.command;

import com.g4vrk.functionalLib.command.requirement.CommandRequirement;
import com.g4vrk.functionalLib.command.subCommand.SubCommand;
import com.g4vrk.functionalLib.util.TaskUtil;
import com.g4vrk.functionalLib.util.command.CommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class SmartAbstractCommand implements TabExecutor {

    protected final Map<String, SubCommand> subCommands = new HashMap<>();
    protected final List<CommandRequirement> requirements = new ArrayList<>();

    protected void registerCommand(String commandName, JavaPlugin plugin, boolean forceRegister) {
        CommandBuilder.builder()
                .plugin(plugin)
                .forceRegister(forceRegister)
                .name(commandName)
                .permission(getPermission())
                .aliases(getAliases())
                .executor(this)
                .tabCompleter(this)
                .build()
                .register();
    }

    protected void addSubCommand(@NotNull SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
        for (String alias : subCommand.getAliases()) {
            subCommands.put(alias.toLowerCase(), subCommand);
        }
    }

    protected void requirements(@NotNull Collection<CommandRequirement> commandRequirements) {
        this.requirements.clear();
        this.requirements.addAll(commandRequirements);
    }

    protected void subCommands(@NotNull Collection<SubCommand> subCommandCollection) {
        subCommandCollection.forEach(subCommand -> {
            subCommands.put(subCommand.getName().toLowerCase(), subCommand);
            for (String alias : subCommand.getAliases()) {
                subCommands.put(alias.toLowerCase(), subCommand);
            }
        });
    }

    protected List<CommandRequirement> getRequirements() {
        return requirements;
    }

    protected @Nullable String getPermission() {
        return null;
    }

    protected @NotNull List<String> getAliases() {
        return Collections.emptyList();
    }

    protected boolean runAsync() {
        return false;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!checkRequirements(sender, args, getRequirements())) return true;

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand == null) return true;

        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        if (!checkRequirements(sender, subArgs, subCommand.getRequirements())) return true;

        Runnable task = () -> subCommand.execute(sender, subArgs);

        if (subCommand.runAsync()) {
            TaskUtil.runAsync(task);
        } else {
            TaskUtil.runSync(task);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return subCommands.values().stream()
                    .distinct()
                    .map(SubCommand::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand == null) return Collections.emptyList();

        return filter(subCommand.tabComplete(sender, Arrays.copyOfRange(args, 1, args.length)), args[args.length - 1]);
    }

    private List<String> filter(List<String> list, String lastArg) {
        if (list == null) return Collections.emptyList();
        return list.stream()
                .filter(s -> s.toLowerCase().startsWith(lastArg.toLowerCase()))
                .toList();
    }

    protected boolean checkRequirements(CommandSender sender, String[] args, Collection<CommandRequirement> requirements) {
        for (CommandRequirement requirement : requirements) {
            if (!requirement.test(sender, args)) {
                requirement.onFail(sender, args);
                return false;
            }
        }
        return true;
    }

    protected Optional<Player> checkPlayer(CommandSender sender) {
        return Optional.ofNullable((Player) sender);
    }

    protected Optional<Player> checkPlayer(String name) {
        return Optional.ofNullable(Bukkit.getPlayer(name));
    }
}
