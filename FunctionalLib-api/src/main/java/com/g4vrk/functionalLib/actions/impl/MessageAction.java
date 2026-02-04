package com.g4vrk.functionalLib.actions.impl;

import com.g4vrk.functionalLib.actions.Action;
import com.g4vrk.functionalLib.actions.ActionContext;
import com.g4vrk.functionalLib.util.SendUtil;

public class MessageAction implements Action {

    @Override
    public void execute(ActionContext context, String args) {
        if (args == null || args.isBlank() || context.player() == null) return;

        SendUtil.sendMessage(context.player(), args);
    }

    @Override
    public String getActionKey() {
        return "message";
    }
}
