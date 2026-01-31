package com.g4vrk.functionalLib.audience.creator;

import com.g4vrk.functionalLib.FunctionalLibPlugin;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface AudienceCreator {
    Optional<Audience> getAudience(CommandSender sender);
    Optional<Audience> getAudience(Player player);

    Optional<Audience> getAudience(String name);
    Optional<Audience> getAudience(UUID uuid);

    Collection<? extends Audience> getAllAudiences();

    static AudienceCreator creator() {
        return new AudienceCreatorImpl(FunctionalLibPlugin.getInstance().getAudiences());
    }
}
