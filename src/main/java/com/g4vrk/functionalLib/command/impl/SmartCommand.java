package com.g4vrk.functionalLib.command.impl;

import com.g4vrk.functionalLib.command.SmartAbstractCommand;
import com.g4vrk.functionalLib.command.requirement.CommandRequirement;
import com.g4vrk.functionalLib.command.subCommand.SubCommand;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@RequiredArgsConstructor
public class SmartCommand extends SmartAbstractCommand {

    protected final String commandName;
    protected final JavaPlugin plugin;
    protected final boolean forceRegister;

    public static Builder builder() {
        return new Builder();
    }

    public void registerCommand() {
        super.registerCommand(commandName, plugin, forceRegister);
    }

    public static final class Builder {

        private String commandName;
        private JavaPlugin plugin;
        private boolean forceRegister;

        private final Map<String, SubCommand> subCommands = new HashMap<>();
        private final List<CommandRequirement> requirementList = new ArrayList<>();
        private final List<String> aliases = new ArrayList<>();
        private String permission;
        private boolean runAsync;

        public Builder commandName(String commandName) {
            this.commandName = commandName;
            return this;
        }

        public Builder plugin(JavaPlugin plugin) {
            this.plugin = plugin;
            return this;
        }

        public Builder forceRegister(boolean forceRegister) {
            this.forceRegister = forceRegister;
            return this;
        }

        public Builder subCommand(@NotNull SubCommand subCommand) {
            subCommands.put(subCommand.getName().toLowerCase(), subCommand);
            for (String alias : subCommand.getAliases()) {
                subCommands.put(alias.toLowerCase(), subCommand);
            }

            return this;
        }

        public Builder subCommands(@NotNull Collection<SubCommand> subCommandList) {
            subCommandList.forEach(subCommand -> {
                subCommands.put(subCommand.getName().toLowerCase(), subCommand);
                for (String alias : subCommand.getAliases()) {
                    subCommands.put(alias.toLowerCase(), subCommand);
                }
            });
            return this;
        }

        public Builder requirement(@NotNull CommandRequirement requirement) {
            this.requirementList.add(requirement);
            return this;
        }

        public Builder requirement(@NotNull Collection<CommandRequirement> commandRequirements) {
            this.requirementList.addAll(commandRequirements);
            return this;
        }

        public Builder permission(@NotNull String perm) {
            this.permission = perm;
            return this;
        }

        public Builder aliases(@NotNull Collection<String> aliases) {
            this.aliases.clear();
            this.aliases.addAll(aliases);
            return this;
        }

        public Builder alias(@NotNull String alias) {
            if (aliases.contains(alias)) return this;
            this.aliases.add(alias);
            return this;
        }

        public Builder runAsync(boolean runAsync) {
            this.runAsync = runAsync;
            return this;
        }

        public SmartCommand build() {
            SmartCommand abstractCommand = new SmartCommand(commandName, plugin, forceRegister) {
                @Override
                protected boolean runAsync() {
                    return runAsync;
                }

                @Override
                protected @Nullable String getPermission() {
                    return permission;
                }

                @Override
                protected @NotNull List<String> getAliases() {
                    return aliases;
                }
            };
            abstractCommand.requirements(requirementList);
            abstractCommand.subCommands(subCommands.values());
            return abstractCommand;
        }
    }
}
