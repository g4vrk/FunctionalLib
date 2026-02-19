package com.g4vrk.functionalLib.menu;

import com.g4vrk.functionalLib.FunctionalLibAPI;
import com.g4vrk.functionalLib.task.Task;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

@Getter
public abstract class MenuItem {

    protected final Menu menu;
    protected final String key;

    public MenuItem(Menu menu, String key) {
        this.menu = menu;
        this.key = key;
    }

    public abstract ItemStack getItemStack(Object... placeholders);

    public void place(Inventory inventory, Player player, Object... placeholders) {
        ItemStack item = getItemStack(player, placeholders);
        int[] slots = getSlots();

        if (item == null || slots.length == 0) return;

        var task = new Task() {

            @Override
            protected void onFirstRun() {
                for (int slot : slots)
                    if (slot >= 0 && slot < inventory.getSize())
                        inventory.setItem(slot, item);
            }

            @Override
            protected void onRun() {
            }

            @Override
            protected void onFinish() {
            }

            @Override
            protected Plugin getPlugin() {
                return FunctionalLibAPI.getAPI().orElseThrow().getPlugin();
            }
        };

        task.async(true);
        task.run();
    }

    public void handleClick(Player player, InventoryClickEvent event) {}

    public abstract int[] getSlots();
}
