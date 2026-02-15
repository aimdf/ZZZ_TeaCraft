// ==================== Файл: tasks/CombinedEffectsTask.java (ОПТИМИЗИРОВАННЫЙ) ====================
package com.zzz.tasks;

import com.zzz.ZZZ_teacraft;
import com.zzz.Constants;
import com.zzz.managers.BuzzManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class CombinedEffectsTask extends BukkitRunnable {
    private final ZZZ_teacraft plugin;
    private final BuzzManager buzzManager;
    private final Random random = new Random();
    private final Map<UUID, Integer> shakeTicks = new HashMap<>();
    private final Map<UUID, Integer> warpTicks = new HashMap<>();
    private final Map<UUID, Long> lastJumpTime = new HashMap<>();
    private final Map<UUID, Long> lastEffectCheck = new HashMap<>(); // Кэш для проверки эффектов

    // Звуки для эффектов
    private final Sound[] scarySounds = {
            Sound.ENTITY_CREEPER_PRIMED, Sound.ENTITY_WITHER_AMBIENT,
            Sound.ENTITY_GHAST_SCREAM, Sound.ENTITY_LIGHTNING_BOLT_THUNDER
    };
    private final Sound[] neutralSounds = {
            Sound.BLOCK_CHEST_OPEN, Sound.ENTITY_ARROW_SHOOT,
            Sound.BLOCK_ANVIL_LAND, Sound.ENTITY_TNT_PRIMED
    };
    private final Sound[] friendlySounds = {
            Sound.ENTITY_CHICKEN_AMBIENT, Sound.ENTITY_CAT_AMBIENT,
            Sound.BLOCK_NOTE_BLOCK_HARP, Sound.ENTITY_EXPERIENCE_ORB_PICKUP
    };

    // Частицы для эффектов
    private final Particle[] friendlyParticles = {
            Particle.HAPPY_VILLAGER, Particle.HEART, Particle.NOTE
    };
    private final Particle[] neutralParticles = {
            Particle.SPLASH, Particle.TOTEM_OF_UNDYING, Particle.FIREWORK
    };
    private final Particle[] scaryParticles = {
            Particle.SMOKE, Particle.PORTAL, Particle.ANGRY_VILLAGER,
            Particle.SOUL_FIRE_FLAME, Particle.WITCH
    };

    public CombinedEffectsTask(ZZZ_teacraft plugin) {
        this.plugin = plugin;
        this.buzzManager = plugin.getBuzzManager();
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            int level = buzzManager.getLevel(player);

            if (level < Constants.EFFECTS_LEVEL_MIN) {
                // Очищаем временные эффекты если уровень слишком низкий
                shakeTicks.remove(uuid);
                warpTicks.remove(uuid);
                lastEffectCheck.remove(uuid);
                continue;
            }

            // Проверяем, нужно ли применять эффекты в этом тике (кэширование)
            Long lastCheck = lastEffectCheck.get(uuid);
            if (lastCheck != null && currentTime - lastCheck < 150) { // ~3 тика
                // Пропускаем проверку эффектов, но продолжаем активные эффекты
                applyActiveEffects(player, level);
                continue;
            }
            lastEffectCheck.put(uuid, currentTime);

            // Применяем все эффекты с нормальной частотой
            applyMovementEffects(player, level, currentTime);
            applySoundEffects(player, level);
            applyVisualEffects(player, level);
            applyActiveEffects(player, level);
        }
    }

    private void applyActiveEffects(Player player, int level) {
        UUID uuid = player.getUniqueId();

        // Продолжаем активную тряску если есть
        if (shakeTicks.containsKey(uuid)) {
            int ticksLeft = shakeTicks.get(uuid) - 1;
            if (ticksLeft <= 0) {
                shakeTicks.remove(uuid);
            } else {
                shakeTicks.put(uuid, ticksLeft);
                applyCameraShake(player, level);
            }
        }

        // Продолжаем активный варп если есть
        if (warpTicks.containsKey(uuid)) {
            int ticksLeft = warpTicks.get(uuid) - 1;
            if (ticksLeft <= 0) {
                warpTicks.remove(uuid);
            } else {
                warpTicks.put(uuid, ticksLeft);
            }
        }
    }

    private void applyMovementEffects(Player player, int level, long currentTime) {
        UUID uuid = player.getUniqueId();

        // Прыжки - проверяем реже
        if (level >= Constants.JUMP_LEVEL_MIN && player.isOnGround() && random.nextInt(3) == 0) {
            double jumpChance = getJumpChance(level);
            Long lastJump = lastJumpTime.get(uuid);

            if ((lastJump == null || currentTime - lastJump > 1000) && random.nextDouble() < jumpChance) {
                Vector velocity = player.getVelocity();
                velocity.setY(getJumpPower(level));
                player.setVelocity(velocity);
                lastJumpTime.put(uuid, currentTime);
            }
        }

        // Тряска камеры - проверяем новые эффекты реже
        if (level >= Constants.SHAKE_LEVEL_MIN && !shakeTicks.containsKey(uuid) && random.nextInt(4) == 0) {
            double shakeChance = getShakeChance(level);
            if (random.nextDouble() < shakeChance) {
                shakeTicks.put(uuid, getShakeDuration(level));
            }
        }

        // Варп скорости - проверяем новые эффекты реже
        if (level >= Constants.SPEEDWARP_LEVEL_MIN && !warpTicks.containsKey(uuid) && random.nextInt(4) == 0) {
            double warpChance = getWarpChance(level);
            if (random.nextDouble() < warpChance) {
                warpTicks.put(uuid, getWarpDuration(level));
                applySpeedWarp(player, level);
            }
        }
    }

    private void applySoundEffects(Player player, int level) {
        if (level < Constants.SOUND_LEVEL_MIN || random.nextInt(3) != 0) return;

        double soundChance = getSoundChance(level);
        if (random.nextDouble() < soundChance) {
            Sound[] sounds = getSoundSet(level);
            Sound sound = sounds[random.nextInt(sounds.length)];
            float volume = getSoundVolume(level);
            player.playSound(player.getLocation(), sound, volume, 1.0f);
        }
    }

    private void applyVisualEffects(Player player, int level) {
        if (level < Constants.PARTICLE_LEVEL_MIN || random.nextInt(4) != 0) return;

        double particleChance = getParticleChance(level);
        if (random.nextDouble() < particleChance) {
            Particle[] particles = getParticleSet(level);
            Particle particle = particles[random.nextInt(particles.length)];
            Location loc = player.getLocation().add(
                    random.nextDouble() * 4 - 2,
                    random.nextDouble() * 3,
                    random.nextDouble() * 4 - 2
            );
            player.getWorld().spawnParticle(particle, loc,
                    getParticleCount(level), 0.2, 0.2, 0.2, 0.02);
        }
    }

    // Хелперы для получения значений в зависимости от уровня
    private double getJumpChance(int level) {
        if (level >= 81) return Constants.JUMP_FREQ_HIGH_MIN / 60.0;
        if (level >= 61) return Constants.JUMP_FREQ_MED_MIN / 60.0;
        return Constants.JUMP_FREQ_LOW_MIN / 60.0;
    }

    private float getJumpPower(int level) {
        if (level >= 81) return Constants.JUMP_POWER_HIGH;
        if (level >= 61) return Constants.JUMP_POWER_MED;
        return Constants.JUMP_POWER_LOW;
    }

    private double getShakeChance(int level) {
        if (level >= 81) return Constants.SHAKE_FREQ_HIGH_MIN / 60.0;
        if (level >= 61) return Constants.SHAKE_FREQ_MED_MIN / 60.0;
        return Constants.SHAKE_FREQ_LOW_MIN / 60.0;
    }

    private int getShakeDuration(int level) {
        if (level >= 81) return Constants.SHAKE_DURATION_HIGH;
        if (level >= 61) return Constants.SHAKE_DURATION_MED;
        return Constants.SHAKE_DURATION_LOW;
    }

    private void applyCameraShake(Player player, int level) {
        float yawAmp = level >= 81 ? Constants.SHAKE_AMPLITUDE_YAW_HIGH :
                level >= 61 ? Constants.SHAKE_AMPLITUDE_YAW_MED :
                        Constants.SHAKE_AMPLITUDE_YAW_LOW;
        float pitchAmp = level >= 81 ? Constants.SHAKE_AMPLITUDE_PITCH_HIGH :
                level >= 61 ? Constants.SHAKE_AMPLITUDE_PITCH_MED :
                        Constants.SHAKE_AMPLITUDE_PITCH_LOW;

        Location loc = player.getLocation();
        float newYaw = loc.getYaw() + (random.nextFloat() - 0.5f) * yawAmp;
        float newPitch = loc.getPitch() + (random.nextFloat() - 0.5f) * pitchAmp;
        newPitch = Math.max(-90, Math.min(90, newPitch));
        player.setRotation(newYaw, newPitch);
    }

    private double getWarpChance(int level) {
        if (level >= 81) return Constants.SPEEDWARP_FREQ_HIGH_MIN / 60.0;
        if (level >= 61) return Constants.SPEEDWARP_FREQ_MED_MIN / 60.0;
        return Constants.SPEEDWARP_FREQ_LOW_MIN / 60.0;
    }

    private int getWarpDuration(int level) {
        if (level >= 81) return Constants.SPEEDWARP_DURATION_HIGH;
        if (level >= 61) return Constants.SPEEDWARP_DURATION_MED;
        return Constants.SPEEDWARP_DURATION_LOW;
    }

    private void applySpeedWarp(Player player, int level) {
        if (random.nextBoolean()) {
            player.setVelocity(player.getVelocity().multiply(1.5));
        } else {
            player.setVelocity(player.getVelocity().multiply(0.5));
        }
    }

    private double getSoundChance(int level) {
        if (level >= 81) return Constants.SOUND_FREQ_HIGH_MIN / 60.0;
        if (level >= 61) return Constants.SOUND_FREQ_MED_MIN / 60.0;
        return Constants.SOUND_FREQ_LOW_MIN / 60.0;
    }

    private Sound[] getSoundSet(int level) {
        if (level >= 81) return scarySounds;
        if (level >= 61) return neutralSounds;
        return friendlySounds;
    }

    private float getSoundVolume(int level) {
        if (level >= 81) return Constants.SOUND_VOLUME_HIGH;
        if (level >= 61) return Constants.SOUND_VOLUME_MED;
        return Constants.SOUND_VOLUME_LOW;
    }

    private double getParticleChance(int level) {
        if (level >= 81) return 1.0 / Constants.PARTICLE_FREQ_HIGH;
        if (level >= 61) return 1.0 / Constants.PARTICLE_FREQ_MED;
        return 1.0 / Constants.PARTICLE_FREQ_LOW;
    }

    private Particle[] getParticleSet(int level) {
        if (level >= 81) return scaryParticles;
        if (level >= 61) return neutralParticles;
        return friendlyParticles;
    }

    private int getParticleCount(int level) {
        if (level >= 81) return Constants.PARTICLE_COUNT_HIGH;
        if (level >= 61) return Constants.PARTICLE_COUNT_MED;
        return Constants.PARTICLE_COUNT_LOW;
    }
}