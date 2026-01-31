package com.g4vrk.functionalLib.addon;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

@Getter
public class AddonClassLoader extends URLClassLoader {

    private final Addon addon;
    private final AddonDescription description;

    public AddonClassLoader(File jarFile, AddonDescription description) throws Exception {
        super(new URL[]{jarFile.toURI().toURL()}, AddonClassLoader.class.getClassLoader());
        this.description = description;

        Class<?> clazz = Class.forName(description.getMainClass(), true, this);
        if (!Addon.class.isAssignableFrom(clazz)) {
            close();
            throw new IllegalStateException("Главный класс аддона не наследует AbstractAddon");
        }

        this.addon = (Addon) clazz.getDeclaredConstructor().newInstance();
    }

    public void init(JavaPlugin plugin, File dataFolder) {
        addon.init(plugin, dataFolder, description);
    }
}
