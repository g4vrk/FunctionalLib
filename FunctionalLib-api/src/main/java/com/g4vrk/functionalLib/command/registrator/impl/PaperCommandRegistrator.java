package com.g4vrk.functionalLib.command.registrator.impl;

import com.g4vrk.functionalLib.command.registrator.AbstractCommandRegistrator;
import org.bukkit.Bukkit;

public final class PaperCommandRegistrator extends AbstractCommandRegistrator {
    public PaperCommandRegistrator() {
        super(Bukkit.getCommandMap(), Bukkit.getCommandMap().getKnownCommands());
    }
}
