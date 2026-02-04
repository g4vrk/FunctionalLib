package com.g4vrk.functionalLib.actions.impl;

import com.g4vrk.functionalLib.actions.Action;
import com.g4vrk.functionalLib.actions.ActionContext;
import org.bukkit.Bukkit;

public class ConsoleAction implements Action {

    @Override
    public void execute(ActionContext context, String args) {
        if (args == null || args.isBlank()) return;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), args);
    }

    @Override
    public String getActionKey() {
        return "console";
    }
}
