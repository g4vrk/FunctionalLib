package com.g4vrk.functionalLib.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.g4vrk.functionalLib.util.text.ReplaceUtil;
import com.g4vrk.functionalLib.util.text.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record ItemBuilder(ItemStack itemStack) {
    private static final Material DEFAULT_MATERIAL = Material.DIRT;
    private static final String BASEHEAD_PREFIX = "basehead-";

    public ItemBuilder name(@Nullable String name) {
        this.itemStack.editMeta(itemMeta -> itemMeta.displayName(name != null ? TextUtil.format(name) : Component.empty()));
        return this;
    }

    public ItemBuilder lore(@Nullable List<String> list) {
        if (list == null || list.isEmpty()) return this;
        this.itemStack.editMeta(itemMeta -> itemMeta.lore(list.stream().map(TextUtil::format).toList()));
        return this;
    }

    public ItemBuilder material(Material material) {
        if (material == null) return this;
        this.itemStack.setType(material);
        return this;
    }

    public ItemBuilder allFlags(boolean allFlags) {
        if (!allFlags) return this;
        this.itemStack.editMeta(itemMeta -> itemMeta.addItemFlags(ItemFlag.values()));
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        if (!unbreakable) return this;
        this.itemStack.editMeta(itemMeta -> itemMeta.setUnbreakable(true));
        return this;
    }

    public ItemBuilder glow(boolean glow) {
        if (!glow) return this;
        this.itemStack.editMeta(itemMeta -> {
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        });
        return this;
    }

    public ItemBuilder customModelData(int modelData) {
        this.itemStack.editMeta(itemMeta -> itemMeta.setCustomModelData(modelData));
        return this;
    }

    public ItemBuilder color(Color color) {
        if (color == null) return this;
        this.itemStack.editMeta(itemMeta -> {
            if (itemMeta instanceof LeatherArmorMeta leatherMeta) leatherMeta.setColor(color);
            if (itemStack instanceof PotionMeta potionMeta) potionMeta.setColor(color);
        });
        return this;
    }

    public ItemBuilder hideAttributes(boolean hideAttributes) {
        if (!hideAttributes) return this;
        this.itemStack.editMeta(itemMeta -> itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES));
        return this;
    }

    public ItemBuilder hideEnchantments(boolean hideEnchantments) {
        if (!hideEnchantments) return this;
        this.itemStack.editMeta(itemMeta -> itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS));
        return this;
    }

    public ItemBuilder hideEffects(boolean hideEffects) {
        if (!hideEffects) return this;
        this.itemStack.editMeta(itemMeta -> itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS));
        return this;
    }

    public ItemBuilder enchantments(Map<Enchantment, Integer> map) {
        if (map == null || map.isEmpty()) return this;
        this.itemStack.editMeta(itemMeta -> map.forEach((enchantment, level) -> itemMeta.addEnchant(enchantment, level, true)));
        return this;
    }

    public ItemBuilder placeholders(Object[] placeholders) {
        if (placeholders == null || placeholders.length == 0) return this;
        this.itemStack.editMeta(itemMeta -> {
            if (itemMeta.hasDisplayName()) {
                itemMeta.displayName(ReplaceUtil.format(itemMeta.displayName(), placeholders));
            }
            if (itemMeta.hasLore()) {
                itemMeta.lore(ReplaceUtil.formatComponentList(itemMeta.lore(), placeholders));
            }
        });
        return this;
    }


    public ItemBuilder(@Nullable ItemStack itemStack) {
        this.itemStack = itemStack != null ? itemStack : new ItemStack(DEFAULT_MATERIAL);
    }

    public ItemBuilder(@Nullable Material material) {
        this(new ItemStack(material != null ? material : DEFAULT_MATERIAL));
    }

    public ItemBuilder(@NotNull String materialStr) {
        this(createItemStack(materialStr));
    }

    public static Material parseMaterial(String materialStr) {
        try {
            if (materialStr.startsWith(BASEHEAD_PREFIX)) {
                return Material.PLAYER_HEAD;
            }
            return Material.valueOf(materialStr);
        } catch (IllegalArgumentException e) {
            return Material.AIR;
        }
    }

    public static ItemStack createItemStack(@Nullable String materialStr) {
        if (materialStr == null) return new ItemStack(DEFAULT_MATERIAL);

        if (materialStr.toLowerCase().startsWith(BASEHEAD_PREFIX)) {
            return fromBase64(materialStr.replace(BASEHEAD_PREFIX, "").trim());
        }

        Material material = parseMaterial(materialStr);
        return new ItemStack(material);
    }

    public static ItemStack fromBase64(String base64) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        head.editMeta(meta -> {
            if (meta instanceof SkullMeta skullMeta) {
                PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
                profile.setProperty(new ProfileProperty("textures", base64));
                skullMeta.setPlayerProfile(profile);
            }
        });
        return head;
    }

    public static ItemBuilder fromConfiguration(ConfigurationSection section) {
        ItemBuilder builder = new ItemBuilder(section.getString("material", DEFAULT_MATERIAL.name()));

        if (section.contains("display_name")) builder.name(section.getString("display-name"));
        if (section.contains("model_data")) builder.customModelData(section.getInt("model_data"));
        if (section.contains("lore")) builder.lore(section.getStringList("lore"));
        if (section.contains("color")) builder.color(ColorUtil.fromHex(section.getString("color")));
        if (section.contains("enchantments")) {
            Map<Enchantment, Integer> enchants = section.getConfigurationSection("enchantments").getValues(false)
                    .entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> Enchantment.getByName(e.getKey()),
                            e -> (Integer) e.getValue()
                    ));
            builder.enchantments(enchants);
        }

        builder.unbreakable(section.getBoolean("unbreakable", false));
        builder.glow(section.getBoolean("glow", false));
        builder.allFlags(section.getBoolean("all_flags", false));
        builder.hideAttributes(section.getBoolean("hide_attributes", false));
        builder.hideEnchantments(section.getBoolean("hide_enchantments", false));
        builder.hideEffects(section.getBoolean("hide_effects", false));

        return builder;
    }
}
