// ==================== Файл: Utils.java ====================
package com.zzz;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static String getBuzzBar(int level) {
        int filled = level / 10;
        StringBuilder bar = new StringBuilder();
        bar.append(ChatColor.GREEN);
        for (int i = 0; i < filled; i++) bar.append("▮");
        bar.append(ChatColor.GRAY);
        for (int i = filled; i < 10; i++) bar.append("▯");
        bar.append(ChatColor.WHITE).append(" ").append(level).append("%");
        return bar.toString();
    }

    public static void spawnParticles(Location location) {
        if (location.getWorld() == null) return;

        Location center = location.clone().add(0.5, 1.0, 0.5);

        location.getWorld().spawnParticle(
                Particle.END_ROD,
                center,
                8,
                0.2, 0.2, 0.2,
                0.02
        );

        if (ThreadLocalRandom.current().nextInt(4) == 0) {
            location.getWorld().spawnParticle(
                    Particle.HAPPY_VILLAGER,
                    center,
                    4,
                    0.2, 0.3, 0.2,
                    0.1
            );
        }
    }

    public static void removeParticles(Location location) {
        // Частицы исчезают сами
    }

    public static long getGlobalCooldown(int level) {
        if (level >= 80) {
            return Constants.GLOBAL_COOLDOWN_HIGH;
        } else if (level >= 50) {
            return Constants.GLOBAL_COOLDOWN_MED;
        } else {
            return Constants.GLOBAL_COOLDOWN_LOW;
        }
    }

    public static String getDefaultItemName(Material material) {
        String name = material.toString().toLowerCase();
        name = name.replace('_', ' ');
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}