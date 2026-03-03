package com.g4vrk.functionalLib.command.requirement.impl;

import com.g4vrk.functionalLib.command.requirement.CommandRequirement;
import org.bukkit.command.CommandSender;

import java.util.function.BiConsumer;

public class PermissionRequirement implements CommandRequirement {

    private final String permission;
    private final BiConsumer<CommandSender, String[]> onFail;

    public PermissionRequirement(String permission, BiConsumer<CommandSender, String[]> onFail) {
        this.permission = permission;
        this.onFail = onFail;
    }

    @Override
    public boolean test(CommandSender sender, String[] args) {
        return sender.hasPermission(permission);
    }

    @Override
    public void onFail(CommandSender sender, String[] args) {
        onFail.accept(sender, args);
    }
}
