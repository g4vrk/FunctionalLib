package com.g4vrk.functionalLib.util.text;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class TextUtil {

    private static final PlainTextComponentSerializer PLAIN_SERIALIZER =
            PlainTextComponentSerializer.plainText();

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .character(ChatColor.COLOR_CHAR)
            .build();

    private static final Map<String, Component> CACHED_COMPONENTS = new ConcurrentHashMap<>();
    private static final Map<String, String> CACHED_STRINGS = new ConcurrentHashMap<>();

    public static String legacyToMiniMessage(String rawText) {
        Component component = LEGACY_SERIALIZER.deserialize(rawText.replace('&', ChatColor.COLOR_CHAR));
        return MINI_MESSAGE.serialize(component);
    }

    public static @NotNull Component format(@NotNull String rawText) {
        return CACHED_COMPONENTS.computeIfAbsent(rawText, string -> MINI_MESSAGE.deserialize(legacyToMiniMessage(string)));
    }

    public static @NotNull String formatLegacy(@NotNull String rawText) {
        return CACHED_STRINGS.computeIfAbsent(rawText, string -> LEGACY_SERIALIZER.serialize(format(string)));
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
        return PLAIN_SERIALIZER.serialize(format(rawText));
    }

    public static @NotNull String plain(@NotNull Component component) {
        return PLAIN_SERIALIZER.serialize(component);
    }

    public static @NotNull List<String> plainList(@NotNull List<String> rawList) {
        return rawList.stream()
                .map(TextUtil::plain)
                .toList();
    }

    public static @NotNull List<String> plainComponentList(@NotNull List<Component> components) {
        return components.stream()
                .map(TextUtil::plain)
                .toList();
    }
}
