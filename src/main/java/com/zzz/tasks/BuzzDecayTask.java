// ==================== Файл: tasks/BuzzDecayTask.java ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import com.zzz.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.UUID;

public class BuzzDecayTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;

    public BuzzDecayTask(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            int level = plugin.getBuzzLevels().getOrDefault(uuid, 0);

            if (level <= 0) continue;

            plugin.getBuzzLevels().put(uuid, Math.max(0, level - Constants.NATURAL_DECAY));

            applyBuzzEffects(player, level);
        }
    }

    private void applyBuzzEffects(Player player, int level) {
        Random random = new Random();

        if (level > 10 && random.nextInt(100) < 30) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.NAUSEA, 100, 0, false, true, true));
        }

        int slownessLevel = Math.min(4, level / 25);
        if (slownessLevel > 0) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOWNESS, 40, slownessLevel - 1, false, true, true));
        }

        if (level >= 50) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.NIGHT_VISION, 400, 0, false, false, true));
        }

        // Темнота
        if (level >= Constants.DARKNESS_LEVEL_MIN) {
            double chance = level >= 81 ? Constants.DARKNESS_CHANCE_HIGH : Constants.DARKNESS_CHANCE_MED;
            int duration = level >= 81 ? Constants.DARKNESS_DURATION_HIGH : Constants.DARKNESS_DURATION_MED;

            if (random.nextDouble() < chance) {
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.DARKNESS, duration, 0, false, true, true));
            }
        }

        // Размытость
        if (level >= Constants.BLUR_LEVEL_MIN) {
            double chance;
            int duration;
            int amplifier;

            if (level >= 81) {
                chance = Constants.BLUR_CHANCE_HIGH;
                duration = Constants.BLUR_DURATION_HIGH;
                amplifier = Constants.BLUR_AMPLIFIER_HIGH;
            } else if (level >= 61) {
                chance = Constants.BLUR_CHANCE_MED;
                duration = Constants.BLUR_DURATION_MED;
                amplifier = Constants.BLUR_AMPLIFIER_MED;
            } else {
                chance = Constants.BLUR_CHANCE_LOW;
                duration = Constants.BLUR_DURATION_LOW;
                amplifier = Constants.BLUR_AMPLIFIER_LOW;
            }

            if (random.nextDouble() < chance) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, amplifier, false, true, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, duration, amplifier, false, true, true));
            }
        }

        if (level >= 90 && random.nextInt(100) < 15) {
            int slot = player.getInventory().getHeldItemSlot();
            ItemStack item = player.getInventory().getItem(slot);
            if (item != null && item.getType() != Material.AIR) {
                player.getInventory().setItem(slot, null);
                player.getWorld().dropItemNaturally(player.getLocation(), item);
                player.sendMessage(ChatColor.RED + "☁ Ваши пальцы ослабли... вы уронили предмет!");
            }
        }
    }
}