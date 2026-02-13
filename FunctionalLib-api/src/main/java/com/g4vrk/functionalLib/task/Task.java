package com.g4vrk.functionalLib.task;

import lombok.Getter;
import lombok.AccessLevel;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

@Getter
public abstract class Task implements Runnable {

    private boolean cancelled;
    private boolean running;

    @Getter(AccessLevel.NONE)
    private final BukkitScheduler scheduler = Bukkit.getScheduler();

    private int taskId = -1;
    private long delayTicks = 0;
    private long repeatTicks = 0;
    private long durationTicks = -1;
    private boolean async = false;

    protected abstract void onFirstRun();
    protected abstract void onRun();
    protected abstract void onFinish();

    public Task delay(long ticks) {
        this.delayTicks = ticks;
        return this;
    }

    public Task repeatEvery(long ticks) {
        this.repeatTicks = ticks;
        return this;
    }

    public Task forDuration(long ticks) {
        this.durationTicks = ticks;
        return this;
    }

    public Task async(boolean value) {
        this.async = value;
        return this;
    }

    public void cancel() {
        if (cancelled) return;
        cancelled = true;
        running = false;

        if (taskId != -1) scheduler.cancelTask(taskId);

        onFinish();
    }

    private void finish() {
        running = false;
        onFinish();
    }

    @Override
    public void run() {
        start();
    }

    public void start() {
        if (cancelled) return;
        running = true;

        Runnable runnable = new Runnable() {
            private boolean firstRun = true;
            private long ticksPassed = 0;

            @Override
            public void run() {
                if (cancelled) return;

                if (firstRun) {
                    onFirstRun();
                    firstRun = false;
                }

                onRun();
                ticksPassed++;

                boolean finished = (durationTicks > 0 && ticksPassed >= durationTicks) ||
                        (repeatTicks <= 0 && durationTicks <= 0 && delayTicks <= 0);

                if (finished) {
                    finish();
                }
            }
        };

        Plugin plugin = getPlugin();

        if (repeatTicks > 0) {
            if (async)
                taskId = scheduler.runTaskTimerAsynchronously(plugin, runnable, delayTicks, repeatTicks).getTaskId();
            else
                taskId = scheduler.runTaskTimer(plugin, runnable, delayTicks, repeatTicks).getTaskId();
        } else if (delayTicks > 0) {
            if (async)
                taskId = scheduler.runTaskLaterAsynchronously(plugin, runnable, delayTicks).getTaskId();
            else
                taskId = scheduler.runTaskLater(plugin, runnable, delayTicks).getTaskId();
        } else {
            if (async)
                taskId = scheduler.runTaskAsynchronously(plugin, runnable).getTaskId();
            else
                taskId = scheduler.runTask(plugin, runnable).getTaskId();
        }
    }

    protected abstract Plugin getPlugin();
}
