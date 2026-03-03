package com.g4vrk.functionalLib.command.registrator.impl;

import com.g4vrk.functionalLib.command.registrator.BaseCommandRegistrator;
import org.bukkit.Bukkit;

public final class PaperCommandRegistrator extends BaseCommandRegistrator {
    public PaperCommandRegistrator() {
        super(Bukkit.getCommandMap(), Bukkit.getCommandMap().getKnownCommands());
    }
}
