// ==================== Файл: tasks/ItemRenameTask.java ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import com.zzz.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ItemRenameTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;
    private final Random random = new Random();
    private final String[] funnyNames = {
            "Загадочная штука", "Непонятный предмет", "Глючный объект",
            "Странная вещь", "Сомнительное нечто", "Подозрительный кусок",
            "Магический артефакт", "Древняя реликвия", "Космический мусор",
            "Сломанный предмет", "Чей-то мусор", "Блестяшка",
            "Вкусняшка", "Нямка", "Хрустяшка",
            "Тыгыдык", "Бдыщь", "Бабах",
            "Секретный ингредиент", "Зельеварение", "Алкаголик"
    };

    public ItemRenameTask(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            int level = plugin.getBuzzLevels().getOrDefault(uuid, 0);

            long now = System.currentTimeMillis();
            long lastEffect = plugin.getLastEffectTime().getOrDefault(uuid, 0L);
            long cooldown = plugin.getGlobalCooldown(level);

            if (now - lastEffect < cooldown) continue;

            if (level >= Constants.ITEMRENAME_LEVEL_MIN) {
                double chance;
                int maxCount;

                if (level >= 81) {
                    chance = Constants.ITEMRENAME_CHANCE_HIGH;
                    maxCount = Constants.ITEMRENAME_COUNT_HIGH;
                } else if (level >= 61) {
                    chance = Constants.ITEMRENAME_CHANCE_MED;
                    maxCount = Constants.ITEMRENAME_COUNT_MED;
                } else {
                    chance = Constants.ITEMRENAME_CHANCE_LOW;
                    maxCount = Constants.ITEMRENAME_COUNT_LOW;
                }

                Map<Integer, String> renames = new HashMap<>();
                ItemStack[] contents = player.getInventory().getContents();

                for (int i = 0; i < contents.length; i++) {
                    ItemStack item = contents[i];
                    if (item != null && item.getType() != Material.AIR && random.nextDouble() < chance / maxCount) {
                        String funnyName = funnyNames[random.nextInt(funnyNames.length)];
                        renames.put(i, funnyName);
                    }
                }

                if (!renames.isEmpty()) {
                    plugin.getItemRenames().put(uuid, renames);
                    plugin.getLastEffectTime().put(uuid, now);
                }
            } else {
                plugin.getItemRenames().remove(uuid);
            }
        }
    }
}