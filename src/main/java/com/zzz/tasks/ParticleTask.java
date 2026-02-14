// ==================== Файл: tasks/ParticleTask.java ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import com.zzz.TeaBushData;
import com.zzz.Utils;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.logging.Level;

public class ParticleTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;

    public ParticleTask(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Map<Location, TeaBushData> teaBushes = plugin.getTeaBushes();
        int particleCount = 0;

        for (TeaBushData bushData : teaBushes.values()) {
            if (bushData.isMature()) {
                Location loc = bushData.getLocation();

                if (loc.getWorld() == null) continue;
                if (!loc.getChunk().isLoaded()) continue;

                boolean playersNearby = loc.getWorld().getPlayers().stream()
                        .anyMatch(player -> player.getLocation().distanceSquared(loc) < 1024);

                if (playersNearby) {
                    Utils.spawnParticles(loc);
                    particleCount++;
                }
            }
        }

        if (particleCount > 0 && plugin.getLogger().isLoggable(Level.FINE)) {
            plugin.getLogger().fine("Spawned particles for " + particleCount + " mature tea bushes");
        }
    }
}