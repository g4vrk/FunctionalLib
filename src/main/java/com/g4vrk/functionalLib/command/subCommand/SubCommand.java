package com.g4vrk.functionalLib.command.subCommand;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {

    String getName();

    List<String> getAliases();

    String getDescription();

    String getUsage();

    String getPermission();

    boolean isPlayerOnly();

    int getMinimumArgs();

    void execute(CommandSender sender, String[] args);

    List<String> tabComplete(CommandSender sender, String[] args);
}
