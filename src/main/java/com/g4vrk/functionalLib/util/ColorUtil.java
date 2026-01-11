package com.g4vrk.functionalLib.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Color;

@UtilityClass
public class ColorUtil {

    public Color fromHex(String hex) {
        hex = hex.startsWith("#") ? hex.substring(1) : hex;
        int rgb = Integer.parseInt(hex, 16);
        return Color.fromRGB(
                (rgb >> 16) & 0xFF,
                (rgb >> 8) & 0xFF,
                rgb & 0xFF
        );
    }
}