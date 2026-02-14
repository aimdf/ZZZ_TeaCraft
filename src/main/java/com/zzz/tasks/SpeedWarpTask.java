// ==================== Файл: tasks/SpeedWarpTask.java ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import com.zzz.Constants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SpeedWarpTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;
    private final Random random = new Random();
    private final Map<UUID, Integer> warpTicks = new HashMap<>();

    public SpeedWarpTask(ZZZ_teacraft plugin) {
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

            if (level >= Constants.SPEEDWARP_LEVEL_MIN) {
                int frequency;
                int duration;
                int amplifier;

                if (level >= 81) {
                    frequency = random.nextInt(Constants.SPEEDWARP_FREQ_HIGH_MAX - Constants.SPEEDWARP_FREQ_HIGH_MIN + 1) + Constants.SPEEDWARP_FREQ_HIGH_MIN;
                    duration = Constants.SPEEDWARP_DURATION_HIGH;
                    amplifier = Constants.SPEEDWARP_AMPLIFIER_HIGH;
                } else if (level >= 61) {
                    frequency = random.nextInt(Constants.SPEEDWARP_FREQ_MED_MAX - Constants.SPEEDWARP_FREQ_MED_MIN + 1) + Constants.SPEEDWARP_FREQ_MED_MIN;
                    duration = Constants.SPEEDWARP_DURATION_MED;
                    amplifier = Constants.SPEEDWARP_AMPLIFIER_MED;
                } else {
                    frequency = random.nextInt(Constants.SPEEDWARP_FREQ_LOW_MAX - Constants.SPEEDWARP_FREQ_LOW_MIN + 1) + Constants.SPEEDWARP_FREQ_LOW_MIN;
                    duration = Constants.SPEEDWARP_DURATION_LOW;
                    amplifier = Constants.SPEEDWARP_AMPLIFIER_LOW;
                }

                if (warpTicks.containsKey(uuid)) {
                    int ticksLeft = warpTicks.get(uuid) - 1;
                    if (ticksLeft <= 0) {
                        warpTicks.remove(uuid);
                        player.removePotionEffect(PotionEffectType.SPEED);
                        player.removePotionEffect(PotionEffectType.SLOWNESS);
                    } else {
                        warpTicks.put(uuid, ticksLeft);
                    }
                } else {
                    double chancePerTick = frequency / 1200.0;
                    if (random.nextDouble() < chancePerTick) {
                        warpTicks.put(uuid, duration);
                        plugin.getLastEffectTime().put(uuid, now);

                        if (random.nextBoolean()) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, amplifier, false, true, true));
                        } else {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, amplifier, false, true, true));
                        }
                    }
                }
            } else {
                warpTicks.remove(uuid);
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.SLOWNESS);
            }
        }
    }
}