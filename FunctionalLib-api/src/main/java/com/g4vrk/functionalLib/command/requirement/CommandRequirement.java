package com.g4vrk.functionalLib.command.requirement;

import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface CommandRequirement {
    boolean test(CommandSender sender, String[] args);

    default void onFail(CommandSender sender, String[] args) {
    }
}
