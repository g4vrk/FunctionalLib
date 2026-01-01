package com.g4vrk.functionalLib.configuration.adapter.impl;

import com.g4vrk.functionalLib.configuration.adapter.Adapter;

public class DoubleAdapter implements Adapter<Double> {
    @Override
    public Class<Double> getType() {
        return Double.class;
    }

    @Override
    public Double adapt(Object value) {
        if (value instanceof Double d) {
            return d;
        }

        return Double.valueOf(value.toString());
    }
}
