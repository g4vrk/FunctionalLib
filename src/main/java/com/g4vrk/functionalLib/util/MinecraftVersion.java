package com.g4vrk.functionalLib.util;

import com.g4vrk.functionalLib.FunctionalLibPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public enum MinecraftVersion {

    v1_16(1, 16, 0),
    v1_16_1(1, 16, 1),
    v1_16_2(1, 16, 2),
    v1_16_3(1, 16, 3),
    v1_16_4(1, 16, 4),
    v1_16_5(1, 16, 5),

    v1_17(1, 17, 0),
    v1_17_1(1, 17, 1),

    v1_18(1, 18, 0),
    v1_18_1(1, 18, 1),
    v1_18_2(1, 18, 2),

    v1_19(1, 19, 0),
    v1_19_1(1, 19, 1),
    v1_19_2(1, 19, 2),
    v1_19_3(1, 19, 3),
    v1_19_4(1, 19, 4),

    v1_20(1, 20, 0),
    v1_20_1(1, 20, 1),
    v1_20_2(1, 20, 2),
    v1_20_3(1, 20, 3),
    v1_20_4(1, 20, 4),
    v1_20_6(1, 20, 6),

    v1_21(1, 21, 0),
    v1_21_1(1, 21, 1),
    v1_21_3(1, 21, 3),
    v1_21_4(1, 21, 4),
    v1_21_6(1, 21, 6),
    v1_21_7(1, 21, 7),
    v1_21_8(1, 21, 8),
    v1_21_11(1, 21, 11);

    private static final Pattern VERSION_PATTERN =
            Pattern.compile("(\\d+)\\.(\\d+)(?:\\.(\\d+))?");

    private static final MinecraftVersion CURRENT;
    private static final Platform PLATFORM;

    static {
        CURRENT = detectVersion();
        PLATFORM = detectPlatform();
    }
    private final int major;
    private final int minor;
    private final int patch;

    MinecraftVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public static MinecraftVersion current() {
        return CURRENT;
    }

    public static boolean atLeast(MinecraftVersion version) {
        return CURRENT.isAtLeast(version);
    }

    public static boolean below(MinecraftVersion version) {
        return CURRENT.isBelow(version);
    }

    public boolean isAtLeast(MinecraftVersion other) {
        return compareTo(other) >= 0;
    }

    public boolean isBelow(MinecraftVersion other) {
        return compareTo(other) < 0;
    }

    public static boolean isPaper() {
        return PLATFORM == Platform.PAPER || PLATFORM == Platform.PURPUR;
    }

    public static boolean isPurpur() {
        return PLATFORM == Platform.PURPUR;
    }

    public static boolean isSpigot() {
        return PLATFORM == Platform.SPIGOT;
    }

    public static Platform platform() {
        return PLATFORM;
    }

    private static MinecraftVersion detectVersion() {
        String version = Bukkit.getBukkitVersion();

        Matcher matcher = VERSION_PATTERN.matcher(version);
        if (!matcher.find()) {
            throw new IllegalStateException("Cannot detect Minecraft version: " + version);
        }

        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        int patch = matcher.group(3) != null
                ? Integer.parseInt(matcher.group(3))
                : 0;
        FunctionalLibPlugin.logger().info("Версия сервера помечена как {}.{}.{}", major, minor, patch);

        MinecraftVersion closest = null;

        for (MinecraftVersion v : values()) {
            if (v.major == major && v.minor == minor) {
                if (v.patch == patch) {
                    return v;
                }
                if (v.patch <= patch) {
                    closest = v;
                }
            }
        }

        if (closest != null) {
            return closest;
        }

        throw new IllegalStateException(
                "Unsupported Minecraft version: " + major + "." + minor + "." + patch
        );
    }

    private static Platform detectPlatform() {
        try {
            Class.forName("org.purpurmc.purpur.PurpurConfig");
            FunctionalLibPlugin.logger().info("Ядро сервера помечено как Purpur.");
            return Platform.PURPUR;
        } catch (Throwable ignored) {
        }

        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            FunctionalLibPlugin.logger().info("Ядро сервера помечено как Paper.");
            return Platform.PAPER;
        } catch (Throwable ignored) {
        }

        FunctionalLibPlugin.logger().info("Ядро сервера помечено как Spigot (Legacy).");
        return Platform.SPIGOT;
    }

    public enum Platform {
        SPIGOT,
        PAPER,
        PURPUR
    }
}
