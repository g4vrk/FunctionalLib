package com.g4vrk.functionalLib.player.service;

import com.g4vrk.functionalLib.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Optional;

// idea - Tox_8729
public class PlayerInventoryService {
    private final Player player;

    public PlayerInventoryService(Player player) {
        this.player = player;
    }

    public Optional<Menu> getOpenedMenu() {
        return Optional.ofNullable((Menu) player.getOpenInventory().getTopInventory().getHolder());
    }

    public void clearInventory() {
        var inventory = player.getInventory();

        inventory.clear();
        inventory.setArmorContents(null);
        inventory.setExtraContents(null);
        inventory.setItemInOffHand(null);
    }

    public boolean hasSpace() {
        return player.getInventory().firstEmpty() != -1;
    }

    public void giveItem(ItemStack itemStack) {
        var added = player.getInventory().addItem(itemStack);

        if (!added.isEmpty()) {
            added.values().forEach(remain ->
                    player.getWorld().dropItem(player.getLocation(), remain));
        }
    }

    public boolean hasItem(Material material) {
        return player.getInventory().contains(material);
    }

    public boolean hasItem(ItemStack itemStack) {
        return player.getInventory().contains(itemStack);
    }
}
