package com.g4vrk.functionalLib;

import com.g4vrk.functionalLib.actions.ActionExecutor;
import com.g4vrk.functionalLib.actions.impl.*;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class FunctionalLibPlugin extends JavaPlugin {

    @Getter
    private static FunctionalLibPlugin instance;

    private BukkitAudiences audiences;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        audiences = BukkitAudiences.create(this);

        ActionExecutor.registerAction(new ActionBarAction());
        ActionExecutor.registerAction(new CloseMenuAction());
        ActionExecutor.registerAction(new ConsoleAction());
        ActionExecutor.registerAction(new MessageAction());
        ActionExecutor.registerAction(new SoundAction());
        ActionExecutor.registerAction(new TitleAction());
        ActionExecutor.registerAction(new UpdateInventoryAction());
    }

    @Override
    public void onDisable() {
        if (this.audiences != null) {
            this.audiences.close();
        }
    }
}
