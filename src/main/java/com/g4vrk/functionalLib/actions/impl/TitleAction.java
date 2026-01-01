package com.g4vrk.functionalLib.actions.impl;

import com.g4vrk.functionalLib.actions.Action;
import com.g4vrk.functionalLib.actions.ActionContext;
import com.g4vrk.functionalLib.util.SendUtil;
import com.g4vrk.functionalLib.util.text.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.time.Duration;

public class TitleAction implements Action {

    @Override
    public void execute(ActionContext context, String args) {
        if (args == null || args.isBlank() || context.player() == null) return;

        String[] parts = args.split(";");

        Component title = TextUtil.format(parts[0]);
        Component subtitle = parts.length > 1 ? TextUtil.format(parts[1]) : Component.empty();

        SendUtil.sendTitle(context.player(), title, subtitle, 3, 1, 3);
//        context.plugin().getAudiences()
//                .player(context.player())
//                .showTitle(Title.title(title, subtitle, Title.Times.times(
//                Duration.ofMillis(100),
//                Duration.ofSeconds(3),
//                Duration.ofMillis(100)
//        )));
    }

    @Override
    public String getActionKey() {
        return "title";
    }
}
