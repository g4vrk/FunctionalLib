package com.g4vrk.functionalLib.command.registrator.impl;

import com.g4vrk.functionalLib.command.registrator.BaseCommandRegistrator;
import com.g4vrk.functionalLib.util.Reflect;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.util.Map;

public final class LegacyCommandRegistrator extends BaseCommandRegistrator {

    private static final CommandMap COMMAND_MAP = resolveCommandMap();
    private static final Map<String, Command> KNOWN_COMMANDS = resolveKnownCommands();

    public LegacyCommandRegistrator() {
        super(COMMAND_MAP, KNOWN_COMMANDS);
    }

    private static CommandMap resolveCommandMap() {
        try {
            return (CommandMap) Reflect.getFieldValue(Bukkit.getServer(), "commandMap");
        } catch (Exception e) {
            throw new IllegalStateException("Cannot resolve server commandMap value", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Command> resolveKnownCommands() {
        try {
            return (Map<String, Command>) Reflect.getFieldValue(LegacyCommandRegistrator.COMMAND_MAP, "knownCommands");
        } catch (Exception e) {
            throw new IllegalStateException("Cannot resolve commandMap knownCommands field", e);
        }
    }
}
