package com.g4vrk.functionalLib.util.command;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

@UtilityClass
public final class CommandUtil {

    private static final CommandMap COMMAND_MAP;

    static {
        try {
            Server server = Bukkit.getServer();

            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            COMMAND_MAP = (CommandMap) field.get(server);
        } catch (Throwable e) {
            throw new RuntimeException("Не удалось получить доступ к CommandMap", e);
        }
    }

    public static PluginCommand create(String name, Plugin plugin) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            return constructor.newInstance(name, plugin);
        } catch (Throwable e) {
            throw new RuntimeException("Не удалось создать PluginCommand", e);
        }
    }

    public static void register(Plugin plugin, PluginCommand command) {
        COMMAND_MAP.register(plugin.getName().toLowerCase(), command);
    }

    public static void unregister(String name) {
        try {
            Field field = COMMAND_MAP.getClass().getDeclaredField("knownCommands");
            field.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<String, Command> known = (Map<String, Command>) field.get(COMMAND_MAP);

            known.remove(name);
            known.remove("minecraft:" + name);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
