package com.g4vrk.functionalLib.actions.defaults.impl;

import com.g4vrk.functionalLib.actions.Action;
import com.g4vrk.functionalLib.actions.defaults.DefaultActionRegistry;
import com.g4vrk.functionalLib.actions.impl.player.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class DefaultPlayerActionRegistry implements DefaultActionRegistry<Player> {
    @Override
    public @NotNull Class<Player> getType() {
        return Player.class;
    }

    @Override
    public @NotNull Collection<Action<Player>> getActions() {
        // todo: add more
        return List.of(
                new ActionBarAction(),
                new CloseMenuAction(),
                new MessageAction(),
                new SoundAction(),
                new TitleAction(),
                new UpdateInventoryAction()
        );
    }
}
