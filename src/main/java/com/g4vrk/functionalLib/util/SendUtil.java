package com.g4vrk.functionalLib.util;

import com.g4vrk.functionalLib.FunctionalLibPlugin;
import com.g4vrk.functionalLib.util.text.TextUtil;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.time.Duration;

@UtilityClass
public class SendUtil {

    private final BukkitAudiences audiences = FunctionalLibPlugin.getInstance().getAudiences();

    public void sendMessage(Player player, Component component) {
        audiences.player(player).sendMessage(component);
    }

    public void sendMessageFormat(Player player, String string) {
        audiences.player(player).sendMessage(TextUtil.format(string));
    }

    public void sendActionBar(Player player, Component component) {
        audiences.player(player).sendActionBar(component);
    }

    public void sendActionBarFormat(Player player, String string) {
        audiences.player(player).sendActionBar(TextUtil.format(string));
    }

    public void sendTitle(Player player, Component title, Component subtitle, int fadeIn, int stay, int fadeOut) {
        audiences.player(player).showTitle(
                Title.title(
                        title,
                        subtitle,
                        Title.Times.times(
                                Duration.ofMillis(fadeIn),
                                Duration.ofMillis(stay),
                                Duration.ofMillis(fadeOut)
                        )
                )
        );
    }

    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        audiences.player(player).showTitle(
                Title.title(
                        TextUtil.format(title),
                        TextUtil.format(subtitle),
                        Title.Times.times(
                                Duration.ofMillis(fadeIn),
                                Duration.ofMillis(stay),
                                Duration.ofMillis(fadeOut)
                        )
                )
        );
    }

    public void playSound(Player player, Sound sound) {
        audiences.player(player).playSound(sound);
    }

    public void playSound(Player player, org.bukkit.Sound sound) {
        audiences.player(player).playSound(
                Sound.sound(
                        Key.key(sound.getKey().asString()),
                        Sound.Source.PLAYER,
                        1.0F,
                        1.0F
                )
        );
    }
}
