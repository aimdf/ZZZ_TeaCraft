// ==================== Файл: managers/BuzzManager.java ====================
package com.zzz.managers;

import com.zzz.ZZZ_teacraft;
import com.zzz.Constants;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BuzzManager {
    private final ZZZ_teacraft plugin;
    private final Map<UUID, Integer> buzzLevels = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastEffectTime = new ConcurrentHashMap<>();
    private final Map<UUID, String> distortedNames = new ConcurrentHashMap<>();
    private final Map<UUID, Long> nameDistortExpiry = new ConcurrentHashMap<>();
    private final Map<UUID, Map<Integer, String>> itemRenames = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public BuzzManager(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    public int getLevel(Player player) {
        return buzzLevels.getOrDefault(player.getUniqueId(), 0);
    }

    public void setLevel(Player player, int level) {
        buzzLevels.put(player.getUniqueId(), Math.max(0, Math.min(100, level)));
    }

    public void addLevel(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        int current = buzzLevels.getOrDefault(uuid, 0);
        buzzLevels.put(uuid, Math.min(100, current + amount));
    }

    public void removeLevel(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        int current = buzzLevels.getOrDefault(uuid, 0);
        buzzLevels.put(uuid, Math.max(0, current - amount));
    }

    public boolean shouldApplyEffect(Player player, int minLevel, double chance) {
        UUID uuid = player.getUniqueId();
        int level = buzzLevels.getOrDefault(uuid, 0);
        if (level < minLevel) return false;

        long now = System.currentTimeMillis();
        long lastEffect = lastEffectTime.getOrDefault(uuid, 0L);
        long cooldown = getGlobalCooldown(level);

        if (now - lastEffect < cooldown) return false;
        if (random.nextDouble() >= chance) return false;

        lastEffectTime.put(uuid, now);
        return true;
    }

    public long getGlobalCooldown(int level) {
        if (level >= 80) return Constants.GLOBAL_COOLDOWN_HIGH;
        if (level >= 50) return Constants.GLOBAL_COOLDOWN_MED;
        return Constants.GLOBAL_COOLDOWN_LOW;
    }

    public void applyBaseEffects(Player player, int level) {
        if (level > 10 && random.nextInt(100) < 30) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.NAUSEA, 100, 0, false, true, true));
        }

        int slownessLevel = Math.min(4, level / 25);
        if (slownessLevel > 0) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOWNESS, 40, slownessLevel - 1, false, true, true));
        }

        if (level >= 50) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.NIGHT_VISION, 400, 0, false, false, true));
        }
    }

    // Геттеры для мап
    public Map<UUID, Integer> getBuzzLevels() { return buzzLevels; }
    public Map<UUID, Long> getLastEffectTime() { return lastEffectTime; }
    public Map<UUID, String> getDistortedNames() { return distortedNames; }
    public Map<UUID, Long> getNameDistortExpiry() { return nameDistortExpiry; }
    public Map<UUID, Map<Integer, String>> getItemRenames() { return itemRenames; }
}