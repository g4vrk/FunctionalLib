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

    public String serialize(Location location) {
        if (location == null) return null;

        return location.getWorld().getName() + ";" +
                location.getX() + ";" +
                location.getY() + ";" +
                location.getZ() + ";" +
                location.getYaw() + ";" +
                location.getPitch();
    }

    public Location deserialize(String value) {
        if (value == null || value.isEmpty()) return null;

        try {
            String[] parts = value.split(";");
            if (parts.length < 4) {
                FunctionalLibPlugin.logger().error("Неверный формат локации: {}", value);
                return null;
            }

            World world = parseWorld(parts[0]);

            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);

            float yaw = parts.length > 4 ? Float.parseFloat(parts[4]) : 0f;
            float pitch = parts.length > 5 ? Float.parseFloat(parts[5]) : 0f;

            return new Location(world, x, y, z, yaw, pitch);

        } catch (Exception e) {
            FunctionalLibPlugin.logger().error("Ошибка при десериализации локации: {}", value, e);
            return null;
        }
    }
}
