package com.g4vrk.functionalLib.actions;

import org.bukkit.Keyed;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Action<T> extends Keyed {

    @NotNull Collection<String> getAliases();

    void execute(T context, String args);

    boolean runAsync();
}
