package com.g4vrk.functionalLib.util;

import com.g4vrk.functionalLib.FunctionalLibPlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

@UtilityClass
public class LocationUtil {

    private static final String DEFAULT_WORLD = "world";

    public Location getFromSection(ConfigurationSection section) {
        if (!section.contains("world")) FunctionalLibPlugin.logger().error("Мир для локации не указан!", new NullPointerException());
        if (!section.contains("x")) {
            FunctionalLibPlugin.logger().error("X для локации не указан!", new NullPointerException());
        }
        if (!section.contains("y")) {
            FunctionalLibPlugin.logger().error("Y для локации не указан!", new NullPointerException());
        }
        if (!section.contains("z")) {
            FunctionalLibPlugin.logger().error("Z для локации не указан!", new NullPointerException());
        }

        return new Location(
                parseWorld(section.getString("world")),
                section.getDouble("x", 0.0D),
                section.getDouble("y", 0.0D),
                section.getDouble("z", 0.0D),
                (float) section.getDouble("yaw", 0.0F),
                (float) section.getDouble("pitch", 0.0F)
        );
    }

    public World parseWorld(String worldStr) {
        World world = Bukkit.getWorld(worldStr);
        if (world == null) {
            FunctionalLibPlugin.logger().error("Неизвестный мир {}", worldStr);
            return Bukkit.getWorld(DEFAULT_WORLD);
        }
        return world;
    }
}
