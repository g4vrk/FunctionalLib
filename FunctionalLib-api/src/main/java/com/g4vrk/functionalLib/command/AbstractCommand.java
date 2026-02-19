package com.g4vrk.functionalLib.command;

import com.g4vrk.functionalLib.command.requirement.CommandRequirement;
import com.g4vrk.functionalLib.command.subCommand.SubCommand;
import com.g4vrk.functionalLib.command.util.CommandBuilder;
import com.g4vrk.functionalLib.util.TaskUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class AbstractCommand extends Command {

    private final Map<String, SubCommand> subCommands = new HashMap<>();
    private final List<CommandRequirement> requirements = new ArrayList<>();

    public AbstractCommand(
            @NotNull String name,
            @NotNull String description,
            @NotNull String usage,
            @Nullable List<String> aliases
    ) {
        super(name, description, usage, aliases != null ? aliases : Collections.emptyList());
    }

    protected void register(@NotNull String commandName, @NotNull JavaPlugin plugin) {
        new CommandBuilder(plugin, commandName)
                .permission(getPermission())
                .aliases(getAliases())
                .executor((sender, command, label, args) -> execute(sender, label, args))
                .tabCompleter((sender, command, alias, args) -> tabComplete(sender, alias, args))
                .register();
    }

    public void addSubCommand(@NotNull SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
        for (String alias : subCommand.getAliases()) {
            subCommands.put(alias.toLowerCase(), subCommand);
        }
    }

    public void addSubCommands(@NotNull Collection<SubCommand> collection) {
        collection.forEach(this::addSubCommand);
    }

    public void setRequirements(@NotNull Collection<CommandRequirement> commandRequirements) {
        this.requirements.clear();
        this.requirements.addAll(commandRequirements);
    }

    public boolean runAsync() {
        return false;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (!checkRequirements(sender, args, requirements, false)) return true;

        if (args.length == 0) {
            onExecuteNoArgs(sender, label, args);
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand == null) return true;

        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        if (!checkRequirements(sender, subArgs, subCommand.getRequirements(), false)) return true;

        Runnable task = () -> subCommand.execute(sender, subArgs);

        if (subCommand.runAsync()) {
            TaskUtil.runAsync(task);
        } else {
            TaskUtil.runSync(task);
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return subCommands.values().stream()
                    .distinct()
                    .filter(sc -> checkRequirements(sender, args, sc.getRequirements(), true))
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

    protected boolean checkRequirements(CommandSender sender, String[] args, Collection<CommandRequirement> requirements, boolean silent) {
        for (CommandRequirement requirement : requirements) {
            if (!requirement.test(sender, args)) {
                if (!silent) requirement.onFail(sender, args);
                return false;
            }
        }
        return true;
    }

    protected Optional<Player> checkPlayer(CommandSender sender) {
        return sender instanceof Player player ? Optional.of(player) : Optional.empty();
    }

    protected Optional<Player> checkPlayer(String name) {
        return Optional.ofNullable(Bukkit.getPlayer(name));
    }

    protected abstract void onExecuteNoArgs(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private String description = "Not provided";
        private String usage = "";
        private final List<String> aliases = new ArrayList<>();
        private final Map<String, SubCommand> subCommands = new ConcurrentHashMap<>();
        private final List<CommandRequirement> requirements = new ArrayList<>();
        private boolean runAsync;

        public Builder name(@NotNull String name) {
            this.name = name;
            return this;
        }

        public Builder description(@NotNull String description) {
            this.description = description;
            return this;
        }

        public Builder usage(@NotNull String usage) {
            this.usage = usage;
            return this;
        }

        public Builder aliases(@NotNull Collection<String> aliases) {
            this.aliases.clear();
            this.aliases.addAll(aliases);
            return this;
        }

        public Builder alias(@NotNull String alias) {
            if (!this.aliases.contains(alias)) this.aliases.add(alias);
            return this;
        }

        public Builder subCommand(@NotNull SubCommand subCommand) {
            subCommands.put(subCommand.getName().toLowerCase(), subCommand);
            for (String alias : subCommand.getAliases()) {
                subCommands.put(alias.toLowerCase(), subCommand);
            }
            return this;
        }

        public Builder subCommands(@NotNull Collection<SubCommand> list) {
            list.forEach(this::subCommand);
            return this;
        }

        public Builder requirement(@NotNull CommandRequirement requirement) {
            requirements.add(requirement);
            return this;
        }

        public Builder requirements(@NotNull Collection<CommandRequirement> list) {
            requirements.addAll(list);
            return this;
        }

        public Builder runAsync(boolean runAsync) {
            this.runAsync = runAsync;
            return this;
        }

        public AbstractCommand build() {
            AbstractCommand command = new AbstractCommand(name, description, usage, aliases) {
                @Override
                public boolean runAsync() {
                    return Builder.this.runAsync;
                }

                @Override
                protected void onExecuteNoArgs(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
                }
            };

            command.addSubCommands(subCommands.values());
            command.setRequirements(requirements);
            return command;
        }
    }
}
