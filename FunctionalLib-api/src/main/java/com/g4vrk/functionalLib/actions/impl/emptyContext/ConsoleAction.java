package com.g4vrk.functionalLib.actions.impl.emptyContext;

import com.g4vrk.functionalLib.actions.AbstractAction;
import com.g4vrk.functionalLib.actions.EmptyContext;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;

@SuppressWarnings("deprecation")
public class ConsoleAction extends AbstractAction<EmptyContext> {

    private final Server server = Bukkit.getServer();

    public ConsoleAction() {
        super(new NamespacedKey("functionallib", "console"));
    }

    @Override
    public void execute(EmptyContext context, String args) {
        if (args == null || args.isBlank()) return;

        server.dispatchCommand(server.getConsoleSender(), args);
    }
}
