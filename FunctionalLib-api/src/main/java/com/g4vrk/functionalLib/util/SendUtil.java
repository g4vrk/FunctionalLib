package com.g4vrk.functionalLib.util;

import com.g4vrk.functionalLib.FunctionalLibAPI;
import com.g4vrk.functionalLib.util.text.TextUtil;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;

@UtilityClass @Deprecated
public class SendUtil {

    private final BukkitAudiences AUDIENCES = FunctionalLibAPI.getAPI().orElseThrow(NullPointerException::new).getAudiences();

    public void sendMessage(Player player, Component component) {
        AUDIENCES.player(player).sendMessage(component);
    }

    public void sendMessage(Player player, String string) {
        AUDIENCES.player(player).sendMessage(TextUtil.format(string));
    }

    public void sendMessage(CommandSender sender, Component component) {
        AUDIENCES.sender(sender).sendMessage(component);
    }

    public void sendMessage(CommandSender sender, String string) {
        AUDIENCES.sender(sender).sendMessage(TextUtil.format(string));
    }

    public void sendActionBar(Player player, Component component) {
        AUDIENCES.player(player).sendActionBar(component);
    }

    public void sendActionBar(Player player, String string) {
        AUDIENCES.player(player).sendActionBar(TextUtil.format(string));
    }

    public void sendTitle(Player player, Component title, Component subtitle, int fadeIn, int stay, int fadeOut) {
        AUDIENCES.player(player).showTitle(
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
        AUDIENCES.player(player).showTitle(
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
        AUDIENCES.player(player).playSound(sound);
    }

    public void playSound(Player player, org.bukkit.Sound sound) {
        AUDIENCES.player(player).playSound(
                Sound.sound(
                        Key.key(sound.getKey().asString()),
                        Sound.Source.PLAYER,
                        1.0F,
                        1.0F
                )
        );
    }
}
