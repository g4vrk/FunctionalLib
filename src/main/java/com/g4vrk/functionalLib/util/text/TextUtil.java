package com.g4vrk.functionalLib.util.text;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class TextUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .character('§')
            .build();

    private static final Map<Character, String> CHAR_TO_MINI_MESSAGE = new ConcurrentHashMap<>();

    static {
        CHAR_TO_MINI_MESSAGE.put('0', "<black>");
        CHAR_TO_MINI_MESSAGE.put('1', "<dark_blue>");
        CHAR_TO_MINI_MESSAGE.put('2', "<dark_green>");
        CHAR_TO_MINI_MESSAGE.put('3', "<dark_aqua>");
        CHAR_TO_MINI_MESSAGE.put('4', "<dark_red>");
        CHAR_TO_MINI_MESSAGE.put('5', "<dark_purple>");
        CHAR_TO_MINI_MESSAGE.put('6', "<gold>");
        CHAR_TO_MINI_MESSAGE.put('7', "<gray>");
        CHAR_TO_MINI_MESSAGE.put('8', "<dark_gray>");
        CHAR_TO_MINI_MESSAGE.put('9', "<blue>");
        CHAR_TO_MINI_MESSAGE.put('a', "<green>");
        CHAR_TO_MINI_MESSAGE.put('b', "<aqua>");
        CHAR_TO_MINI_MESSAGE.put('c', "<red>");
        CHAR_TO_MINI_MESSAGE.put('d', "<light_purple>");
        CHAR_TO_MINI_MESSAGE.put('e', "<yellow>");
        CHAR_TO_MINI_MESSAGE.put('f', "<white>");
        CHAR_TO_MINI_MESSAGE.put('k', "<obfuscated>");
        CHAR_TO_MINI_MESSAGE.put('l', "<bold>");
        CHAR_TO_MINI_MESSAGE.put('m', "<strikethrough>");
        CHAR_TO_MINI_MESSAGE.put('n', "<underlined>");
        CHAR_TO_MINI_MESSAGE.put('o', "<italic>");
        CHAR_TO_MINI_MESSAGE.put('r', "<reset>");
    }

    public static @NotNull Component format(@NotNull String rawText) {
        rawText = rawText.replaceAll("(?i)&#([A-F0-9]{6})", "<#$1>");

        StringBuilder stringBuilder = new StringBuilder(rawText.length());

        char[] chars = rawText.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '&' && i + 1 < chars.length) {
                String tag = CHAR_TO_MINI_MESSAGE.get(Character.toLowerCase(chars[i + 1]));

                if (tag != null) stringBuilder.append(tag);
                else stringBuilder.append('&').append(chars[i + 1]);

                i++;
            } else stringBuilder.append(chars[i]);
        }

        return MINI_MESSAGE.deserialize(stringBuilder.toString());
    }

    public static @NotNull String formatLegacy(@NotNull String rawText) {
        return LEGACY_SERIALIZER.serialize(LEGACY_SERIALIZER.deserialize(rawText));
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
}
