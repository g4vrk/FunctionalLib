package com.g4vrk.functionalLib.menu;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Getter
public abstract class MenuItem {

    protected final Menu menu;
    protected final String key;

    public MenuItem(Menu menu, String key) {
        this.menu = menu;
        this.key = key;
    }

    public abstract ItemStack getItemStack(Object... placeholders);

    public void place(Inventory inventory, Object... placeholders) {
        ItemStack item = getItemStack(placeholders);
        int[] slots = getSlots();
        for (int slot : slots) {
            if (slot >= 0 && slot < inventory.getSize()) {
                inventory.setItem(slot, item);
            }
        }
    }

    public void handleClick(Player player, InventoryClickEvent event) {}

    public abstract int[] getSlots();
}
