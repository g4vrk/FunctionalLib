package com.g4vrk.functionalLib.audience.sender;

import com.g4vrk.functionalLib.audience.creator.AudienceCreator;
import com.g4vrk.functionalLib.util.formatter.TextFormatType;
import com.g4vrk.functionalLib.util.formatter.TextFormatter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.function.Consumer;

public class Sender {
    private final Audience audience;

    private final TextFormatter textFormatter = TextFormatter.builder()
            .type(TextFormatType.MIXED)
            .cache(true)
            .build();

    public Sender(Player player) {
        this.audience = AudienceCreator.creator()
                .getAudience(player)
                .orElseGet(Audience::empty);
    }

    public Sender(CommandSender sender) {
        this.audience = AudienceCreator.creator()
                .getAudience(sender)
                .orElseGet(Audience::empty);
    }

    public boolean isEmpty() {
        return audience == Audience.empty();
    }

    public void ifPresent(Consumer<Sender> consumer) {
        if (!isEmpty()) {
            consumer.accept(this);
        }
    }

    public void ifPresentOrElse(Consumer<Sender> present, Runnable runnable) {
        if (!isEmpty()) {
            present.accept(this);
        } else {
            runnable.run();
        }
    }

    public Audience audience() {
        return audience;
    }

    public void sendMessage(Component message) {
        audience.sendMessage(message);
    }

    public void sendMessage(String message) {
        audience.sendMessage(textFormatter.format(message));
    }

    public void sendMessages(String... messages) {
        for (String message : messages) {
            audience.sendMessage(textFormatter.format(message));
        }
    }

    public void sendMessages(Component... components) {
        for (Component component : components) {
            audience.sendMessage(component);
        }
    }

    public void sendActionBar(Component message) {
        audience.sendActionBar(message);
    }

    public void sendActionBar(String message) {
        audience.sendActionBar(textFormatter.format(message));
    }

    public void sendTitle(Component title, Component subtitle, int fadeIn, int stay, int fadeOut) {
        audience.showTitle(
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

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        audience.showTitle(
                Title.title(
                        textFormatter.format(title),
                        textFormatter.format(subtitle),
                        Title.Times.times(
                                Duration.ofMillis(fadeIn),
                                Duration.ofMillis(stay),
                                Duration.ofMillis(fadeOut)
                        )
                )
        );
    }

    public void sendTitle(Title title) {
        audience.showTitle(title);
    }

    public void playSound(Sound sound) {
        audience.playSound(sound);
    }

    public void playSound(org.bukkit.Sound sound) {
        audience.playSound(
                Sound.sound(
                        Key.key(sound.getKey().asString()),
                        Sound.Source.PLAYER,
                        1.0F,
                        1.0F
                )
        );
    }

    public void showBossBar(BossBar bossBar) {
        audience.showBossBar(bossBar);
    }

    public void hideBossBar(BossBar bossBar) {
        audience.hideBossBar(bossBar);
    }

    public void clearActionBar() {
        audience.sendActionBar(Component.empty());
    }

    public void clearTitle() {
        audience.clearTitle();
    }
}
