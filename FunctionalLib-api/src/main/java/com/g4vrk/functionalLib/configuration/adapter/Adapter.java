package com.g4vrk.functionalLib.configuration.adapter;

public interface Adapter<T> {

    Class<T> getType();

    T adapt(Object value);
}

