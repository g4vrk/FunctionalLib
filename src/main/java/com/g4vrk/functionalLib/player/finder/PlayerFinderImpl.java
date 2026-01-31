package com.g4vrk.functionalLib.player.finder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

final class PlayerFinderImpl implements PlayerFinder {
    @Override
    public Optional<Player> find(String name) {
        if (name == null) return Optional.empty();
        return Optional.ofNullable(Bukkit.getPlayer(name));
    }

    @Override
    public Optional<Player> find(UUID uuid) {
        if (uuid == null) return Optional.empty();
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }

    @Override
    public boolean isOnline(String name) {
        return Bukkit.getPlayerExact(name) != null;
    }

    @Override
    public boolean isOnline(UUID uuid) {
        return Bukkit.getPlayer(uuid) != null;
    }

    @Override
    public Collection<? extends Player> findAll() {
        return Bukkit.getOnlinePlayers();
    }
}
