package com.g4vrk.functionalLib.actions;

public interface Action {
    void execute(ActionContext context, String args);

    default boolean runAsync() {
        return false;
    }

    String getActionKey();
}
