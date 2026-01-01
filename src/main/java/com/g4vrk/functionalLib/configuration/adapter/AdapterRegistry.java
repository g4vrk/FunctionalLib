package com.g4vrk.functionalLib.configuration.adapter;

import com.g4vrk.functionalLib.configuration.yaml.YamlValue;
import com.g4vrk.functionalLib.configuration.adapter.impl.*;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public final class AdapterRegistry {

    private static final Map<Class<?>, Adapter<?>> ADAPTERS = new ConcurrentHashMap<>();

    static {
        register(new FloatAdapter());
        register(new LongAdapter());
        register(new ShortAdapter());
        register(new DoubleAdapter());
        register(new IntegerAdapter());
        register(new StringAdapter());
        register(new BooleanAdapter());
        register(new ComponentAdapter());
        register(new ConfigurationSectionAdapter());
    }

    public static <T> void register(Adapter<T> adapter) {
        ADAPTERS.put(adapter.getType(), adapter);
    }

    @SuppressWarnings("unchecked")
    public static <T> Adapter<T> get(Class<T> type) {
        return (Adapter<T>) ADAPTERS.get(type);
    }

    public static <T> T getAs(YamlValue value, Class<T> type) {
        Adapter<T> adapter = get(type);

        if (adapter == null) {
            throw new IllegalStateException("No Adapter registered for type: " + type.getName());
        }

        return adapter.adapt(value);
    }
 }

