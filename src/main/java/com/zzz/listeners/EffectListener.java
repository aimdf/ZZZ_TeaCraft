// ==================== Файл: listeners/EffectListener.java ====================
package com.zzz.listeners;

import com.zzz.ZZZ_teacraft;
import com.zzz.Constants;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public class EffectListener implements Listener {
    private final ZZZ_teacraft plugin;

    public EffectListener(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int level = plugin.getBuzzLevels().getOrDefault(uuid, 0);

        if (level < Constants.CHATDISTORT_LEVEL_MIN &&
                level < Constants.CATLANG_LEVEL_MIN &&
                level < Constants.NAMEDISTORT_LEVEL_MIN) {
            return;
        }

        String message = event.getMessage();
        String originalFormat = event.getFormat();
        String playerName = player.getDisplayName();
        Random random = new Random();

        // Искажение сообщения
        if (level >= Constants.CHATDISTORT_LEVEL_MIN) {
            double chance;
            int minRepeat, maxRepeat;

            if (level >= 81) {
                chance = Constants.CHATDISTORT_CHANCE_HIGH;
                minRepeat = Constants.CHATDISTORT_REPEAT_HIGH_MIN;
                maxRepeat = Constants.CHATDISTORT_REPEAT_HIGH_MAX;
            } else if (level >= 61) {
                chance = Constants.CHATDISTORT_CHANCE_MED;
                minRepeat = Constants.CHATDISTORT_REPEAT_MED_MIN;
                maxRepeat = Constants.CHATDISTORT_REPEAT_MED_MAX;
            } else {
                chance = Constants.CHATDISTORT_CHANCE_LOW;
                minRepeat = Constants.CHATDISTORT_REPEAT_LOW_MIN;
                maxRepeat = Constants.CHATDISTORT_REPEAT_LOW_MAX;
            }

            if (random.nextDouble() < chance) {
                message = distortMessage(message, minRepeat, maxRepeat);
            }
        }

        // Кошачий язык
        if (level >= Constants.CATLANG_LEVEL_MIN) {
            double chance;
            if (level >= 81) {
                chance = Constants.CATLANG_CHANCE_HIGH;
            } else if (level >= 61) {
                chance = Constants.CATLANG_CHANCE_MED;
            } else {
                chance = Constants.CATLANG_CHANCE_LOW;
            }

            if (random.nextDouble() < chance) {
                message = addCatLanguage(message, level);
            }
        }

        // Искажение ника
        if (level >= Constants.NAMEDISTORT_LEVEL_MIN) {
            double chance;
            int maxChanges;

            if (level >= 81) {
                chance = Constants.NAMEDISTORT_CHANCE_HIGH;
                maxChanges = Constants.NAMEDISTORT_CHANGES_HIGH;
            } else if (level >= 61) {
                chance = Constants.NAMEDISTORT_CHANCE_MED;
                maxChanges = Constants.NAMEDISTORT_CHANGES_MED;
            } else {
                chance = Constants.NAMEDISTORT_CHANCE_LOW;
                maxChanges = Constants.NAMEDISTORT_CHANGES_LOW;
            }

            if (random.nextDouble() < chance) {
                String distortedName = distortName(playerName, maxChanges);
                originalFormat = originalFormat.replace(playerName, distortedName);
                plugin.getDistortedNames().put(uuid, distortedName);
                plugin.getNameDistortExpiry().put(uuid, System.currentTimeMillis() + 60000);
            }
        }

        event.setMessage(message);
        event.setFormat(originalFormat);
    }

    private String distortMessage(String message, int minRepeat, int maxRepeat) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        Pattern vowelPattern = Pattern.compile("[аеёиоуыэюяaeiou]");

        for (char c : message.toCharArray()) {
            sb.append(c);
            String s = String.valueOf(c);
            if (vowelPattern.matcher(s).matches() && random.nextInt(3) == 0) {
                int repeat = random.nextInt(maxRepeat - minRepeat + 1) + minRepeat;
                sb.append("-".repeat(repeat / 2));
                sb.append(String.valueOf(c).repeat(repeat));
            }
        }

        return sb.toString();
    }

    private String addCatLanguage(String message, int level) {
        Random random = new Random();
        String[] catWords = {" мяу", " мяу!", " мяу...", " мяу?", " няв", " мррр"};

        if (level >= 81) {
            String[] words = message.split(" ");
            if (words.length > 2) {
                int pos = random.nextInt(words.length - 1) + 1;
                words[pos] = words[pos] + catWords[random.nextInt(catWords.length)];
                return String.join(" ", words);
            }
        } else if (level >= 61) {
            if (message.endsWith(".") || message.endsWith("!") || message.endsWith("?")) {
                return message.substring(0, message.length() - 1) + catWords[random.nextInt(catWords.length)];
            }
        }

        return message + catWords[random.nextInt(catWords.length)];
    }

    private String distortName(String name, int maxChanges) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(name);
        int changes = random.nextInt(maxChanges) + 1;

        for (int i = 0; i < changes; i++) {
            int type = random.nextInt(4);
            int pos = random.nextInt(sb.length());

            switch (type) {
                case 0:
                    sb.insert(pos, (char) (random.nextInt(26) + 'a'));
                    break;
                case 1:
                    if (sb.length() > 1) {
                        sb.deleteCharAt(pos);
                    }
                    break;
                case 2:
                    char c = sb.charAt(pos);
                    if (c == 'a') sb.setCharAt(pos, '4');
                    else if (c == 'e') sb.setCharAt(pos, '3');
                    else if (c == 'o') sb.setCharAt(pos, '0');
                    else sb.setCharAt(pos, (char) (c + 1));
                    break;
                case 3:
                    if (pos < sb.length() - 1) {
                        char tmp = sb.charAt(pos);
                        sb.setCharAt(pos, sb.charAt(pos + 1));
                        sb.setCharAt(pos + 1, tmp);
                    }
                    break;
            }
        }

        return sb.toString();
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        int level = plugin.getBuzzLevels().getOrDefault(player.getUniqueId(), 0);
        if (level < Constants.MISS_LEVEL_MIN) return;

        long now = System.currentTimeMillis();
        long lastEffect = plugin.getLastEffectTime().getOrDefault(player.getUniqueId(), 0L);
        long cooldown = plugin.getGlobalCooldown(level);

        if (now - lastEffect < cooldown) return;

        Random random = new Random();
        double missChance;

        if (level >= 81) {
            missChance = Constants.MISS_CHANCE_HIGH;
        } else if (level >= 61) {
            missChance = Constants.MISS_CHANCE_MED;
        } else {
            missChance = Constants.MISS_CHANCE_LOW;
        }

        if (random.nextDouble() < missChance) {
            Arrow arrow = (Arrow) event.getProjectile();
            Vector direction = arrow.getVelocity();

            double spread = 0.5;
            direction.add(new Vector(
                    (random.nextDouble() - 0.5) * spread,
                    (random.nextDouble() - 0.5) * spread,
                    (random.nextDouble() - 0.5) * spread
            )).normalize().multiply(arrow.getVelocity().length());

            arrow.setVelocity(direction);
            plugin.getLastEffectTime().put(player.getUniqueId(), now);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int level = plugin.getBuzzLevels().getOrDefault(player.getUniqueId(), 0);
        if (level < Constants.HEADTWITCH_LEVEL_MIN) return;

        long now = System.currentTimeMillis();
        long lastEffect = plugin.getLastEffectTime().getOrDefault(player.getUniqueId(), 0L);
        long cooldown = plugin.getGlobalCooldown(level);

        if (now - lastEffect < cooldown) return;

        Random random = new Random();
        double chance;
        int maxAngle;

        if (level >= 81) {
            chance = Constants.HEADTWITCH_CHANCE_HIGH;
            maxAngle = Constants.HEADTWITCH_ANGLE_HIGH;
        } else if (level >= 61) {
            chance = Constants.HEADTWITCH_CHANCE_MED;
            maxAngle = Constants.HEADTWITCH_ANGLE_MED;
        } else {
            chance = Constants.HEADTWITCH_CHANCE_LOW;
            maxAngle = Constants.HEADTWITCH_ANGLE_LOW;
        }

        if (random.nextDouble() < chance / 20) {
            Location loc = player.getLocation();
            float newYaw = loc.getYaw() + (random.nextFloat() - 0.5f) * 2 * maxAngle;
            loc.setYaw(newYaw);
            player.teleport(loc);
            plugin.getLastEffectTime().put(player.getUniqueId(), now);
        }
    }
}