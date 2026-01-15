package com.g4vrk.functionalLib;

import com.g4vrk.functionalLib.menu.listener.MenuClickListener;
import com.g4vrk.functionalLib.util.MinecraftVersion;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@Getter
public final class FunctionalLibPlugin extends BasePlugin {

    @Getter
    private static FunctionalLibPlugin instance;
    private static Logger log;
    private final MinecraftVersion minecraftVersion = MinecraftVersion.current();

    private BukkitAudiences audiences;

    public FunctionalLibPlugin() {
        instance = this;
        log = getSLF4JLogger();
    }

    @Override
    public void onEnabling() {
        audiences = BukkitAudiences.create(this);
        new MenuClickListener().registerEvents(this);
        if (MinecraftVersion.below(MinecraftVersion.v1_16_5)) {
            log.error("Вы используете не поддерживаемую версию! ({}.{}.{})",
                    minecraftVersion.getMajor(),
                    minecraftVersion.getMinor(),
                    minecraftVersion.getPatch());
            log.error("Пожалуйста, используйте 1.18.2 и выше для стабильной работы!");
        }
    }

    @Override
    public void onDisable() {
        if (this.audiences != null) {
            this.audiences.close();
        }
    }

    public static @Nullable Logger logger() {
        if (log == null) {
            getInstance().getSLF4JLogger().error("Логгер не был инициализирован. Используйте logger() после инициализации плагина FunctionalLib");
        }
        return log;
    }
}
