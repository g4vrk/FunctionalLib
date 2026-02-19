package com.g4vrk.functionalLib.actions.defaults;

import com.g4vrk.functionalLib.actions.Action;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface DefaultActionRegistry<T> {

    @NotNull Class<T> getType();

    @NotNull Collection<Action<T>> getActions();
}
