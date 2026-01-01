package com.g4vrk.functionalLib.configuration.adapter.impl;

import com.g4vrk.functionalLib.configuration.adapter.Adapter;
import com.g4vrk.functionalLib.util.text.TextUtil;
import net.kyori.adventure.text.Component;

public class ComponentAdapter implements Adapter<Component> {
    @Override
    public Class<Component> getType() {
        return Component.class;
    }

    @Override
    public Component adapt(Object value) {
        if (value instanceof String string) {
            return TextUtil.format(string);
        }

        return TextUtil.format(value.toString());
    }
}
