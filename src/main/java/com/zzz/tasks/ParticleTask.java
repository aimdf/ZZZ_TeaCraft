// ==================== Файл: tasks/ParticleTask.java (ОПТИМИЗИРОВАННЫЙ) ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import com.zzz.TeaBushData;
import com.zzz.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Map;

public class ParticleTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;
    private int tickCounter = 0;

    public ParticleTask(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Map<Location, TeaBushData> teaBushes = plugin.getTeaBushes();
        if (teaBushes.isEmpty()) return;

        tickCounter++;

        // Разные частицы спавним с разной частотой для оптимизации
        for (TeaBushData bushData : teaBushes.values()) {
            if (!bushData.isMature()) continue;

            Location loc = bushData.getLocation();

            // Проверяем, есть ли игроки поблизости, чтобы не спавнить частицы в пустых чанках
            if (loc.getWorld() != null) {
                Collection<Player> nearby = loc.getWorld().getNearbyPlayers(loc, 32);
                if (nearby.isEmpty()) continue;
            }

            // Основные частицы спавним каждый раз
            if (tickCounter % 2 == 0) { // Каждые 2 тика (0.1 секунды)
                Utils.spawnParticles(loc);
            }
        }
    }
}