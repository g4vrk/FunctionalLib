package com.g4vrk.functionalLib.actions.impl.player;

import com.g4vrk.functionalLib.actions.AbstractAction;
import com.g4vrk.functionalLib.util.SendUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("deprecation")
public class ActionBarAction extends AbstractAction<Player> {

    public ActionBarAction() {
        super(new NamespacedKey("functionallib", "action-bar"), List.of("actionbar"));
    }

    @Override
    public void execute(Player player, String args) {
        if (args == null || args.isBlank() || player == null) return;

        SendUtil.sendActionBar(player, args);
    }
}
