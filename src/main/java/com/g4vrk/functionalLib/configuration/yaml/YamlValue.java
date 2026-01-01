package com.g4vrk.functionalLib.configuration.yaml;

import com.g4vrk.functionalLib.configuration.adapter.AdapterRegistry;
import com.g4vrk.functionalLib.util.text.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record YamlValue(@Nullable Object value) {

    public boolean isNull() {
        return value == null;
    }

    public <T> T getAs(Class<T> type) {
        return AdapterRegistry.getAs(this, type);
    }

    public <T> T getAs(Class<T> type, T def) {
        if (value == null) return def;
        return getAs(type);
    }

    public String getString() {
        return getAs(String.class);
    }

    public String getString(String def) {
        return value == null ? def : getString();
    }

    public Component getComponent() {
        return getAs(Component.class);
    }

    public Component getComponent(Component def) {
        return value == null ? def : getComponent();
    }

    public ConfigurationSection getConfigurationSection() {
        return getAs(ConfigurationSection.class);
    }

    public ConfigurationSection getConfigurationSection(ConfigurationSection def) {
        return value == null ? def : getConfigurationSection();
    }

    public int getInt() {
        return getAs(Integer.class);
    }

    public int getInt(int def) {
        return value == null ? def : getInt();
    }

    public boolean getBoolean() {
        return getAs(Boolean.class);
    }

    public boolean getBoolean(boolean def) {
        return value == null ? def : getBoolean();
    }

    public double getDouble() {
        return getAs(Double.class);
    }

    public double getDouble(double def) {
        return value == null ? def : getDouble();
    }

    public long getLong() {
        return getAs(Long.class);
    }

    public long getLong(long def) {
        return value == null ? def : getLong();
    }

    public float getFloat() {
        return getAs(Float.class);
    }

    public float getFloat(float def) {
        return value == null ? def : getFloat();
    }

    public short getShort() {
        return getAs(Short.class);
    }

    public short getShort(short def) {
        return value == null ? def : getShort();
    }

    public List<String> getStringList() {
        return getAsList(String.class);
    }

    public List<String> getStringList(List<String> def) {
        return value == null ? def : getStringList();
    }

    public List<Component> getComponentList() {
        return getAsList(String.class).stream()
                .map(TextUtil::format)
                .toList();
    }

    public List<Component> getComponentList(List<Component> def) {
        return value == null ? def : getComponentList();
    }

    public List<Integer> getIntegerList() {
        return getAsList(Integer.class);
    }

    public List<Integer> getIntegerList(List<Integer> def) {
        return value == null ? def : getIntegerList();
    }

    public <T> List<T> getAsList(Class<T> type) {
        if (value == null) return Collections.emptyList();

        if (!(value instanceof List<?> list)) {
            throw new IllegalStateException("Value is not a list: " + value);
        }

        return list.stream()
                .map(v -> new YamlValue(v).getAs(type))
                .collect(Collectors.toList());
    }

    public <T> List<T> getAsList(Class<T> type, List<T> def) {
        return value == null ? def : getAsList(type);
    }
}
