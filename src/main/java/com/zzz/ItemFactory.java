// ==================== Файл: ItemFactory.java ====================
package com.zzz;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemFactory {

    public static ItemStack createTeaBushItem(ZZZ_teacraft plugin) {
        ItemStack item = new ItemStack(Material.FERN);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Куст чая");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Посадите на землю",
                ChatColor.GRAY + "Время роста: 5 минут"
        ));
        meta.getPersistentDataContainer().set(plugin.getTeaBushKey(), PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createTeaFruitItem(ZZZ_teacraft plugin, int dryness) {
        ItemStack item = new ItemStack(Material.SHORT_GRASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Плод чая");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Сырье для сушки");
        if (dryness > 0) {
            lore.add(ChatColor.GRAY + "Сушка: " + getProgressBar(dryness) +
                    ChatColor.WHITE + " " + dryness + "%");
        }
        meta.setLore(lore);

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(plugin.getTeaFruitKey(), PersistentDataType.BOOLEAN, true);
        if (dryness > 0) {
            pdc.set(plugin.getDrynessKey(), PersistentDataType.INTEGER, dryness);
        }

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createDryTeaItem(ZZZ_teacraft plugin) {
        ItemStack item = new ItemStack(Material.DEAD_BUSH);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Сухой чай");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Высушенный чайный лист",
                ChatColor.GRAY + "Используется для скруток"
        ));
        meta.getPersistentDataContainer().set(plugin.getTeaDryKey(), PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createTeaJointItem(ZZZ_teacraft plugin) {
        ItemStack item = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Чайная скрутка");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "ПКМ чтобы использовать",
                ChatColor.GRAY + "Дает эффект напыханости"
        ));
        meta.getPersistentDataContainer().set(plugin.getTeaJointKey(), PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    private static String getProgressBar(int percent) {
        int bars = percent / 10;
        StringBuilder bar = new StringBuilder();
        bar.append(ChatColor.GREEN);
        for (int i = 0; i < bars; i++) bar.append("|");
        bar.append(ChatColor.GRAY);
        for (int i = bars; i < 10; i++) bar.append("|");
        return bar.toString();
    }
}