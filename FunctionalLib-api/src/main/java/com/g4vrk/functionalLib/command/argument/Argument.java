package com.g4vrk.functionalLib.command.argument;

import com.g4vrk.functionalLib.command.context.CommandContext;
import com.g4vrk.functionalLib.command.requirement.CommandRequirement;
import com.mojang.brigadier.arguments.ArgumentType;

import java.util.Collection;
import java.util.List;

public interface Argument {
    String getName();
    List<String> getAliases();

    ArgumentType<?> getArgumentType();

    Collection<CommandRequirement> getRequirements();

    Collection<Argument> getArguments();

    void execute(CommandContext context);

    default boolean runAsync() {
        return false;
    }
}

