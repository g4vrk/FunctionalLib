package com.g4vrk.functionalLib.actions.impl;

import com.g4vrk.functionalLib.FunctionalLibPlugin;
import com.g4vrk.functionalLib.actions.Action;
import com.g4vrk.functionalLib.actions.ActionContext;
import com.g4vrk.functionalLib.util.SendUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

public class SoundAction implements Action {

    @Override
    public void execute(ActionContext context, String args) {
        if (args == null || args.isBlank() || context.player() == null) return;

        String[] parts = args.split(";", 3);

        String soundStr = parts.length > 0 ? parts[0].trim() : "";
        float volume = parts.length > 1 ? parseFloat(parts[1]) : 1f;
        float pitch = parts.length > 2 ? parseFloat(parts[2]) : 1f;

        try {
            Sound sound;

            org.bukkit.Sound bukkitSound = null;
            try {
                bukkitSound = org.bukkit.Sound.valueOf(soundStr.toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }

            String keyStr;
            if (bukkitSound != null) {
                keyStr = bukkitSound.getKey().asString();
            } else {
                keyStr = soundStr.contains(":") ? soundStr : "minecraft:" + soundStr;
            }

            sound = Sound.sound(Key.key(keyStr), Sound.Source.MASTER, volume, pitch);

            SendUtil.playSound(context.player(), sound);

        } catch (Throwable e) {
            FunctionalLibPlugin.getInstance().getSLF4JLogger().error("Ошибка воспроизведения звука: {}", soundStr, e);
        }
    }

    private float parseFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return (float) 1.0;
        }
    }

    @Override
    public String getActionKey() {
        return "sound";
    }
}
