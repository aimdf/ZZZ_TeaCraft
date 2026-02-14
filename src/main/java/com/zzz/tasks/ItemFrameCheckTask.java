// ==================== Файл: tasks/ItemFrameCheckTask.java ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemFrameCheckTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;

    public ItemFrameCheckTask(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (org.bukkit.entity.Entity entity : player.getNearbyEntities(50, 50, 50)) {
                if (entity instanceof ItemFrame frame) {
                    ItemStack item = frame.getItem();
                    if (item.getType() == Material.SHORT_GRASS && item.hasItemMeta()) {
                        ItemMeta meta = item.getItemMeta();
                        PersistentDataContainer pdc = meta.getPersistentDataContainer();

                        if (pdc.has(plugin.getTeaFruitKey(), PersistentDataType.BOOLEAN)) {
                            int dryness = pdc.getOrDefault(plugin.getDrynessKey(), PersistentDataType.INTEGER, 0);
                            dryness = Math.min(100, dryness + 2);

                            if (dryness >= 100) {
                                frame.setItem(plugin.createDryTeaItem());
                            } else {
                                frame.setItem(plugin.createTeaFruitItem(dryness));
                            }
                        }
                    }
                }
            }
        }
    }
}