package com.g4vrk.functionalLib.configuration.adapter.impl;

import com.g4vrk.functionalLib.configuration.adapter.Adapter;

public class ShortAdapter implements Adapter<Short> {
    @Override
    public Class<Short> getType() {
        return Short.class;
    }

    @Override
    public Short adapt(Object value) {
        if (value instanceof Number number) {
            return number.shortValue();
        }

        return Short.valueOf(value.toString());
    }
}
