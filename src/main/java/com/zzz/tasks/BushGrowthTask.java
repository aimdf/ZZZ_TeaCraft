// ==================== Файл: tasks/BushGrowthTask.java (ОПТИМИЗИРОВАННЫЙ) ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import com.zzz.TeaBushData;
import com.zzz.Constants;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class BushGrowthTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;
    private int tickCounter = 0;

    public BushGrowthTask(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        Map<Location, TeaBushData> teaBushes = plugin.getTeaBushes();
        boolean changed = false;

        tickCounter++;

        // Проверяем рост каждую секунду (20 тиков)
        if (tickCounter % 20 != 0) return;

        for (TeaBushData bushData : teaBushes.values()) {
            if (bushData.isMature()) continue;

            // Получаем текущую влажность (уже актуальна благодаря MoistureDrainTask)
            int moisture = bushData.getRawMoisture();

            // Вычисляем эффективное время с учетом влажности
            long elapsed = (currentTime - bushData.getPlantTime()) / 1000;

            double speedMultiplier = Constants.MIN_GROWTH_SPEED +
                    (moisture / 100.0) * (Constants.MAX_GROWTH_SPEED - Constants.MIN_GROWTH_SPEED);

            long effectiveElapsed = (long)(elapsed * speedMultiplier);

            // Проверяем, созрел ли куст
            if (effectiveElapsed >= Constants.GROW_TIME && !bushData.isMature()) {
                bushData.setMature(true);
                changed = true;
            }
        }

        // Сохраняем изменения в БД, если были изменения
        if (changed) {
            plugin.saveAllTeaBushesAsync();
        }
    }
}