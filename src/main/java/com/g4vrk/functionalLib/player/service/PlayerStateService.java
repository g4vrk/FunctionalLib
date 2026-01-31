package com.g4vrk.functionalLib.player.service;

import com.g4vrk.functionalLib.menu.Menu;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.Optional;

// idea - Tox_8729
public class PlayerStateService {
    private final Player player;

    public PlayerStateService(Player player) {
        this.player = player;
    }

    public void setMaxHealth() {
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

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

    public void setFly(boolean fly) {
        player.setAllowFlight(fly);
        player.setFlying(fly);
    }

    public void restoreStates() {
        setMaxHealth();
        feelFoodLevel();
        clearEffects();
    }
}
