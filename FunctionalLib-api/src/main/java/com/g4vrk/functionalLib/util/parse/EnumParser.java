package com.g4vrk.functionalLib.util.parse;

import java.util.Optional;

public final class EnumParser {

    private EnumParser() {
    }

    public static <E extends Enum<E>> Optional<E> parse(Class<E> enumClass, String value) {
        if (value == null) return Optional.empty();

        try {
            return Optional.of(Enum.valueOf(enumClass, value));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    public static <E extends Enum<E>> Optional<E> parseIgnoreCase(Class<E> enumClass, String value) {
        if (value == null) return Optional.empty();

        for (E constant : enumClass.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(value)) {
                return Optional.of(constant);
            }
        }
        return Optional.empty();
    }

    public static <E extends Enum<E>> boolean canCast(Class<E> enumClass, String value) {
        return parse(enumClass, value).isPresent();
    }

    public static <E extends Enum<E>> boolean canCastIgnoreCase(Class<E> enumClass, String value) {
        return parseIgnoreCase(enumClass, value).isPresent();
    }
}
