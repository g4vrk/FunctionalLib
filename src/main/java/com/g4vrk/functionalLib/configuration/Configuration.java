package com.g4vrk.functionalLib.configuration;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.List;

public interface Configuration {

    <T> T getAs(String path, Class<T> type);
    <T> T getAs(String path, Class<T> type, T def);

    <T> List<T> getAsList(String path, Class<T> listType);
    <T> List<T> getAsList(String path, Class<T> listType, List<T> def);

    boolean getBoolean(String path);
    boolean getBoolean(String path, boolean def);

    int getInt(String path);
    int getInt(String path, int def);

    double getDouble(String path);
    double getDouble(String path, double def);

    float getFloat(String path);
    float getFloat(String path, float def);

    short getShort(String path);
    short getShort(String path, short def);

    long getLong(String path);
    long getLong(String path, long def);

    Component getComponent(String path);
    Component getComponent(String path, Component def);

    String getString(String path);
    String getString(String path, String def);

    ConfigurationSection getConfigurationSection(String path);
    ConfigurationSection getConfigurationSection(String path, ConfigurationSection def);

    List<String> getStringList(String path);
    List<String> getStringList(String path, List<String> def);

    List<Integer> getIntegerList(String path);
    List<Integer> getIntegerList(String path, List<Integer> def);

    List<Component> getComponentList(String path);
    List<Component> getComponentList(String path, List<Component> def);

    void save(File file);
    void set(String path, Object value);

    boolean contains(String path);
}
