// ==================== Файл: TeaBushData.java ====================
package com.zzz;

import org.bukkit.Location;

public class TeaBushData {
    private final Location location;
    private long plantTime;
    private boolean isMature;

    public TeaBushData(Location location, long plantTime, boolean isMature) {
        this.location = location;
        this.plantTime = plantTime;
        this.isMature = isMature;
    }

    public Location getLocation() { return location; }
    public long getPlantTime() { return plantTime; }
    public boolean isMature() { return isMature; }
    public void setMature(boolean mature) { isMature = mature; }
    public void setPlantTime(long plantTime) { this.plantTime = plantTime; }

    public int getGrowthProgress() {
        long elapsed = (System.currentTimeMillis() - plantTime) / 1000;
        return Math.min(100, (int) ((elapsed * 100) / Constants.GROW_TIME));
    }
}