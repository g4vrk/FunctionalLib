package com.g4vrk.functionalLib.util;

import com.g4vrk.functionalLib.FunctionalLibPlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@UtilityClass
public class TaskUtil {

    private static final FunctionalLibPlugin plugin = FunctionalLibPlugin.getInstance();
    private static final BukkitScheduler BUKKIT_SCHEDULER = Bukkit.getScheduler();

    public static BukkitTask runSync(@NotNull Runnable runnable) {
        return BUKKIT_SCHEDULER.runTask(plugin, runnable);
    }

    public static BukkitTask runAsync(@NotNull Runnable runnable) {
        return BUKKIT_SCHEDULER.runTaskAsynchronously(plugin, runnable);
    }

    public static BukkitTask runLater(@NotNull Runnable runnable, long delayTicks) {
        return BUKKIT_SCHEDULER.runTaskLater(plugin, runnable, delayTicks);
    }

    public static BukkitTask runLaterAsync(@NotNull Runnable runnable, long delayTicks) {
        return BUKKIT_SCHEDULER.runTaskLaterAsynchronously(plugin, runnable, delayTicks);
    }

    public static BukkitTask runTimer(@NotNull Runnable runnable, long delayTicks, long periodTicks) {
        return BUKKIT_SCHEDULER.runTaskTimer(plugin, runnable, delayTicks, periodTicks);
    }

    public static BukkitTask runTimerAsync(@NotNull Runnable runnable, long delayTicks, long periodTicks) {
        return BUKKIT_SCHEDULER.runTaskTimerAsynchronously(plugin, runnable, delayTicks, periodTicks);
    }

    public static BukkitTask runRepeating(@NotNull Runnable runnable, long periodTicks) {
        return runTimer(runnable, 0L, periodTicks);
    }

    public static BukkitTask runRepeatingAsync(@NotNull Runnable runnable, long periodTicks) {
        return runTimerAsync(runnable, 0L, periodTicks);
    }

    public static BukkitTask runSync(@NotNull Consumer<BukkitRunnable> consumer) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                consumer.accept(this);
            }
        };
        return runnable.runTask(plugin);
    }

    public static BukkitTask runAsync(@NotNull Consumer<BukkitRunnable> consumer) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                consumer.accept(this);
            }
        };
        return runnable.runTaskAsynchronously(plugin);
    }

    public static BukkitTask runLater(@NotNull Consumer<BukkitRunnable> consumer, long delayTicks) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                consumer.accept(this);
            }
        };
        return runnable.runTaskLater(plugin, delayTicks);
    }

    public static BukkitTask runTimer(@NotNull Consumer<BukkitRunnable> consumer, long delayTicks, long periodTicks) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                consumer.accept(this);
            }
        };
        return runnable.runTaskTimer(plugin, delayTicks, periodTicks);
    }
}
