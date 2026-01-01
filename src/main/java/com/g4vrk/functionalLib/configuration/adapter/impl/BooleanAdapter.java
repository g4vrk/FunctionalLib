package com.g4vrk.functionalLib.configuration.adapter.impl;

import com.g4vrk.functionalLib.configuration.adapter.Adapter;

public class BooleanAdapter implements Adapter<Boolean> {
    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public Boolean adapt(Object value) {
        if (value instanceof Boolean b) {
            return b;
        }

        return Boolean.valueOf(value.toString());
    }
}
