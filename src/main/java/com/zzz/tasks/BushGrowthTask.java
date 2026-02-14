// ==================== Файл: tasks/BushGrowthTask.java ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import com.zzz.TeaBushData;
import com.zzz.Constants;
import com.zzz.Utils;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class BushGrowthTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;

    public BushGrowthTask(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        Map<Location, TeaBushData> teaBushes = plugin.getTeaBushes();

        for (TeaBushData bushData : teaBushes.values()) {
            if (bushData.isMature()) continue;

            long elapsed = (currentTime - bushData.getPlantTime()) / 1000;
            if (elapsed >= Constants.GROW_TIME && !bushData.isMature()) {
                bushData.setMature(true);
                Utils.spawnParticles(bushData.getLocation());
                plugin.saveTeaBush(bushData);
            }
        }
    }
}