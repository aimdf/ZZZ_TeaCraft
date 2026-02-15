// ==================== Файл: TeaBushData.java (ОПТИМИЗИРОВАННЫЙ) ====================
package com.zzz;

import org.bukkit.Location;

import java.util.UUID;

public class TeaBushData {
    private final Location location;
    private long plantTime;
    private boolean isMature;
    private int moisture; // 0-100
    private long lastMoistureUpdate;
    private UUID plantedBy; // кто посадил

    public TeaBushData(Location location, long plantTime, boolean isMature) {
        this.location = location;
        this.plantTime = plantTime;
        this.isMature = isMature;
        this.moisture = Constants.MAX_MOISTURE; // при посадке всегда 100%
        this.lastMoistureUpdate = System.currentTimeMillis();
        this.plantedBy = null;
    }

    public TeaBushData(Location location, long plantTime, boolean isMature, int moisture, long lastMoistureUpdate) {
        this.location = location;
        this.plantTime = plantTime;
        this.isMature = isMature;
        this.moisture = moisture;
        this.lastMoistureUpdate = lastMoistureUpdate;
        this.plantedBy = null;
    }

    public Location getLocation() { return location; }
    public long getPlantTime() { return plantTime; }
    public boolean isMature() { return isMature; }
    public void setMature(boolean mature) { isMature = mature; }
    public void setPlantTime(long plantTime) { this.plantTime = plantTime; }

    public UUID getPlantedBy() { return plantedBy; }
    public void setPlantedBy(UUID plantedBy) { this.plantedBy = plantedBy; }

    // Геттер больше не обновляет влажность - это делает MoistureDrainTask
    public int getMoisture() {
        return moisture;
    }

    // Для прямого получения без обновления (для сохранения в БД)
    public int getRawMoisture() {
        return moisture;
    }

    public long getLastMoistureUpdate() {
        return lastMoistureUpdate;
    }

    // Полив
    public void water(int amount) {
        this.moisture = Math.min(Constants.MAX_MOISTURE, Math.max(Constants.MIN_MOISTURE, moisture + amount));
        this.lastMoistureUpdate = System.currentTimeMillis();
    }

    // Прогресс роста БЕЗ учета влажности (для БД)
    public int getRawGrowthProgress() {
        long elapsed = (System.currentTimeMillis() - plantTime) / 1000;
        return Math.min(100, (int) ((elapsed * 100) / Constants.GROW_TIME));
    }

    // Прогресс роста С УЧЕТОМ влажности (для отображения)
    public int getEffectiveGrowthProgress() {
        if (isMature) return 100;

        long now = System.currentTimeMillis();
        long elapsed = (now - plantTime) / 1000;

        double speedMultiplier = Constants.MIN_GROWTH_SPEED +
                (moisture / 100.0) * (Constants.MAX_GROWTH_SPEED - Constants.MIN_GROWTH_SPEED);

        // Эффективное время с учетом влажности
        long effectiveElapsed = (long)(elapsed * speedMultiplier);

        return Math.min(100, (int)((effectiveElapsed * 100) / Constants.GROW_TIME));
    }

    // Время до созревания с учетом влажности
    public long getTimeLeft() {
        if (isMature) return 0;

        long now = System.currentTimeMillis();
        long elapsed = (now - plantTime) / 1000;

        double speedMultiplier = Constants.MIN_GROWTH_SPEED +
                (moisture / 100.0) * (Constants.MAX_GROWTH_SPEED - Constants.MIN_GROWTH_SPEED);

        long remainingSeconds = (long)((Constants.GROW_TIME - elapsed * speedMultiplier) / speedMultiplier);
        return Math.max(0, remainingSeconds);
    }

    // Форматированное время
    public String getFormattedTimeLeft() {
        long seconds = getTimeLeft();
        if (seconds <= 0) return "§aСОЗРЕЛ";
        if (seconds < 60) return seconds + "с";
        if (seconds < 3600) {
            long minutes = seconds / 60;
            long secs = seconds % 60;
            return minutes + "м " + secs + "с";
        }
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        return hours + "ч " + minutes + "м";
    }

    // Прогресс-бар
    public static String getProgressBar(int percent, String color) {
        int bars = percent / 10;
        StringBuilder sb = new StringBuilder("§7[");
        for (int i = 0; i < bars; i++) sb.append(color + "█");
        for (int i = bars; i < 10; i++) sb.append("§7▒");
        sb.append("§7]");
        return sb.toString();
    }
}