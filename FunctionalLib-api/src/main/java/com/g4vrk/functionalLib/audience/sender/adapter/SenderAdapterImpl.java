package com.g4vrk.functionalLib.audience.sender.adapter;

import com.g4vrk.functionalLib.audience.sender.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SenderAdapterImpl implements SenderAdapter {
    @Override
    public Optional<Sender> fromCommandSender(Player player) {
        if (player == null) return Optional.empty();
        return Optional.of(new Sender(player));
    }

    @Override
    public Optional<Sender> fromCommandSender(CommandSender sender) {
        if (sender == null) return Optional.empty();
        return Optional.of(new Sender(sender));
    }

    @Override
    public Optional<Player> asPlayer(Sender sender) {
        if (sender instanceof Player player) return Optional.of(player);
        return Optional.empty();
    }

    @Override
    public Optional<CommandSender> asCommandSender(Sender sender) {
        if (sender instanceof CommandSender commandSender) return Optional.of(commandSender);
        return Optional.empty();
    }
}
