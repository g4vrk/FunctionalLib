package com.g4vrk.functionalLib.logging.impl;

import com.g4vrk.functionalLib.logging.LogLevel;
import com.g4vrk.functionalLib.logging.PluginLogger;
import com.g4vrk.functionalLib.util.text.TextUtil;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BukkitLogger implements PluginLogger {

    private final Logger logger;
    private boolean debug;

    public BukkitLogger(String name) {
        this(name, false);
    }

    public BukkitLogger(String name, boolean debug) {
        this.logger = LoggerFactory.getLogger(name);
        this.debug = debug;
    }

    @Override
    public void log(LogLevel level, Component message) {
        log(level, message, null);
    }

    @Override
    public void log(LogLevel level, Component message, Throwable throwable) {
        if (level == LogLevel.DEBUG && !debug) return;

        String plain = TextUtil.plain(String.valueOf(message));

        switch (level) {
            case INFO -> {
                if (throwable == null) logger.info(plain);
                else logger.info(plain, throwable);
            }
            case WARNING -> {
                if (throwable == null) logger.warn(plain);
                else logger.warn(plain, throwable);
            }
            case ERROR -> {
                if (throwable == null) logger.error(plain);
                else logger.error(plain, throwable);
            }
            case DEBUG -> {
                if (throwable == null) logger.debug(plain);
                else logger.debug(plain, throwable);
            }
        }
    }

    @Override
    public void log(LogLevel level, String message, Object... args) {
        if (level == LogLevel.DEBUG && !debug) return;

        switch (level) {
            case INFO -> logger.info(message, args);
            case WARNING -> logger.warn(message, args);
            case ERROR -> logger.error(message, args);
            case DEBUG -> logger.debug(message, args);
        }
    }

    @Override
    public void log(LogLevel level, String message, Throwable throwable, Object... args) {
        if (level == LogLevel.DEBUG && !debug) return;

        switch (level) {
            case INFO -> logger.info(message, args, throwable);
            case WARNING -> logger.warn(message, args, throwable);
            case ERROR -> logger.error(message, args, throwable);
            case DEBUG -> logger.debug(message, args, throwable);
        }
    }

    @Override
    public void info(Component message) {
        log(LogLevel.INFO, message);
    }

    @Override
    public void info(Component message, Throwable throwable) {
        log(LogLevel.INFO, message, throwable);
    }

    @Override
    public void info(String message, Object... args) {
        log(LogLevel.INFO, message, args);
    }

    @Override
    public void warn(Component message) {
        log(LogLevel.WARNING, message);
    }

    @Override
    public void warn(Component message, Throwable throwable) {
        log(LogLevel.WARNING, message, throwable);
    }

    @Override
    public void warn(String message, Object... args) {
        log(LogLevel.WARNING, message, args);
    }

    @Override
    public void error(Component message) {
        log(LogLevel.ERROR, message);
    }

    @Override
    public void error(Component message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable);
    }

    @Override
    public void error(String message, Object... args) {
        log(LogLevel.ERROR, message, args);
    }

    @Override
    public void debug(Component message) {
        log(LogLevel.DEBUG, message);
    }

    @Override
    public void debug(Component message, Throwable throwable) {
        log(LogLevel.DEBUG, message, throwable);
    }

    @Override
    public void debug(String message, Object... args) {
        log(LogLevel.DEBUG, message, args);
    }

    @Override
    public void setDebug(boolean value) {
        this.debug = value;
    }

    @Override
    public boolean isDebug() {
        return debug;
    }
}
