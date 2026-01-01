package com.g4vrk.functionalLib.configuration.adapter.impl;

import com.g4vrk.functionalLib.configuration.adapter.Adapter;

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

        return String.valueOf(value);
    }
}
