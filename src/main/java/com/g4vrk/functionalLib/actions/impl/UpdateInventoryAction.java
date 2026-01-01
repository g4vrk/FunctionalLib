package com.g4vrk.functionalLib.actions.impl;

import com.g4vrk.functionalLib.actions.Action;
import com.g4vrk.functionalLib.actions.ActionContext;

public class UpdateInventoryAction implements Action {

    @Override
    public void execute(ActionContext context, String args) {
        if (context.player() == null) return;

        context.player().updateInventory();
    }

    @Override
    public String getActionKey() {
        return "update-inventory";
    }
}
