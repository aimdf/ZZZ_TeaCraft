// ==================== Файл: tasks/PhantomParticleTask.java ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import com.zzz.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.UUID;

public class PhantomParticleTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;
    private final Random random = new Random();
    private final Particle[] friendlyParticles = {
            Particle.HAPPY_VILLAGER, Particle.HEART, Particle.NOTE
    };
    private final Particle[] neutralParticles = {
            Particle.SPLASH, Particle.TOTEM_OF_UNDYING, Particle.FIREWORK
    };
    private final Particle[] scaryParticles = {
            Particle.SMOKE, Particle.PORTAL, Particle.ANGRY_VILLAGER,
            Particle.SOUL_FIRE_FLAME, Particle.WITCH
    };

    public PhantomParticleTask(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            int level = plugin.getBuzzLevels().getOrDefault(uuid, 0);
            if (level < Constants.PARTICLE_LEVEL_MIN) continue;

            int frequency;
            int count;
            Particle[] particleSet;

            if (level >= 81) {
                frequency = Constants.PARTICLE_FREQ_HIGH;
                count = Constants.PARTICLE_COUNT_HIGH;
                particleSet = scaryParticles;
            } else if (level >= 61) {
                frequency = Constants.PARTICLE_FREQ_MED;
                count = Constants.PARTICLE_COUNT_MED;
                particleSet = neutralParticles;
            } else {
                frequency = Constants.PARTICLE_FREQ_LOW;
                count = Constants.PARTICLE_COUNT_LOW;
                particleSet = friendlyParticles;
            }

            double chancePerTick = 1.0 / frequency;

            if (random.nextDouble() < chancePerTick) {
                Particle particle = particleSet[random.nextInt(particleSet.length)];
                Location loc = player.getLocation().add(
                        random.nextDouble() * 4 - 2,
                        random.nextDouble() * 3,
                        random.nextDouble() * 4 - 2
                );
                player.getWorld().spawnParticle(particle, loc, count, 0.2, 0.2, 0.2, 0.02);
            }
        }
    }
}