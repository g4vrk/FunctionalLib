package com.g4vrk.functionalLib.configuration.adapter.impl;

import com.g4vrk.functionalLib.configuration.adapter.Adapter;

import java.util.List;
import java.util.stream.Collectors;

public class StringAdapter implements Adapter<String> {
    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String adapt(Object value) {
        if (value instanceof String string) {
            return string;
        }

        if (value instanceof List<?> list) {
            return list.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("\n"));
        }
        return String.valueOf(value);
    }
}
