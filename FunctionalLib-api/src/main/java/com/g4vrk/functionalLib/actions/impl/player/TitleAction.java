package com.g4vrk.functionalLib.actions.impl.player;

import com.g4vrk.functionalLib.actions.AbstractAction;
import com.g4vrk.functionalLib.util.SendUtil;
import com.g4vrk.functionalLib.util.formatter.TextFormatType;
import com.g4vrk.functionalLib.util.formatter.TextFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class TitleAction extends AbstractAction<Player> {

    private final TextFormatter textFormatter = TextFormatter.builder()
            .cache(true)
            .type(TextFormatType.MIXED)
            .build();

    public TitleAction() {
        super(new NamespacedKey("functionallib", "title"));
    }

    @Override
    public void execute(Player player, String args) {
        if (args == null || args.isBlank() || player == null) return;

        String[] parts = args.split(";");

        Component title = textFormatter.format(parts[0]);
        Component subtitle = parts.length > 1 ? textFormatter.format(parts[1]) : Component.empty();

        SendUtil.sendTitle(player, title, subtitle, 3, 1, 3);
    }
}
