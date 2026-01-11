package com.g4vrk.functionalLib.actions.impl;

import com.g4vrk.functionalLib.actions.Action;
import com.g4vrk.functionalLib.actions.ActionContext;
import com.g4vrk.functionalLib.util.SendUtil;

public class ActionBarAction implements Action {

    @Override
    public void execute(ActionContext context, String args) {
        if (args == null || args.isBlank() || context.player() == null) return;

        SendUtil.sendActionBar(context.player(), args);
//        FunctionalLibPlugin plugin = context.plugin();
//
//        Component component = TextUtil.format(args);
//
//        plugin.getAudiences()
//                .player(context.player())
//                .sendActionBar(component);
    }

    @Override
    public String getActionKey() {
        return "actionbar";
    }
}
