package com.g4vrk.functionalLib.actions.impl;

import com.g4vrk.functionalLib.FunctionalLibPlugin;
import com.g4vrk.functionalLib.actions.Action;
import com.g4vrk.functionalLib.actions.ActionContext;
import com.g4vrk.functionalLib.util.SendUtil;
import com.g4vrk.functionalLib.util.text.TextUtil;
import net.kyori.adventure.text.Component;

public class MessageAction implements Action {

    @Override
    public void execute(ActionContext context, String args) {
        if (args == null || args.isBlank() || context.player() == null) return;

        SendUtil.sendMessageFormat(context.player(), args);
//        FunctionalLibPlugin plugin = context.plugin();
//
//        Component component = TextUtil.format(args);
//
//        plugin.getAudiences()
//                .player(context.player())
//                .sendMessage(component);
    }

    @Override
    public String getActionKey() {
        return "message";
    }
}
