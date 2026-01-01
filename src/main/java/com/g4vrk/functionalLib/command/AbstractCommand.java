package com.g4vrk.functionalLib.command;

import com.g4vrk.functionalLib.util.TaskUtil;
import com.g4vrk.functionalLib.util.command.CommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

    protected void registerCommand(String commandName, JavaPlugin plugin) {
        CommandBuilder.builder()
                .plugin(plugin)
                .name(commandName)
                .permission(getPermission())
                .aliases(getAliases())
                .executor(this)
                .tabCompleter(this)
                .build().register();
    }

    protected @Nullable String getPermission() {
        return null;
    }

    protected @NotNull List<String> getAliases() {
        return new ArrayList<>();
    }

    protected boolean runAsync() {
        return false;
    }

    protected @Nullable Player checkPlayer(CommandSender sender) {
        return sender instanceof Player player ? player : null;
    }
    protected @Nullable Player checkPlayer(String name) {
        return Bukkit.getPlayer(name);
    }

    public abstract void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);
    public abstract @Nullable List<String> onComplete(@NotNull CommandSender sender, @NotNull String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (runAsync()) {
            TaskUtil.runAsync(() -> onExecute(sender, label, args));
        } else {
            onExecute(sender, label, args);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return filter(onComplete(commandSender, args), args);
    }

    private List<String> filter(List<String> list, String[] args) {
        if (args.length == 0 || list == null) return list;
        return list.stream()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .toList();
    }
}
