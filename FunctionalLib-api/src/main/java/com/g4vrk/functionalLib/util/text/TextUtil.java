package com.g4vrk.functionalLib.util.text;

import com.g4vrk.functionalLib.util.formatter.TextFormatType;
import com.g4vrk.functionalLib.util.formatter.TextFormatter;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@UtilityClass @Deprecated
public class TextUtil {

    private static final TextFormatter TEXT_FORMATTER = TextFormatter.builder()
            .type(TextFormatType.MIXED)
            .cache(true)
            .build();

    public static @NotNull Component format(@NotNull String rawText) {
        return TEXT_FORMATTER.format(rawText);
    }

    public static @NotNull String formatLegacy(@NotNull String rawText) {
        return TEXT_FORMATTER.legacy(rawText);
    }

    public static @NotNull List<Component> formatList(@NotNull List<String> rawList) {
        return rawList.stream()
                .map(TextUtil::format)
                .toList();
    }

    public static @NotNull List<String> formatListLegacy(@NotNull List<String> rawList) {
        return rawList.stream()
                .map(TextUtil::formatLegacy)
                .toList();
    }

    public static @NotNull String plain(@NotNull String rawText) {
        return TEXT_FORMATTER.plain(rawText);
    }

    public static @NotNull List<String> plainList(@NotNull List<String> rawList) {
        return rawList.stream()
                .map(TextUtil::plain)
                .toList();
    }
}
