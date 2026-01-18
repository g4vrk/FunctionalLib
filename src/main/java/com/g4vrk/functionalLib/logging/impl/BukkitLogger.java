package com.g4vrk.functionalLib.logging.impl;

import com.g4vrk.functionalLib.logging.PluginLogger;
import com.g4vrk.functionalLib.logging.LogLevel;
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
    public void log(LogLevel logLevel, Component message) {
        if (logLevel == LogLevel.DEBUG && !debug) return;
        switch (logLevel) {
            case INFO -> logger.info(TextUtil.plain(message));
            case WARNING -> logger.warn(TextUtil.plain(message));
            case ERROR -> logger.error(TextUtil.plain(message));
            case DEBUG -> logger.debug(TextUtil.plain(message));
        }
    }

    @Override
    public void log(LogLevel logLevel, Component message , Throwable throwable) {
        if (logLevel == LogLevel.DEBUG && !debug) return;
        switch (logLevel) {
            case INFO -> logger.info(TextUtil.plain(message), throwable);
            case WARNING -> logger.warn(TextUtil.plain(message), throwable);
            case ERROR -> logger.error(TextUtil.plain(message), throwable);
            case DEBUG -> logger.debug(TextUtil.plain(message), throwable);
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
    public void error(Component message) {
        log(LogLevel.ERROR, message);
    }

    @Override
    public void error(Component message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable);
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
    public void setDebug(boolean value) {
        this.debug = value;
    }

    @Override
    public boolean isDebug() {
        return debug;
    }

    @Override
    public void debug(Component message) {
        log(LogLevel.DEBUG, message);
    }

    @Override
    public void debug(Component message, Throwable throwable) {
        log(LogLevel.DEBUG, message, throwable);
    }
}
