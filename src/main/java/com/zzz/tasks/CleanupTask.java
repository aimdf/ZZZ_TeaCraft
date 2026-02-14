// ==================== Файл: tasks/CleanupTask.java ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import com.zzz.TeaBushData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;

public class CleanupTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;

    public CleanupTask(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Map<Location, TeaBushData> teaBushes = plugin.getTeaBushes();
        int removedCount = 0;
        Iterator<Map.Entry<Location, TeaBushData>> iterator = teaBushes.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Location, TeaBushData> entry = iterator.next();
            Location loc = entry.getKey();

            if (loc.getWorld() == null) {
                iterator.remove();
                plugin.deleteTeaBushByLocation(loc);
                removedCount++;
                continue;
            }

            if (loc.getBlock().getType() != Material.FERN) {
                iterator.remove();
                plugin.deleteTeaBushByLocation(loc);
                removedCount++;
                continue;
            }
        }

        if (removedCount > 0) {
            plugin.getLogger().info("Cleaned up " + removedCount + " invalid tea bushes");
        }
    }
}