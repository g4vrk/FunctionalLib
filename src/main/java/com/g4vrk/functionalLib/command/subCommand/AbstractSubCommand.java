package com.g4vrk.functionalLib.command.subCommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public abstract class AbstractSubCommand implements SubCommand {

    @Override
    public abstract String getName();

    @Override
    public abstract void execute(CommandSender sender, String[] args);

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public String getDescription() {
        return "Без описания";
    }

    @Override
    public String getUsage() {
        return "/" + getName();
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public int getMinimumArgs() {
        return 0;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    protected boolean checkPermission(CommandSender sender) {
        String perm = getPermission();
        return perm == null || sender.hasPermission(perm);
    }

    protected Player requirePlayer(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            throw new IllegalStateException("Команда доступна только игроку");
        }
        return player;
    }
}
