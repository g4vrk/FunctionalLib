package com.g4vrk.functionalLib.logging.impl;

import com.g4vrk.functionalLib.logging.LogLevel;
import com.g4vrk.functionalLib.logging.PluginLogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class PaperLogger implements PluginLogger {

    private final ComponentLogger logger;
    private boolean debug;

    public PaperLogger(String name) {
        this(name, false);
    }

    public PaperLogger(String name, boolean debug) {
        this.logger = ComponentLogger.logger(name);
        this.debug = debug;
    }

    @Override
    public void log(LogLevel logLevel, Component message) {
        if (logLevel == LogLevel.DEBUG && !debug) return;
        switch (logLevel) {
            case INFO -> logger.info(message);
            case WARNING -> logger.warn(message);
            case ERROR -> logger.error(message);
            case DEBUG -> logger.debug(message);
        }
    }

    @Override
    public void log(LogLevel logLevel, Component message , Throwable throwable) {
        if (logLevel == LogLevel.DEBUG && !debug) return;
        switch (logLevel) {
            case INFO -> logger.info(message, throwable);
            case WARNING -> logger.warn(message, throwable);
            case ERROR -> logger.error(message, throwable);
            case DEBUG -> logger.debug(message, throwable);
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
