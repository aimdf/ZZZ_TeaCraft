// ==================== Файл: managers/ItemManager.java ====================
package com.zzz.managers;

import com.zzz.ZZZ_teacraft;
import com.zzz.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemManager {
    private final ZZZ_teacraft plugin;

    public ItemManager(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    public ItemStack createTeaBushItem() {
        ItemStack item = new ItemStack(Material.FERN);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Куст чая");

        // Обновляем описание с новым временем роста
        int hours = Constants.GROW_TIME / 3600;
        int minutes = (Constants.GROW_TIME % 3600) / 60;
        String timeText;
        if (hours > 0) {
            timeText = hours + "ч " + minutes + "м";
        } else {
            timeText = minutes + " минут";
        }

        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "🌱 Посадите на землю",
                ChatColor.GRAY + "⏱ Время роста: " + timeText,
                ChatColor.GRAY + "💧 Требует полива",
                ChatColor.GRAY + "✂️ Собирать ножницами",
                ChatColor.GRAY + "🔍 Стекло - информация"
        ));
        meta.getPersistentDataContainer().set(plugin.getTeaBushKey(), PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createTeaFruitItem(int dryness) {
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

    public ItemStack createDryTeaItem() {
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

    public ItemStack createTeaJointItem() {
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

    private String getProgressBar(int percent) {
        int bars = percent / 10;
        StringBuilder bar = new StringBuilder();
        bar.append(ChatColor.GREEN);
        for (int i = 0; i < bars; i++) bar.append("|");
        bar.append(ChatColor.GRAY);
        for (int i = bars; i < 10; i++) bar.append("|");
        return bar.toString();
    }
}