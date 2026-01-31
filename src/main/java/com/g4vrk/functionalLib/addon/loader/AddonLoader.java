package com.g4vrk.functionalLib.addon.loader;

import com.g4vrk.functionalLib.addon.Addon;
import com.g4vrk.functionalLib.addon.AddonClassLoader;
import com.g4vrk.functionalLib.addon.AddonDescription;
import com.g4vrk.functionalLib.logging.PluginLogger;
import com.g4vrk.functionalLib.logging.PluginLoggerFactory;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AddonLoader {

    private static PluginLogger logger;
    private final JavaPlugin plugin;
    private final File folder;
    private final Map<String, AddonContainer> addons = new ConcurrentHashMap<>();

    public AddonLoader(JavaPlugin plugin) {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder(), "addons");
        this.folder.mkdirs();

        logger = PluginLoggerFactory.getLogger(plugin);
    }

    public void loadAll() {
        File[] jars = folder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jars == null) return;

        for (File jar : jars) {
            try {
                loadAddon(jar);
            } catch (Throwable e) {
                logger.error("Ошибка при загрузке аддона {}", jar.getName(), e);
            }
        }
    }

    public void enableAll() {
        for (AddonContainer container : addons.values()) {
            try {
                container.addon().setEnabled(true);
            } catch (Throwable e) {
                logger.error("Произошла ошибка при включении аддона {}", container.addon().getName(), e);
            }
        }
    }

    public void disableAll() {
        for (AddonContainer container : addons.values()) {
            try {
                container.addon().setEnabled(false);
                container.classLoader().close();
            } catch (Throwable e) {
                logger.error("Произошла ошибка при выключении аддона {}", container.addon().getName(), e);
            }
        }
        addons.clear();
    }

    private void loadAddon(File jarFile) throws Exception {
        AddonDescription description = new AddonDescription(readFileContent(jarFile));

        File dataFolder = new File(folder, description.getName());
        if (!dataFolder.exists()) dataFolder.mkdirs();

        AddonClassLoader loader = new AddonClassLoader(jarFile, description);
        loader.init(plugin, dataFolder);

        addons.put(description.getName(), new AddonContainer(
                description.getName(),
                loader.getAddon(),
                description,
                loader
        ));
    }

    private YamlConfiguration readFileContent(File file) throws IOException {
        try(JarFile jar = new JarFile(file)) {
            JarEntry entry = jar.getJarEntry("addon.yml");

            if (entry == null) {
                throw new IllegalStateException("addon.yml не найден в " + jar.getName());
            }

            return YamlConfiguration.loadConfiguration(
                    new InputStreamReader(jar.getInputStream(entry), StandardCharsets.UTF_8)
            );
        }
    }

    record AddonContainer(
            String name,
            Addon addon,
            AddonDescription description,
            URLClassLoader classLoader
    ) {}
}
