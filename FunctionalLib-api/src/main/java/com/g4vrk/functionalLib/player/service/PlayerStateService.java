package com.g4vrk.functionalLib.player.service;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

// idea - Tox_8729
public class PlayerStateService {
    private final Player player;

    public PlayerStateService(Player player) {
        this.player = player;
    }

    public void setMaxHealth() {
        var attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double maxHealth = attribute != null ? attribute.getValue() : 20d;

        player.setHealth(maxHealth);
        player.setFireTicks(0);
    }

    public void feelFoodLevel() {
        player.setFoodLevel(20);
        player.setSaturation(20f);
    }

    public void clearEffects() {
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
    }

    public void resetGameMode() {
        player.setGameMode(GameMode.SURVIVAL);
    }

    public void setFly(boolean fly) {
        player.setAllowFlight(fly);
        player.setFlying(fly);
    }

    public void restoreStates() {
        resetGameMode();
        setMaxHealth();
        feelFoodLevel();
        clearEffects();
    }
}
