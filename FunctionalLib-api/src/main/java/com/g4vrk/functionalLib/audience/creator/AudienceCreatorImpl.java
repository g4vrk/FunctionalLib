package com.g4vrk.functionalLib.audience.creator;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

final class AudienceCreatorImpl implements AudienceCreator {

    private final BukkitAudiences audiences;

    AudienceCreatorImpl(@NotNull BukkitAudiences audiences) {
        this.audiences = audiences;
    }

    @Override
    public Optional<Audience> getAudience(CommandSender sender) {
        if (sender == null) return Optional.empty();
        return Optional.of(audiences.sender(sender));
    }

    @Override
    public Optional<Audience> getAudience(Player player) {
        if (player == null) return Optional.empty();
        return Optional.of(audiences.player(player));
    }

    @Override
    public Optional<Audience> getAudience(String name) {
        Player player = Bukkit.getPlayer(name);
        return player != null ? Optional.of(audiences.player(player)) : Optional.empty();
    }

    @Override
    public Optional<Audience> getAudience(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player != null ? Optional.of(audiences.player(player)) : Optional.empty();
    }

    @Override
    public Collection<? extends Audience> getAllAudiences() {
        return Bukkit.getOnlinePlayers().stream()
                .map(audiences::player)
                .toList();
    }
}
