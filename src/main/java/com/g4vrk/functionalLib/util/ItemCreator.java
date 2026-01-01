package com.g4vrk.functionalLib.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.g4vrk.functionalLib.util.text.ReplaceUtil;
import com.g4vrk.functionalLib.util.text.TextUtil;
import lombok.Builder;
import lombok.Singular;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public final class ItemCreator {

    private ItemStack cached;
    private String material;
    private String name;
    @Singular(value = "addLoreLine")
    private List<String> lore;

    @Builder.Default
    private boolean allFlags = false;

    @Builder.Default
    private boolean unbreakable = false;

    private boolean glow;
    private Integer customModelData;
    private Color color;
    private Map<Enchantment, Integer> enchantments;
    private List<PotionEffect> effects;

    private boolean hideAttributes;
    private boolean hideEnchantments;
    private boolean hideEffects;

    private Object[] placeholders;

    public ItemStack toItemStack() {
        if (cached != null && (placeholders == null || placeholders.length == 0)) {
            return cached.clone();
        }

        ItemStack itemStack = createItem();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return itemStack;

        if (name != null) {
            meta.displayName(TextUtil.format(ReplaceUtil.format(name, placeholders)));
        }
        if (lore != null && !lore.isEmpty()) {
            meta.lore(TextUtil.formatList(ReplaceUtil.formatStringList(lore, placeholders)));
        }
        if (allFlags) meta.addItemFlags(ItemFlag.values());
        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (unbreakable) meta.setUnbreakable(true);
        if (customModelData != null) meta.setCustomModelData(customModelData);
        if (hideAttributes) meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if (hideEnchantments) meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (hideEffects) meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        if (color != null) {
            if (meta instanceof LeatherArmorMeta leatherMeta) leatherMeta.setColor(color);
            if (meta instanceof PotionMeta potionMeta) potionMeta.setColor(color);
        }
        if (enchantments != null) enchantments.forEach((enchant, level) -> meta.addEnchant(enchant, level, true));

        itemStack.setItemMeta(meta);

        if (placeholders == null || placeholders.length == 0) cached = itemStack.clone();
        return itemStack;
    }

    private ItemStack createItem() {
        if (material == null) return new ItemStack(Material.STONE);

        if (material.toLowerCase().startsWith("basehead-")) {
            return fromBase64(material.substring("basehead-".length()));
        }

        Material mat = Material.matchMaterial(material);
        if (mat == null) mat = Material.STONE;
        return new ItemStack(mat);
    }

    private ItemStack fromBase64(String base64) {
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

    public static ItemCreator fromConfiguration(ConfigurationSection section) {
        ItemCreator.ItemCreatorBuilder builder = ItemCreator.builder()
                .material(section.getString("material"))
                .name(section.getString("name"))
                .allFlags(section.getBoolean("all_flags", false))
                .unbreakable(section.getBoolean("unbreakable", false))
                .glow(section.getBoolean("glow", false));

        if (section.contains("custom_model_data")) {
            builder.customModelData(section.getInt("custom_model_data"));
        }

        if (section.contains("lore")) {
            section.getStringList("lore").forEach(builder::addLoreLine);
        }

        if (section.contains("color")) {
            builder.color(Color.fromRGB(section.getInt("color")));
        }

        if (section.contains("enchantments")) {
            Map<Enchantment, Integer> enchants = section.getConfigurationSection("enchantments").getValues(false)
                    .entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> Enchantment.getByName(e.getKey()),
                            e -> (Integer) e.getValue()
                    ));
            builder.enchantments(enchants);
        }

        builder.hideAttributes(section.getBoolean("hide_attributes", false));
        builder.hideEnchantments(section.getBoolean("hide_enchantments", false));
        builder.hideEffects(section.getBoolean("hide_effects", false));

        return builder.build();
    }
}
