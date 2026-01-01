package com.g4vrk.functionalLib.util.text;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ItemStackSerializer {

    public static String itemToString(ItemStack item) {
        if (item == null) {
            return null;
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeObject(item);
            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (Throwable e) {
            throw new IllegalArgumentException("Failed to serialize ItemStack", e);
        }
    }

    public static ItemStack stringToItem(String base64) {
        if (base64 == null || base64.isBlank()) {
            return null;
        }

        try (ByteArrayInputStream inputStream =
                     new ByteArrayInputStream(Base64Coder.decodeLines(base64));
             BukkitObjectInputStream dataInput =
                     new BukkitObjectInputStream(inputStream)) {

            Object object = dataInput.readObject();

            if (!(object instanceof ItemStack)) {
                throw new IllegalArgumentException("Deserialized object is not an ItemStack");
            }

            return (ItemStack) object;

        } catch (Throwable e) {
            throw new IllegalArgumentException("Failed to deserialize ItemStack", e);
        }
    }

    public static String itemArrayToString(ItemStack[] items) {
        if (items == null) {
            return null;
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(items.length);
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (Throwable e) {
            throw new IllegalArgumentException("Failed to serialize ItemStack array", e);
        }
    }

    public static ItemStack[] stringToItemArray(String base64) {
        if (base64 == null || base64.isBlank()) {
            return new ItemStack[0];
        }

        try (ByteArrayInputStream inputStream =
                     new ByteArrayInputStream(Base64Coder.decodeLines(base64));
             BukkitObjectInputStream dataInput =
                     new BukkitObjectInputStream(inputStream)) {

            int length = dataInput.readInt();
            ItemStack[] items = new ItemStack[length];

            for (int i = 0; i < length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            return items;

        } catch (Throwable e) {
            throw new IllegalArgumentException("Failed to deserialize ItemStack array", e);
        }
    }

    public static String itemListToString(List<ItemStack> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }

        return itemArrayToString(items.toArray(new ItemStack[0]));
    }

    public static List<ItemStack> stringToItemList(String base64) {
        ItemStack[] items = stringToItemArray(base64);
        List<ItemStack> list = new ArrayList<>(items.length);

        for (ItemStack item : items) {
            if (item != null) {
                list.add(item);
            }
        }

        return list;
    }

    public static boolean isSerializedItem(String base64) {
        try {
            stringToItem(base64);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static boolean isSerializedItemArray(String base64) {
        try {
            stringToItemArray(base64);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}
