package com.g4vrk.functionalLib.util.formatter;

import lombok.Builder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Builder
public class TextFormatter {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private static final LegacyComponentSerializer LEGACY_SERIALIZER =
            LegacyComponentSerializer.builder()
                    .character(ChatColor.COLOR_CHAR)
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .build();

    private static final PlainTextComponentSerializer PLAIN_SERIALIZER =
            PlainTextComponentSerializer.plainText();

    @Builder.Default
    private TextFormatType type = TextFormatType.MIXED;

    @Builder.Default
    private boolean cache = true;

    private final Map<String, Component> componentCache = new ConcurrentHashMap<>();

    public @NotNull Component format(@NotNull String input) {
        if (!cache) return format0(input);
        return componentCache.computeIfAbsent(input, this::format0);
    }

    public @NotNull String legacy(@NotNull String input) {
        return LEGACY_SERIALIZER.serialize(format(input));
    }

    public @NotNull String plain(@NotNull String input) {
        return PLAIN_SERIALIZER.serialize(format(input));
    }

    private Component format0(String input) {
        return switch (type) {
            case MINI_MESSAGE -> formatMiniMessage(input);
            case LEGACY -> formatLegacy(input);
            case MIXED -> formatMixed(input);
        };
    }

    private Component formatMiniMessage(String input) {
        return MINI_MESSAGE.deserialize(input);
    }

    private Component formatLegacy(String input) {
        return LEGACY_SERIALIZER.deserialize(
                input.replace('&', ChatColor.COLOR_CHAR)
        );
    }

    private Component formatMixed(String input) {
        Component legacyComponent = formatLegacy(input);

        String mini = MINI_MESSAGE.serialize(legacyComponent);

        return MINI_MESSAGE.deserialize(mini);
    }
}
