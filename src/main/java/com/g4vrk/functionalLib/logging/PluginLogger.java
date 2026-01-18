package com.g4vrk.functionalLib.logging;

import net.kyori.adventure.text.Component;

public interface PluginLogger {
    void log(LogLevel logLevel, Component message);
    void log(LogLevel logLevel, Component message, Throwable throwable);

    void info(Component message);
    void info(Component message, Throwable throwable);

    void error(Component message);
    void error(Component message, Throwable throwable);

    void warn(Component message);
    void warn(Component message, Throwable throwable);

    void setDebug(boolean value);
    boolean isDebug();

    void debug(Component message);
    void debug(Component message, Throwable throwable);
}
