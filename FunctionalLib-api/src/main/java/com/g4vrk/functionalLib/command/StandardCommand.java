package com.g4vrk.functionalLib.command;

import com.g4vrk.functionalLib.FunctionalLibAPI;
import com.g4vrk.functionalLib.command.argument.Argument;
import com.g4vrk.functionalLib.command.context.CommandContext;
import com.g4vrk.functionalLib.command.registrator.CommandRegistrator;
import com.g4vrk.functionalLib.command.requirement.CommandRequirement;

import com.g4vrk.functionalLib.util.TaskUtil;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class StandardCommand extends Command {

    private final Map<String, Argument> subCommands = new HashMap<>();
    private final List<CommandRequirement> requirements = new ArrayList<>();

    private Consumer<CommandContext> noArgumentsConsumer = ctx -> {};
    private Consumer<CommandContext> unknownArgumentConsumer = ctx -> {};
    private ExecutionStrategy executionStrategy = ExecutionStrategy.SYNC;

    public enum ExecutionStrategy { SYNC, ASYNC }

    public StandardCommand(@NotNull String name) {
        super(name);
    }

    public StandardCommand(@NotNull String name, @NotNull String description, @NotNull String usage, @NotNull List<String> aliases) {
        super(name, description, usage, aliases);
    }

    public static Builder builder(@NotNull String name) {
        return new Builder(name);
    }

    public static class Builder {
        private final String name;
        private String description = "";
        private String usage = "";
        private String permission;
        private List<String> aliases = new ArrayList<>();

        private final List<Argument> arguments = new ArrayList<>();
        private final List<CommandRequirement> requirements = new ArrayList<>();

        private Consumer<CommandContext> noArgumentConsumer = ctx -> {};
        private Consumer<CommandContext> unknownArgumentConsumer = ctx -> {};

        private ExecutionStrategy strategy = ExecutionStrategy.SYNC;

        private Builder(String name) {
            this.name = name;
        }

        public Builder description(@NotNull String description) {
            this.description = description;
            return this;
        }

        public Builder usage(@NotNull String usage) {
            this.usage = usage;
            return this;
        }

        public Builder permission(@NotNull String permission) {
            this.permission = permission;
            return this;
        }

        public Builder aliases(@NotNull String... aliases) {
            this.aliases = List.of(aliases);
            return this;
        }

        public Builder argument(@NotNull Argument arg) {
            this.arguments.add(arg);
            return this;
        }

        public Builder requirements(@NotNull CommandRequirement... requirements) {
            Collections.addAll(this.requirements, requirements);
            return this;
        }

        public Builder onNoArguments(@NotNull Consumer<CommandContext> handler) {
            this.noArgumentConsumer = handler;
            return this;
        }

        public Builder onUnknownArgument(@NotNull Consumer<CommandContext> handler) {
            this.unknownArgumentConsumer = handler;
            return this;
        }

        public Builder executionStrategy(@NotNull ExecutionStrategy strategy) {
            this.strategy = strategy;
            return this;
        }

        public StandardCommand build() {
            StandardCommand cmd = new StandardCommand(name, description, usage, aliases);

            for (Argument arg : arguments) {
                cmd.addArgument(arg);
            }

            cmd.setRequirements(requirements);
            cmd.onNoArguments(noArgumentConsumer);
            cmd.onUnknownArgument(unknownArgumentConsumer);
            cmd.executionStrategy(strategy);
            cmd.setPermission(permission);

            return cmd;
        }
    }

    public void addArgument(@NotNull Argument arg) {
        subCommands.put(arg.getName().toLowerCase(), arg);
        for (String alias : arg.getAliases()) subCommands.put(alias.toLowerCase(), arg);
    }

    public void setRequirements(@NotNull Collection<CommandRequirement> requirements) {
        this.requirements.clear();
        this.requirements.addAll(requirements);
    }

    public void onNoArguments(@NotNull Consumer<CommandContext> handler) {
        this.noArgumentsConsumer = handler;
    }

    public void onUnknownArgument(@NotNull Consumer<CommandContext> handler) {
        this.unknownArgumentConsumer = handler;
    }

    public void executionStrategy(@NotNull ExecutionStrategy strategy) {
        this.executionStrategy = strategy;
    }

    public void register(@NotNull JavaPlugin plugin, boolean override) {
        FunctionalLibAPI api = FunctionalLibAPI.getAPI().orElseThrow(() -> new NullPointerException("API не инициализировано!"));
        CommandRegistrator registrator = api.getCommandRegistrator();

        if (getName().equalsIgnoreCase(plugin.getName())) {
            api.getLogger().error("Команда не может быть такой же, как название плагина!");
            api.getLogger().error("Регистрация команды отменена, для устранения ошибок. (/{}:{})", getName(), plugin.getName(), getName());
        }

        if (override)
            registrator.getCommand(getName()).ifPresent(existing -> registrator.override(existing, this));
        else
            registrator.register(plugin, this);

        if (CommodoreProvider.isSupported()) {
            try {
                Commodore commodore = CommodoreProvider.getCommodore(plugin);

                LiteralArgumentBuilder<CommandSender> root = LiteralArgumentBuilder
                        .<CommandSender>literal(getName())
                        .executes(ctx -> {
                            runCommand(ctx.getSource(), new String[0]);
                            return 1;
                        });

                for (Argument arg : new HashSet<>(subCommands.values())) {
                    root.then(buildBrigadier(arg));
                }

                commodore.register(this, root);

            } catch (Exception ignored) {}
        }

        api.getLogger().info("Команда /{} (Алиасы: {}) зарегистрирована плагином {}",
                getName(),
                String.join(", ", getAliases().isEmpty() ? getAliases() : List.of("Not provided")),
                plugin.getName()
        );
    }

    private ArgumentBuilder<CommandSender, ?> buildBrigadier(Argument arg) {

        ArgumentBuilder<CommandSender, ?> node;

        if (arg.getArgumentType() == null)
            node = LiteralArgumentBuilder.literal(arg.getName());
        else
            node = RequiredArgumentBuilder.argument(arg.getName(), arg.getArgumentType());

        node.requires(sender -> {
            for (CommandRequirement r : arg.getRequirements()) {
                if (!r.test(sender, new String[0])) return false;
            }
            return true;
        });

        node.executes(ctx -> 1);

        for (Argument child : arg.getArguments()) {
            node.then(buildBrigadier(child));
        }

        return node;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        Runnable task = () -> runCommand(sender, args);

        if (executionStrategy == ExecutionStrategy.ASYNC)
            TaskUtil.runAsync(task);
        else
            task.run();

        return true;
    }

    private void runCommand(CommandSender sender, String[] args) {
        if (!checkRequirements(sender, args, requirements, false)) return;

        if (args.length == 0) {
            noArgumentsConsumer.accept(new CommandContext(sender, args));
            return;
        }

        Optional<Argument> matched = matchArgument(sender, args, subCommands, 0);

        if (matched.isEmpty()) {
            unknownArgumentConsumer.accept(new CommandContext(sender, args));
            return;
        }

        matched.get().execute(new CommandContext(sender, args));
    }

    private Optional<Argument> matchArgument(CommandSender sender, String[] args, Map<String, Argument> map, int index) {
        if (index >= args.length) return Optional.empty();

        Argument current = map.get(args[index].toLowerCase());

        if (current == null) return Optional.empty();
        if (!checkRequirements(sender, args, current.getRequirements(), false)) return Optional.empty();
        if (index == args.length - 1) return Optional.of(current);

        return matchArgument(sender, args, buildMap(current.getArguments()), index + 1);
    }

    private Map<String, Argument> buildMap(Collection<Argument> args) {
        Map<String, Argument> map = new HashMap<>();

        for (Argument arg : args) {
            map.put(arg.getName().toLowerCase(), arg);
            for (String alias : arg.getAliases()) map.put(alias.toLowerCase(), arg);
        }

        return map;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {

        List<String> completions = new ArrayList<>();

        if (args.length == 0 || (args.length == 1 && args[0].isEmpty())) {
            for (Argument arg : new HashSet<>(subCommands.values())) {
                if (!checkRequirements(sender, args, arg.getRequirements(), true)) continue;

                completions.add(arg.getName());
                completions.addAll(arg.getAliases());
            }
            return completions;
        }

        String firstArg = args[0].toLowerCase();
        if (firstArg.equalsIgnoreCase(getName()) || getAliases().stream().anyMatch(a -> a.equalsIgnoreCase(firstArg))) {
            args = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];
        }

        Map<String, Argument> currentMap = subCommands;

        for (int i = 0; i < args.length - 1; i++) {
            Argument next = currentMap.get(args[i].toLowerCase());
            if (next == null) return completions;

            currentMap = buildMap(next.getArguments());
        }

        String last = args.length > 0 ? args[args.length - 1].toLowerCase() : "";

        for (Argument arg : new HashSet<>(currentMap.values())) {

            if (!checkRequirements(sender, args, arg.getRequirements(), true)) continue;

            if (arg.getName().toLowerCase().startsWith(last)) completions.add(arg.getName());

            for (String aliasStr : arg.getAliases())
                if (aliasStr.toLowerCase().startsWith(last)) completions.add(aliasStr);
        }

        Collections.sort(completions);
        return completions;
    }

    private boolean checkRequirements(CommandSender sender, String[] args, Collection<CommandRequirement> requirements, boolean silent) {
        for (CommandRequirement r : requirements) {
            if (!r.test(sender, args)) {
                if (!silent) r.onFail(sender, args);
                return false;
            }
        }
        return true;
    }
}