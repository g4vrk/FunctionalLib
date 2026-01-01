package com.g4vrk.functionalLib.actions.impl;

import com.g4vrk.functionalLib.actions.Action;
import com.g4vrk.functionalLib.actions.ActionContext;
public class CloseMenuAction implements Action {

    @Override
    public void execute(ActionContext context, String args) {
        if (context.player() == null) return;

        context.player().closeInventory();
    }

    @Override
    public String getActionKey() {
        return "close-menu";
    }
}
