// ==================== Файл: listeners/BuzzListener.java ====================
package com.zzz.listeners;

import com.zzz.ZZZ_teacraft;
import com.zzz.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;

import java.util.UUID;

public class BuzzListener implements Listener {
    private final ZZZ_teacraft plugin;

    public BuzzListener(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJointUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.FIREWORK_ROCKET && item.hasItemMeta()) {
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
            if (pdc.has(plugin.getTeaJointKey(), PersistentDataType.BOOLEAN)) {
                event.setCancelled(true);

                item.setAmount(item.getAmount() - 1);

                Location eyeLoc = player.getEyeLocation();
                player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,
                        eyeLoc, 30, 0.2, 0.2, 0.2, 0.05);

                UUID uuid = player.getUniqueId();
                int currentLevel = plugin.getBuzzLevels().getOrDefault(uuid, 0);
                plugin.getBuzzLevels().put(uuid, Math.min(100, currentLevel + Constants.BUZZ_INCREMENT));

                player.sendMessage(ChatColor.DARK_GREEN + "☁ " + ChatColor.GREEN +
                        "Напыханость: " + plugin.getBuzzBar(plugin.getBuzzLevels().get(uuid)));
            }
        }
    }

    @EventHandler
    public void onWaterDrink(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();

        if (item.getType() == Material.POTION) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            if (meta != null && meta.getBasePotionType() == PotionType.WATER) {
                Player player = event.getPlayer();
                UUID uuid = player.getUniqueId();
                int currentLevel = plugin.getBuzzLevels().getOrDefault(uuid, 0);

                if (currentLevel > 0) {
                    plugin.getBuzzLevels().put(uuid, Math.max(0, currentLevel - Constants.WATER_REDUCTION));
                    player.sendMessage(ChatColor.AQUA + "💧 " + ChatColor.WHITE +
                            "Вода снизила напыханость. Текущий уровень: " +
                            plugin.getBuzzBar(plugin.getBuzzLevels().get(uuid)));
                }
            }
        }
    }
}