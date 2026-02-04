package com.g4vrk.functionalLib.command.subCommand;

import com.g4vrk.functionalLib.command.requirement.CommandRequirement;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.List;

public interface SubCommand {
    String getName();
    List<String> getAliases();

    Collection<CommandRequirement> getRequirements();

    void execute(CommandSender sender, String[] args);
    List<String> tabComplete(CommandSender sender, String[] args);

    default boolean runAsync() {
        return false;
    }
}

