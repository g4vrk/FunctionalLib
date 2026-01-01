package com.g4vrk.functionalLib.configuration.yaml;

import com.g4vrk.functionalLib.FunctionalLibPlugin;
import com.g4vrk.functionalLib.configuration.Configuration;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class YamlConfig implements Configuration {

    private final @NotNull MemorySection section;
    private final @Nullable MemorySection defaultSection;

    private final @NotNull File configFile;
    private final @Nullable InputStream defaultInputStream;

    private final Map<String, YamlValue> values = new ConcurrentHashMap<>();
    private final Map<String, YamlValue> defaultValues = new ConcurrentHashMap<>();

    public YamlConfig(File configFile) throws InvalidConfigurationException, IOException {
        this(configFile, null);
    }

    public YamlConfig(@NotNull File configFile, @Nullable InputStream defaultInputStream) throws InvalidConfigurationException, IOException {
        this.configFile = configFile;
        this.defaultInputStream = defaultInputStream;

        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.load(configFile);
        this.section = yamlConfiguration;

        this.section.getValues(true).forEach((k, v) -> values.put(k, new YamlValue(v)));

        if (defaultInputStream != null) {
            YamlConfiguration defaultYamlConfiguration = new YamlConfiguration();
            defaultYamlConfiguration.loadFromString(new String(defaultInputStream.readAllBytes()));
            this.defaultSection = defaultYamlConfiguration;

            this.defaultSection.getValues(true).forEach((s, o) -> defaultValues.put(s, new YamlValue(o)));
        } else {
            this.defaultSection = null;
        }
    }

    private YamlValue getOrDefault(String path) {
        YamlValue value = values.get(path);
        if (value == null) {
            value = defaultValues.get(path);
        }
        return value != null ? value : new YamlValue(null);
    }

    @Override
    public List<String> getStringList(String path) {
        return getOrDefault(path).getStringList();
    }

    @Override
    public List<String> getStringList(String path, List<String> def) {
        return getOrDefault(path).getStringList(def);
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        return getOrDefault(path).getIntegerList();
    }

    @Override
    public List<Integer> getIntegerList(String path, List<Integer> def) {
        return getOrDefault(path).getIntegerList(def);
    }

    @Override
    public List<Component> getComponentList(String path) {
        return getOrDefault(path).getComponentList();
    }

    @Override
    public List<Component> getComponentList(String path, List<Component> def) {
        return getOrDefault(path).getComponentList(def);
    }

    @Override
    public <T> T getAs(String path, Class<T> type) {
        return values.get(path).getAs(type);
    }

    @Override
    public <T> T getAs(String path, Class<T> type, T def) {
        return values.get(path).getAs(type, def);
    }

    @Override
    public <T> List<T> getAsList(String path, Class<T> listType) {
        return getOrDefault(path).getAsList(listType);
    }

    @Override
    public <T> List<T> getAsList(String path, Class<T> listType, List<T> def) {
        return getOrDefault(path).getAsList(listType, def);
    }

    @Override
    public boolean getBoolean(String path) {
        return getOrDefault(path).getBoolean();
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return getOrDefault(path).getBoolean(def);
    }

    @Override
    public int getInt(String path) {
        return getOrDefault(path).getInt();
    }

    @Override
    public int getInt(String path, int def) {
        return getOrDefault(path).getInt(def);
    }

    @Override
    public double getDouble(String path) {
        return getOrDefault(path).getDouble();
    }

    @Override
    public double getDouble(String path, double def) {
        return getOrDefault(path).getDouble(def);
    }

    @Override
    public float getFloat(String path) {
        return getOrDefault(path).getFloat();
    }

    @Override
    public float getFloat(String path, float def) {
        return getOrDefault(path).getFloat(def);
    }

    @Override
    public short getShort(String path) {
        return getOrDefault(path).getShort();
    }

    @Override
    public short getShort(String path, short def) {
        return getOrDefault(path).getShort(def);
    }

    @Override
    public long getLong(String path) {
        return getOrDefault(path).getLong();
    }

    @Override
    public long getLong(String path, long def) {
        return getOrDefault(path).getLong(def);
    }

    @Override
    public Component getComponent(String path) {
        return getOrDefault(path).getComponent();
    }

    @Override
    public Component getComponent(String path, Component def) {
        return getOrDefault(path).getComponent(def);
    }

    @Override
    public String getString(String path) {
        return getOrDefault(path).getString();
    }

    @Override
    public String getString(String path, String def) {
        return getOrDefault(path).getString(def);
    }

    @Override
    public ConfigurationSection getConfigurationSection(String path) {
        return getOrDefault(path).getConfigurationSection();
    }

    @Override
    public ConfigurationSection getConfigurationSection(String path, ConfigurationSection def) {
        return getOrDefault(path).getConfigurationSection(def);
    }

    @Override
    public void save(File file) {
        try {
            ((YamlConfiguration) section).save(file);
        } catch (IOException e) {
            FunctionalLibPlugin.getInstance().getSLF4JLogger().error("Ошибка при сохранении конфигурации", e);
        }
    }

    @Override
    public void set(String path, Object value) {
        section.set(path, value);
        values.put(path, new YamlValue(value));
    }

    @Override
    public boolean contains(String path) {
        return values.containsKey(path);
    }
}
