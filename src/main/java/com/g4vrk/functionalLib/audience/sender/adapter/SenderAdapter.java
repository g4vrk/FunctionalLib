package com.g4vrk.functionalLib.audience.sender.adapter;

import com.g4vrk.functionalLib.audience.sender.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public interface SenderAdapter {
    Optional<Sender> fromCommandSender(Player player);
    Optional<Sender> fromCommandSender(CommandSender sender);

    Optional<Player> asPlayer(Sender sender);
    Optional<CommandSender> asCommandSender(Sender sender);

    static SenderAdapter adapter() {
        return new SenderAdapterImpl();
    }
}
