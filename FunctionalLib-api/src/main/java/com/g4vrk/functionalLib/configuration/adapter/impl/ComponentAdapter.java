package com.g4vrk.functionalLib.configuration.adapter.impl;

import com.g4vrk.functionalLib.configuration.adapter.Adapter;
import com.g4vrk.functionalLib.util.text.TextUtil;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.stream.Collectors;

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

        if (value instanceof List<?> list) {
            String joined = list.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("\n"));

            return TextUtil.format(joined);
        }

        return TextUtil.format(String.valueOf(value));
    }
}
