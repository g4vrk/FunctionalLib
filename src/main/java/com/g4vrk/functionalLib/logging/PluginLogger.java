package com.g4vrk.functionalLib.logging;

import net.kyori.adventure.text.Component;

public interface PluginLogger {

    void log(LogLevel level, Component message);
    void log(LogLevel level, Component message, Throwable throwable);

    void log(LogLevel level, String message, Object... args);
    void log(LogLevel level, String message, Throwable throwable, Object... args);

    void info(Component message);
    void info(Component message, Throwable throwable);
    void info(String message, Object... args);

    void warn(Component message);
    void warn(Component message, Throwable throwable);
    void warn(String message, Object... args);

    void error(Component message);
    void error(Component message, Throwable throwable);
    void error(String message, Object... args);

    void debug(Component message);
    void debug(Component message, Throwable throwable);
    void debug(String message, Object... args);

    void setDebug(boolean value);
    boolean isDebug();
}

