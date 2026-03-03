package com.g4vrk.functionalLib.command.argument;

import com.g4vrk.functionalLib.command.context.CommandContext;
import com.g4vrk.functionalLib.command.requirement.CommandRequirement;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import lombok.Setter;
import org.bukkit.command.CommandExecutor;

import java.util.*;

@Setter
public class StandardArgument implements Argument {

    private String name;
    private List<String> aliases;
    private ArgumentType<?> argumentType;
    private Collection<CommandRequirement> requirements;
    private boolean runAsync;

    private CommandExecutor executor;

    private final Map<String, Argument> arguments = new HashMap<>();

    public StandardArgument(
            String name,
            List<String> aliases,
            Collection<CommandRequirement> requirements,
            boolean runAsync,
            CommandExecutor executor
    ) {
        this.name = name;

        this.aliases = aliases != null
                ? new ArrayList<>(aliases)
                : new ArrayList<>();

        this.requirements = requirements != null
                ? new ArrayList<>(requirements)
                : new ArrayList<>();

        this.runAsync = runAsync;
        this.executor = executor;
    }

    public StandardArgument(
            String name,
            List<String> aliases,
            boolean runAsync,
            CommandExecutor executor
    ) {
        this(name, aliases, Collections.emptyList(), runAsync, executor);
    }

    public StandardArgument(
            String name,
            boolean runAsync,
            CommandExecutor executor
    ) {
        this(name, Collections.emptyList(), Collections.emptyList(), runAsync, executor);
    }

    public StandardArgument(
            String name,
            CommandExecutor executor
    ) {
        this(name, Collections.emptyList(), Collections.emptyList(), false, executor);
    }

    public StandardArgument argument(Argument argument) {
        arguments.put(argument.getName().toLowerCase(), argument);

        for (String alias : argument.getAliases()) {
            arguments.put(alias.toLowerCase(), argument);
        }

        return this;
    }

    public StandardArgument argument(Collection<Argument> arguments) {
        arguments.forEach(this::argument);
        return this;
    }

    public StandardArgument executes(CommandExecutor commandExecutor) {
        this.executor = commandExecutor;
        return this;
    }

    public StandardArgument requires(CommandRequirement commandRequirement) {
        this.requirements.add(commandRequirement);
        return this;
    }

    public StandardArgument alias(String alias) {
        this.aliases.add(alias);
        return this;
    }

    public StandardArgument aliases(Collection<String> aliases) {
        this.aliases.addAll(aliases);
        return this;
    }

    public StandardArgument clearAliases() {
        this.aliases.clear();
        return this;
    }

    public StandardArgument type(ArgumentType<?> argumentType) {
        this.argumentType = argumentType;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getAliases() {
        return Collections.unmodifiableList(aliases);
    }

    @Override
    public ArgumentType<?> getArgumentType() {
        return argumentType;
    }

    @Override
    public Collection<CommandRequirement> getRequirements() {
        return Collections.unmodifiableCollection(requirements);
    }

    @Override
    public Collection<Argument> getArguments() {
        return arguments.values()
                .stream()
                .distinct()
                .toList();
    }

    @Override
    public void execute(CommandContext context) {
        var args = context.args();
        var sender = context.sender();

        if (args.length > 0) {
            Argument next = arguments.get(args[0].toLowerCase());

            if (next != null) {
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                next.execute(new CommandContext(sender, subArgs));
                return;
            }
        }

        if (executor != null) // noinspection DataFlowIssue
            executor.onCommand(sender, null, null, args);
    }

    @Override
    public boolean runAsync() {
        return runAsync;
    }

    public static StandardArgument of(String name) {
        return new StandardArgument(name, null);
    }
}