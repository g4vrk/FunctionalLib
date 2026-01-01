package com.g4vrk.functionalLib.configuration.adapter.impl;

import com.g4vrk.functionalLib.configuration.adapter.Adapter;

public class LongAdapter implements Adapter<Long> {
    @Override
    public Class<Long> getType() {
        return Long.class;
    }

    @Override
    public Long adapt(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }

        return Long.valueOf(value.toString());
    }
}
