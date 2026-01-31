package com.g4vrk.functionalLib.player.finder;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

// thx to Tox_8729
public interface PlayerFinder {

    Optional<Player> find(String name);
    Optional<Player> find(UUID uuid);

    boolean isOnline(String name);
    boolean isOnline(UUID uuid);

    Collection<? extends Player> findAll();

    static PlayerFinder finder() {
        return new PlayerFinderImpl();
    }
}
