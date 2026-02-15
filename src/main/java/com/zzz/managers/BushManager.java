// ==================== Файл: managers/BushManager.java ====================
package com.zzz.managers;

import com.zzz.ZZZ_teacraft;
import com.zzz.TeaBushData;
import com.zzz.Constants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BushManager {
    private final ZZZ_teacraft plugin;
    private final Map<Location, TeaBushData> teaBushes = new ConcurrentHashMap<>();

    public BushManager(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    public void plantBush(Location location) {
        TeaBushData bushData = new TeaBushData(location, System.currentTimeMillis(), false);
        teaBushes.put(location, bushData);
        plugin.saveTeaBush(bushData);
    }

    public void removeBush(Location location) {
        TeaBushData bushData = teaBushes.remove(location);
        if (bushData != null) {
            plugin.deleteTeaBush(bushData);
        }
    }

    public TeaBushData getBush(Location location) {
        return teaBushes.get(location);
    }

    public boolean isTeaBush(Block block) {
        return block.getType() == Material.FERN && teaBushes.containsKey(block.getLocation());
    }

    public void growBushes() {
        long currentTime = System.currentTimeMillis();
        for (TeaBushData bushData : teaBushes.values()) {
            if (bushData.isMature()) continue;

            long elapsed = (currentTime - bushData.getPlantTime()) / 1000;
            if (elapsed >= Constants.GROW_TIME) {
                bushData.setMature(true);
                plugin.saveTeaBush(bushData);
            }
        }
    }

    public Map<Location, TeaBushData> getTeaBushes() { return teaBushes; }
}