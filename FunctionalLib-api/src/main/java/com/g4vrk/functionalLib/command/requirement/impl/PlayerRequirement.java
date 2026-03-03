package com.g4vrk.functionalLib.command.requirement.impl;

import com.g4vrk.functionalLib.command.requirement.CommandRequirement;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public class PlayerRequirement implements CommandRequirement {

    private final BiConsumer<CommandSender, String[]> onFail;

    public PlayerRequirement(BiConsumer<CommandSender, String[]> onFail) {
        this.onFail = onFail;
    }

    @Override
    public boolean test(CommandSender sender, String[] args) {
        return sender instanceof Player;
    }

    @Override
    public void onFail(CommandSender sender, String[] args) {
        onFail.accept(sender, args);
    }
}
