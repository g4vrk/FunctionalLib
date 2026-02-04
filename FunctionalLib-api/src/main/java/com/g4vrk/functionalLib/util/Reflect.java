package com.g4vrk.functionalLib.util;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Reflect {

    private static final Map<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Method> METHOD_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Field> FIELD_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<>();

    public static Class<?> getClass(String name) {
        return CLASS_CACHE.computeIfAbsent(name, key -> {
            try {
                return Class.forName(key);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Class not found: " + key, e);
            }
        });
    }

    public static <T> T newInstance(Class<T> clazz, Object... args) {
        try {
            Constructor<?> constructor = getConstructor(clazz, types(args));
            return clazz.cast(constructor.newInstance(args));
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot create instance of " + clazz.getName(), e);
        }
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) {
        String key = clazz.getName() + Arrays.toString(params);

        return CONSTRUCTOR_CACHE.computeIfAbsent(key, k -> {
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                Class<?>[] constructorParams = constructor.getParameterTypes();

                if (constructorParams.length != params.length) {
                    continue;
                }

                boolean match = true;
                for (int i = 0; i < params.length; i++) {
                    if (!constructorParams[i].isAssignableFrom(params[i])) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    constructor.setAccessible(true);
                    return constructor;
                }
            }

            throw new IllegalStateException(
                    "Constructor not found: " + clazz.getName() + Arrays.toString(params)
            );
        });
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... params) {
        String key = clazz.getName() + "#" + name + Arrays.toString(params);

        return METHOD_CACHE.computeIfAbsent(key, k -> {
            try {
                return clazz.getMethod(name, params);
            } catch (NoSuchMethodException e) {
                try {
                    Method m = clazz.getDeclaredMethod(name, params);
                    m.setAccessible(true);
                    return m;
                } catch (NoSuchMethodException ignored) {
                    throw new IllegalStateException(
                            "Method not found: " + clazz.getName() + "." + name, e
                    );
                }
            }
        });
    }

    public static Object invoke(Object instance, String name, Object... args) {
        Method method = getMethod(
                instance.getClass(),
                name,
                types(args)
        );

        try {
            return method.invoke(instance, args);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Cannot invoke method " + name + " on " + instance.getClass().getName(), e
            );
        }
    }

    public static Object invokeStatic(Class<?> clazz, String name, Object... args) {
        Method method = getMethod(clazz, name, types(args));

        try {
            return method.invoke(null, args);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Cannot invoke static method " + name + " on " + clazz.getName(), e
            );
        }
    }

    public static Field getField(Class<?> clazz, String name) {
        String key = clazz.getName() + "#" + name;

        return FIELD_CACHE.computeIfAbsent(key, k -> {
            Class<?> search = clazz;

            while (search != null) {
                try {
                    Field f = search.getDeclaredField(name);
                    f.setAccessible(true);
                    return f;
                } catch (NoSuchFieldException ignored) {
                    search = search.getSuperclass();
                }
            }

            throw new IllegalStateException(
                    "Field not found: " + clazz.getName() + "." + name
            );
        });
    }

    public static Object getFieldValue(Object instance, String name) {
        try {
            return getField(instance.getClass(), name).get(instance);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot read field " + name, e);
        }
    }

    public static void setFieldValue(Object instance, String name, Object value) {
        try {
            getField(instance.getClass(), name).set(instance, value);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot set field " + name, e);
        }
    }

    public static boolean hasClass(String clazzName) {
        try {
            Class.forName(clazzName);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static boolean hasMethod(Class<?> clazz, String name, Class<?>... params) {
        try {
            clazz.getMethod(name, params);
            return true;
        } catch (NoSuchMethodException ignored) {
            try {
                clazz.getDeclaredMethod(name, params);
                return true;
            } catch (NoSuchMethodException ignored2) {
                return false;
            }
        }
    }

    public static boolean hasConstructor(Class<?> clazz, Class<?>... params) {
        try {
            clazz.getConstructor(params);
            return true;
        } catch (NoSuchMethodException ignored) {
            try {
                clazz.getDeclaredConstructor(params);
                return true;
            } catch (NoSuchMethodException ignored2) {
                return false;
            }
        }
    }

    public static boolean hasField(Class<?> clazz, String name) {
        Class<?> search = clazz;

        while (search != null) {
            try {
                search.getDeclaredField(name);
                return true;
            } catch (NoSuchFieldException ignored) {
                search = search.getSuperclass();
            }
        }
        return false;
    }

    private static Class<?>[] types(Object[] args) {
        if (args == null || args.length == 0) {
            return new Class<?>[0];
        }

        Class<?>[] types = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i] == null ? Object.class : args[i].getClass();
        }
        return types;
    }
}
