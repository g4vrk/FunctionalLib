package com.g4vrk.functionalLib.menu.types;

import com.g4vrk.functionalLib.menu.Menu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class StaticMenu extends Menu {

    private boolean initialized;

    protected final void initOnce() {
        if (initialized) return;

        inventory = createInventory();
        prepareInventory();
        initialized = true;
    }

    @Override
    public final void show(@NotNull Player player) {
        initOnce();
        player.openInventory(inventory);
    }
}
