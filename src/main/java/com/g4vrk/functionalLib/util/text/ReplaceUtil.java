package com.g4vrk.functionalLib.util.text;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@UtilityClass
public class ReplaceUtil {

    public static @NotNull Component format(@NotNull Component rawText, @Nullable Object[] placeholders) {
        if (placeholders == null || placeholders.length == 0 || placeholders.length % 2 != 0) {
            return rawText;
        }

        Component finalText = rawText;
        for (int i = 0; i < placeholders.length - 1; i += 2) {
            String key = String.valueOf(placeholders[i]);
            String value = String.valueOf(placeholders[i + 1]);

            finalText = finalText.replaceText(builder ->
                    builder.matchLiteral(key).replacement(value)
            );
        }
        return finalText;
    }

    public static @NotNull String format(@NotNull String rawText, @Nullable Object[] placeholders) {
        if (placeholders == null || placeholders.length == 0 || placeholders.length % 2 != 0) {
            return rawText;
        }

        String finalText = rawText;
        for (int i = 0; i < placeholders.length - 1; i += 2) {
            String key = String.valueOf(placeholders[i]);
            String value = String.valueOf(placeholders[i + 1]);

            finalText = finalText.replace(key, value);
        }
        return finalText;
    }

    public static @NotNull List<String> formatStringList(@NotNull List<String> rawList, @Nullable Object[] placeholders) {
        return rawList.stream()
                .map(string -> format(string, placeholders))
                .toList();
    }

    public static @NotNull List<Component> formatComponentList(@NotNull List<Component> rawList, @Nullable Object[] placeholders) {
        return rawList.stream()
                .map(component -> format(component, placeholders))
                .toList();
    }

}
