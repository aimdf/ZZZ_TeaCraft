// ==================== Файл: listeners/CraftListener.java ====================
package com.zzz.listeners;

import com.zzz.ZZZ_teacraft;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class CraftListener implements Listener {
    private final ZZZ_teacraft plugin;

    public CraftListener(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (event.isCancelled()) return;

        ItemStack result = event.getCurrentItem();
        if (result == null || result.getType() != Material.FIREWORK_ROCKET) return;

        boolean hasDryTea = false;

        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item != null && item.getType() == Material.DEAD_BUSH && item.hasItemMeta()) {
                PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
                if (pdc.has(plugin.getTeaDryKey(), PersistentDataType.BOOLEAN)) {
                    hasDryTea = true;
                    break;
                }
            }
        }

        if (!hasDryTea) {
            event.setCancelled(true);
            return;
        }

        if (event.isShiftClick()) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                ItemStack[] matrix = event.getInventory().getMatrix();
                int maxCrafts = getMaxCrafts(matrix);
                ItemStack jointItem = plugin.createTeaJointItem();
                jointItem.setAmount(2 * maxCrafts);

                Player player = (Player) event.getWhoClicked();
                HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(jointItem);

                if (!leftover.isEmpty()) {
                    for (ItemStack item : leftover.values()) {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                }

                for (int i = 0; i < matrix.length; i++) {
                    if (matrix[i] != null) {
                        matrix[i].setAmount(matrix[i].getAmount() - 1);
                    }
                }
                event.getInventory().setMatrix(matrix);
            });
        } else {
            event.setCurrentItem(plugin.createTeaJointItem());
            event.getCurrentItem().setAmount(2);
        }
    }

    private int getMaxCrafts(ItemStack[] matrix) {
        int paperCount = 0;
        int dryTeaCount = 0;

        for (ItemStack item : matrix) {
            if (item == null) continue;

            if (item.getType() == Material.PAPER) {
                paperCount += item.getAmount();
            } else if (item.getType() == Material.DEAD_BUSH && item.hasItemMeta()) {
                PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
                if (pdc.has(plugin.getTeaDryKey(), PersistentDataType.BOOLEAN)) {
                    dryTeaCount += item.getAmount();
                }
            }
        }

        return Math.min(paperCount, dryTeaCount);
    }
}