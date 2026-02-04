package com.g4vrk.functionalLib.command.requirement;

import org.bukkit.command.CommandSender;

public interface CommandRequirement {
    boolean test(CommandSender sender, String[] args);

    void onFail(CommandSender sender, String[] args);
}
