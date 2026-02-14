// ==================== Файл: tasks/ShakeTask.java ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import com.zzz.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ShakeTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;
    private final Random random = new Random();
    private final Map<UUID, Integer> shakeTicks = new HashMap<>();

    public ShakeTask(ZZZ_teacraft plugin) {
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

            if (level >= Constants.SHAKE_LEVEL_MIN) {
                if (!player.isOnGround()) continue;

                int frequency;
                int duration;
                float yawAmplitude;
                float pitchAmplitude;

                if (level >= 81) {
                    frequency = random.nextInt(Constants.SHAKE_FREQ_HIGH_MAX - Constants.SHAKE_FREQ_HIGH_MIN + 1) + Constants.SHAKE_FREQ_HIGH_MIN;
                    duration = Constants.SHAKE_DURATION_HIGH;
                    yawAmplitude = Constants.SHAKE_AMPLITUDE_YAW_HIGH;
                    pitchAmplitude = Constants.SHAKE_AMPLITUDE_PITCH_HIGH;
                } else if (level >= 61) {
                    frequency = random.nextInt(Constants.SHAKE_FREQ_MED_MAX - Constants.SHAKE_FREQ_MED_MIN + 1) + Constants.SHAKE_FREQ_MED_MIN;
                    duration = Constants.SHAKE_DURATION_MED;
                    yawAmplitude = Constants.SHAKE_AMPLITUDE_YAW_MED;
                    pitchAmplitude = Constants.SHAKE_AMPLITUDE_PITCH_MED;
                } else {
                    frequency = random.nextInt(Constants.SHAKE_FREQ_LOW_MAX - Constants.SHAKE_FREQ_LOW_MIN + 1) + Constants.SHAKE_FREQ_LOW_MIN;
                    duration = Constants.SHAKE_DURATION_LOW;
                    yawAmplitude = Constants.SHAKE_AMPLITUDE_YAW_LOW;
                    pitchAmplitude = Constants.SHAKE_AMPLITUDE_PITCH_LOW;
                }

                if (shakeTicks.containsKey(uuid)) {
                    int ticksLeft = shakeTicks.get(uuid) - 1;
                    if (ticksLeft <= 0) {
                        shakeTicks.remove(uuid);
                    } else {
                        shakeTicks.put(uuid, ticksLeft);

                        Location loc = player.getLocation();

                        float newYaw = loc.getYaw() + (random.nextFloat() - 0.5f) * yawAmplitude;
                        float newPitch = loc.getPitch() + (random.nextFloat() - 0.5f) * pitchAmplitude;
                        newPitch = Math.max(-90, Math.min(90, newPitch));

                        player.setRotation(newYaw, newPitch);
                    }
                } else {
                    double chancePerTick = frequency / 1200.0;
                    if (random.nextDouble() < chancePerTick) {
                        shakeTicks.put(uuid, duration);
                        plugin.getLastEffectTime().put(uuid, now);
                    }
                }
            } else {
                shakeTicks.remove(uuid);
            }
        }
    }
}