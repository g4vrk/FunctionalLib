package com.g4vrk.functionalLib.command.context;

import org.bukkit.command.CommandSender;

public record CommandContext(CommandSender sender, String[] args) {
}
