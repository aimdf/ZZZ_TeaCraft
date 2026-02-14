// ==================== Файл: tasks/SoundTask.java ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import com.zzz.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.UUID;

public class SoundTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;
    private final Random random = new Random();
    private final Sound[] sounds = {
            Sound.ENTITY_CREEPER_PRIMED,
            Sound.ENTITY_ARROW_SHOOT,
            Sound.ENTITY_ZOMBIE_AMBIENT,
            Sound.ENTITY_SKELETON_AMBIENT,
            Sound.ENTITY_SPIDER_AMBIENT,
            Sound.ENTITY_GHAST_SCREAM,
            Sound.ENTITY_WITHER_AMBIENT,
            Sound.ENTITY_ENDERMAN_SCREAM,
            Sound.ENTITY_LIGHTNING_BOLT_THUNDER,
            Sound.BLOCK_ANVIL_LAND,
            Sound.BLOCK_CHEST_OPEN,
            Sound.BLOCK_CHERRY_WOOD_DOOR_CLOSE,
            Sound.BLOCK_PORTAL_AMBIENT,
            Sound.ENTITY_TNT_PRIMED,
            Sound.ENTITY_BLAZE_SHOOT
    };

    public SoundTask(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            int level = plugin.getBuzzLevels().getOrDefault(uuid, 0);
            if (level < Constants.SOUND_LEVEL_MIN) continue;

            long now = System.currentTimeMillis();
            long lastEffect = plugin.getLastEffectTime().getOrDefault(uuid, 0L);
            long cooldown = plugin.getGlobalCooldown(level);

            if (now - lastEffect < cooldown) continue;

            int frequency;
            float volume;

            if (level >= 81) {
                frequency = random.nextInt(Constants.SOUND_FREQ_HIGH_MAX - Constants.SOUND_FREQ_HIGH_MIN + 1) + Constants.SOUND_FREQ_HIGH_MIN;
                volume = Constants.SOUND_VOLUME_HIGH;
            } else if (level >= 61) {
                frequency = random.nextInt(Constants.SOUND_FREQ_MED_MAX - Constants.SOUND_FREQ_MED_MIN + 1) + Constants.SOUND_FREQ_MED_MIN;
                volume = Constants.SOUND_VOLUME_MED;
            } else {
                frequency = random.nextInt(Constants.SOUND_FREQ_LOW_MAX - Constants.SOUND_FREQ_LOW_MIN + 1) + Constants.SOUND_FREQ_LOW_MIN;
                volume = Constants.SOUND_VOLUME_LOW;
            }

            double chancePerTick = frequency / 1200.0;

            if (random.nextDouble() < chancePerTick) {
                Sound sound = sounds[random.nextInt(sounds.length)];
                player.playSound(player.getLocation(), sound, volume, Constants.SOUND_PITCH);
                plugin.getLastEffectTime().put(uuid, now);
            }
        }
    }
}