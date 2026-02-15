// ==================== Файл: tasks/MoistureDrainTask.java (НОВЫЙ) ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import com.zzz.TeaBushData;
import com.zzz.Constants;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class MoistureDrainTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;

    public MoistureDrainTask(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Map<Location, TeaBushData> teaBushes = plugin.getTeaBushes();
        boolean changed = false;
        long currentTime = System.currentTimeMillis();

        for (TeaBushData bushData : teaBushes.values()) {
            if (bushData.isMature()) continue; // Зрелые кусты не теряют влагу

            int currentMoisture = bushData.getRawMoisture();
            if (currentMoisture > Constants.MIN_MOISTURE) {
                // Уменьшаем влажность на 1% каждую минуту
                int newMoisture = Math.max(Constants.MIN_MOISTURE, currentMoisture - Constants.MOISTURE_DRAIN_RATE);

                // Прямое обновление без вызова getMoisture()
                java.lang.reflect.Field moistureField;
                try {
                    moistureField = TeaBushData.class.getDeclaredField("moisture");
                    moistureField.setAccessible(true);
                    moistureField.set(bushData, newMoisture);

                    java.lang.reflect.Field lastUpdateField = TeaBushData.class.getDeclaredField("lastMoistureUpdate");
                    lastUpdateField.setAccessible(true);
                    lastUpdateField.set(bushData, currentTime);

                    changed = true;
                } catch (Exception e) {
                    // Если рефлексия не сработала, используем обычный метод
                    bushData.water(-Constants.MOISTURE_DRAIN_RATE);
                    changed = true;
                }
            }
        }

        // Сохраняем изменения если были
        if (changed) {
            plugin.saveAllTeaBushesAsync();
        }
    }
}