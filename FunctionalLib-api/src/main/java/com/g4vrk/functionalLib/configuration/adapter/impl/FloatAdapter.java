package com.g4vrk.functionalLib.configuration.adapter.impl;

import com.g4vrk.functionalLib.configuration.adapter.Adapter;

public class FloatAdapter implements Adapter<Float> {
    @Override
    public Class<Float> getType() {
        return Float.class;
    }

    @Override
    public Float adapt(Object value) {
        if (value instanceof Number number) {
            return number.floatValue();
        }

        return Float.valueOf(value.toString());
    }
}
