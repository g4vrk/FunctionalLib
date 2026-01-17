package com.g4vrk.functionalLib.util.command;

import com.g4vrk.functionalLib.util.MinecraftVersion;
import com.g4vrk.functionalLib.util.Reflect;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;
import java.util.Map;

@UtilityClass
public final class CommandUtil {

    private static final CommandMap COMMAND_MAP = resolveCommandMap();

    private static CommandMap resolveCommandMap() {
        if (MinecraftVersion.isPaper() || MinecraftVersion.isPurpur()) {
            try {
                return Bukkit.getCommandMap();
            } catch (Throwable ignored) {}
        }
        Server server = Bukkit.getServer();
        return (CommandMap) Reflect.getFieldValue(server, "commandMap");
    }

    public static PluginCommand create(String name, Plugin plugin) {
        return Reflect.newInstance(PluginCommand.class, name, plugin);
    }

    public static void register(Plugin plugin, PluginCommand command) {
        unregister(command.getName());
        COMMAND_MAP.register(plugin.getName().toLowerCase(), command);
    }

    public static void unregister(String name) {
        Map<String, Command> known = getKnownCommands();

        Iterator<Map.Entry<String, Command>> it = known.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Command> entry = it.next();

            String key = entry.getKey();
            Command command = entry.getValue();

            if (matches(key, name) || command.getName().equalsIgnoreCase(name)) {
                command.unregister(COMMAND_MAP);
                it.remove();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Command> getKnownCommands() {
        return (Map<String, Command>) Reflect.getFieldValue(COMMAND_MAP, "knownCommands");
    }

    private static boolean matches(String key, String name) {
        return key.equalsIgnoreCase(name)
                || key.equalsIgnoreCase("minecraft:" + name)
                || key.endsWith(":" + name);
    }
}
