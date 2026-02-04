package com.g4vrk.functionalLib.menu;

import com.g4vrk.functionalLib.util.text.TextUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class Menu implements InventoryHolder {
    protected final Map<Integer, MenuItem> items = new ConcurrentHashMap<>();

    @Setter
    protected Inventory inventory;
    @Setter
    protected Menu parent;
    protected Player viewer;

    public Menu() {
    }

    public Menu(@NotNull Menu parent) {
        this.parent = parent;
    }

    protected Inventory createInventory() {
        return Bukkit.createInventory(this, getSize() % 9 == 0 ? getSize() : (getSize() / 9) * 9, TextUtil.format(getTitle() != null ? getTitle() : ""));
    }

    protected void setInventoryItems(@Nullable Object... placeholders) {
        items.forEach((slot, menuItem) -> inventory.setItem(slot, inventory.getItem(slot) == null ? menuItem.getItemStack(placeholders) : inventory.getItem(slot)));
    }

    protected void prepareInventory(Player player) {
        inventory.clear();
        items.clear();

        loadItems();
        setInventoryItems((Object) null);
    }

    public void refresh(Player player) {
        prepareInventory(player);
    }

    public void show(@NotNull Player player) {
        inventory = createInventory();
        viewer = player;
        prepareInventory(player);

        player.openInventory(inventory);
    }

    public void showParent(@NotNull Player player) {
        if (parent == null) return;
        parent.show(player);
    }

    public void onClick(@NotNull InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        int slot = event.getSlot();

        MenuItem item = items.get(slot);
        if (item == null) return;

        item.handleClick(player, event);
    }

    public void onClose(@NotNull InventoryCloseEvent event) {
    }

    public void onDrag(@NotNull InventoryDragEvent event) {
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;

        player.setItemOnCursor(null);
    }

    public void addItem(int slot, MenuItem item) {
        items.put(slot, item);
    }

    public void addItem(MenuItem item) {
        for (int slot: item.getSlots()) {
            items.put(slot, item);
        }
    }

    protected abstract int getSize();
    protected abstract @Nullable String getTitle();
    protected abstract void loadItems();
}
