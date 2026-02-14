// ==================== Файл: tasks/JumpTask.java ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import com.zzz.Constants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class JumpTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;
    private final Random random = new Random();
    private final Map<UUID, Long> lastJumpTime = new HashMap<>();

    public JumpTask(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            int level = plugin.getBuzzLevels().getOrDefault(uuid, 0);
            if (level < Constants.JUMP_LEVEL_MIN) continue;

            long now = System.currentTimeMillis();
            long lastEffect = plugin.getLastEffectTime().getOrDefault(uuid, 0L);
            long cooldown = plugin.getGlobalCooldown(level);

            if (now - lastEffect < cooldown) continue;

            long lastJump = lastJumpTime.getOrDefault(uuid, 0L);
            if (now - lastJump < 1000) continue;

            int frequency;
            float jumpPower;

            if (level >= 81) {
                frequency = random.nextInt(Constants.JUMP_FREQ_HIGH_MAX - Constants.JUMP_FREQ_HIGH_MIN + 1) + Constants.JUMP_FREQ_HIGH_MIN;
                jumpPower = Constants.JUMP_POWER_HIGH;
            } else if (level >= 61) {
                frequency = random.nextInt(Constants.JUMP_FREQ_MED_MAX - Constants.JUMP_FREQ_MED_MIN + 1) + Constants.JUMP_FREQ_MED_MIN;
                jumpPower = Constants.JUMP_POWER_MED;
            } else {
                frequency = random.nextInt(Constants.JUMP_FREQ_LOW_MAX - Constants.JUMP_FREQ_LOW_MIN + 1) + Constants.JUMP_FREQ_LOW_MIN;
                jumpPower = Constants.JUMP_POWER_LOW;
            }

            double chancePerTick = frequency / 1200.0;

            if (random.nextDouble() < chancePerTick) {
                if (player.isOnGround()) {
                    Vector velocity = player.getVelocity();
                    velocity.setY(jumpPower);
                    player.setVelocity(velocity);

                    lastJumpTime.put(uuid, now);
                    plugin.getLastEffectTime().put(uuid, now);
                }
            }
        }
    }
}