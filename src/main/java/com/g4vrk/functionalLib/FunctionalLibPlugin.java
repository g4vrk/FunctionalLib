package com.g4vrk.functionalLib;

import com.g4vrk.functionalLib.menu.listener.MenuClickListener;
import com.g4vrk.functionalLib.util.MinecraftVersion;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public final class FunctionalLibPlugin extends BasePlugin {

    @Getter
    private static FunctionalLibPlugin instance;
    private static Logger log;

    public static final String NAME = "FunctionalLib";

    private BukkitAudiences audiences;

    @Override
    public void onEnabling() {
        instance = this;
        BasePlugin.setLib(this);
        log = getSLF4JLogger();
        audiences = BukkitAudiences.create(this);
        new MenuClickListener().registerEvents(this);

        MinecraftVersion minecraftVersion = MinecraftVersion.current();
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

    public static @NotNull Logger logger() {
        if (log == null) {
            return log = LoggerFactory.getLogger(NAME);
        }
        return log;
    }
}
