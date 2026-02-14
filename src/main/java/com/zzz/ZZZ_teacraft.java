package com.zzz;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.io.File;
import java.sql.*;
import java.util.*;

public final class ZZZ_teacraft extends JavaPlugin implements Listener {

    // ==================== ОСНОВНЫЕ КОНСТАНТЫ ====================
    private static final int GROW_TIME = 300; // 5 минут в секундах (300 * 20 = 6000 тиков)
    private static final int DRY_TIME = 300; // 5 минут для сушки
    private static final int BUZZ_INCREMENT = 20;
    private static final int WATER_REDUCTION = 30;
    private static final int NATURAL_DECAY = 1;
    private static final long DECAY_INTERVAL = 1200; // 1 минута в тиках

    // ==================== ГЛОБАЛЬНЫЙ КУЛДАУН ЭФФЕКТОВ ====================
    private static final long GLOBAL_COOLDOWN_LOW = 600; // 30 секунд для уровня <50%
    private static final long GLOBAL_COOLDOWN_MED = 400; // 20 секунд для уровня 50-80%
    private static final long GLOBAL_COOLDOWN_HIGH = 200; // 10 секунд для уровня >80%

    // ==================== НАСТРОЙКИ ЭФФЕКТОВ НАПЫХАНОСТИ ====================

    // Темнота (DARKNESS)
    private static final int DARKNESS_LEVEL_MIN = 61;
    private static final double DARKNESS_CHANCE_MED = 0.15; // 15% в минуту
    private static final double DARKNESS_CHANCE_HIGH = 0.30; // 30% в минуту
    private static final int DARKNESS_DURATION_MED = 60; // 3 секунды (60 тиков)
    private static final int DARKNESS_DURATION_HIGH = 100; // 5 секунд (100 тиков)

    // Размытость (BLUR - медлительность + слабость)
    private static final int BLUR_LEVEL_MIN = 31;
    private static final double BLUR_CHANCE_LOW = 0.10;
    private static final double BLUR_CHANCE_MED = 0.20;
    private static final double BLUR_CHANCE_HIGH = 0.30;
    private static final int BLUR_DURATION_LOW = 60; // 3 сек
    private static final int BLUR_DURATION_MED = 100; // 5 сек
    private static final int BLUR_DURATION_HIGH = 160; // 8 сек
    private static final int BLUR_AMPLIFIER_LOW = 0; // I уровень
    private static final int BLUR_AMPLIFIER_MED = 1; // II уровень
    private static final int BLUR_AMPLIFIER_HIGH = 1; // II уровень

    // Паранойя - поворот головы (HEAD_TWITCH)
    private static final int HEADTWITCH_LEVEL_MIN = 31;
    private static final double HEADTWITCH_CHANCE_LOW = 0.05;
    private static final double HEADTWITCH_CHANCE_MED = 0.10;
    private static final double HEADTWITCH_CHANCE_HIGH = 0.20;
    private static final int HEADTWITCH_ANGLE_LOW = 90; // макс 90 градусов
    private static final int HEADTWITCH_ANGLE_MED = 135; // макс 135 градусов
    private static final int HEADTWITCH_ANGLE_HIGH = 180; // макс 180 градусов

    // Паранойя - промах стрельбой (MISS_SHOT)
    private static final int MISS_LEVEL_MIN = 31;
    private static final double MISS_CHANCE_LOW = 0.15; // 15% промах
    private static final double MISS_CHANCE_MED = 0.30; // 30% промах
    private static final double MISS_CHANCE_HIGH = 0.50; // 50% промах

    // Искажение сообщений (CHAT_DISTORTION)
    private static final int CHATDISTORT_LEVEL_MIN = 31;
    private static final double CHATDISTORT_CHANCE_LOW = 0.40;
    private static final double CHATDISTORT_CHANCE_MED = 0.60;
    private static final double CHATDISTORT_CHANCE_HIGH = 0.80;
    private static final int CHATDISTORT_REPEAT_LOW_MIN = 2;
    private static final int CHATDISTORT_REPEAT_LOW_MAX = 4;
    private static final int CHATDISTORT_REPEAT_MED_MIN = 3;
    private static final int CHATDISTORT_REPEAT_MED_MAX = 5;
    private static final int CHATDISTORT_REPEAT_HIGH_MIN = 4;
    private static final int CHATDISTORT_REPEAT_HIGH_MAX = 7;

    // Искажение ника (NAME_DISTORTION)
    private static final int NAMEDISTORT_LEVEL_MIN = 31;
    private static final double NAMEDISTORT_CHANCE_LOW = 0.30;
    private static final double NAMEDISTORT_CHANCE_MED = 0.50;
    private static final double NAMEDISTORT_CHANCE_HIGH = 0.70;
    private static final int NAMEDISTORT_CHANGES_LOW = 2; // макс изменений
    private static final int NAMEDISTORT_CHANGES_MED = 3;
    private static final int NAMEDISTORT_CHANGES_HIGH = 5;

    // Кошачий язык (CAT_LANGUAGE)
    private static final int CATLANG_LEVEL_MIN = 31;
    private static final double CATLANG_CHANCE_LOW = 0.20;
    private static final double CATLANG_CHANCE_MED = 0.40;
    private static final double CATLANG_CHANCE_HIGH = 0.60;

    // Случайные прыжки (RANDOM_JUMP)
    private static final int JUMP_LEVEL_MIN = 31;
    private static final int JUMP_FREQ_LOW_MIN = 3; // раз в минуту
    private static final int JUMP_FREQ_LOW_MAX = 6;
    private static final int JUMP_FREQ_MED_MIN = 8;
    private static final int JUMP_FREQ_MED_MAX = 12;
    private static final int JUMP_FREQ_HIGH_MIN = 6;
    private static final int JUMP_FREQ_HIGH_MAX = 10;
    private static final float JUMP_POWER_LOW = 0.45f;
    private static final float JUMP_POWER_MED = 0.48f;
    private static final float JUMP_POWER_HIGH = 0.55f;
    // Дрожание камеры (SCREEN_SHAKE)
    private static final int SHAKE_LEVEL_MIN = 31;
    private static final int SHAKE_FREQ_LOW_MIN = 3;
    private static final int SHAKE_FREQ_LOW_MAX = 4;
    private static final int SHAKE_FREQ_MED_MIN = 5;
    private static final int SHAKE_FREQ_MED_MAX = 7;
    private static final int SHAKE_FREQ_HIGH_MIN = 8;
    private static final int SHAKE_FREQ_HIGH_MAX = 12;
    private static final int SHAKE_DURATION_LOW = 40; // 2 сек
    private static final int SHAKE_DURATION_MED = 60; // 3 сек
    private static final int SHAKE_DURATION_HIGH = 80; // 4 сек
    private static final float SHAKE_AMPLITUDE_YAW_LOW = 1.0f;   // Амплитуда поворота по горизонтали
    private static final float SHAKE_AMPLITUDE_YAW_MED = 2.0f;
    private static final float SHAKE_AMPLITUDE_YAW_HIGH = 3.0f;
    private static final float SHAKE_AMPLITUDE_PITCH_LOW = 0.50f; // Амплитуда поворота по вертикали
    private static final float SHAKE_AMPLITUDE_PITCH_MED = 1.0f;
    private static final float SHAKE_AMPLITUDE_PITCH_HIGH = 1.5f;

    // Искажение скорости (SPEED_WARP)
    private static final int SPEEDWARP_LEVEL_MIN = 31;
    private static final int SPEEDWARP_FREQ_LOW_MIN = 2;
    private static final int SPEEDWARP_FREQ_LOW_MAX = 3;
    private static final int SPEEDWARP_FREQ_MED_MIN = 4;
    private static final int SPEEDWARP_FREQ_MED_MAX = 5;
    private static final int SPEEDWARP_FREQ_HIGH_MIN = 6;
    private static final int SPEEDWARP_FREQ_HIGH_MAX = 8;
    private static final int SPEEDWARP_DURATION_LOW = 40; // 2 сек
    private static final int SPEEDWARP_DURATION_MED = 60; // 3 сек
    private static final int SPEEDWARP_DURATION_HIGH = 80; // 4 сек
    private static final int SPEEDWARP_AMPLIFIER_LOW = 0; // I
    private static final int SPEEDWARP_AMPLIFIER_MED = 1; // II
    private static final int SPEEDWARP_AMPLIFIER_HIGH = 1; // II

    // Случайные звуки (RANDOM_SOUNDS)
    private static final int SOUND_LEVEL_MIN = 31;
    private static final int SOUND_FREQ_LOW_MIN = 1;
    private static final int SOUND_FREQ_LOW_MAX = 2;
    private static final int SOUND_FREQ_MED_MIN = 2;
    private static final int SOUND_FREQ_MED_MAX = 4;
    private static final int SOUND_FREQ_HIGH_MIN = 4;
    private static final int SOUND_FREQ_HIGH_MAX = 6;
    private static final float SOUND_VOLUME_LOW = 0.5f;
    private static final float SOUND_VOLUME_MED = 0.8f;
    private static final float SOUND_VOLUME_HIGH = 1.0f;
    private static final float SOUND_PITCH = 1.0f;

    // Фантомные частицы (PHANTOM_PARTICLES)
    private static final int PARTICLE_LEVEL_MIN = 31;
    private static final int PARTICLE_FREQ_LOW = 200; // каждые 10 сек (200 тиков)
    private static final int PARTICLE_FREQ_MED = 100; // каждые 5 сек
    private static final int PARTICLE_FREQ_HIGH = 60; // каждые 3 сек
    private static final int PARTICLE_COUNT_LOW = 8;
    private static final int PARTICLE_COUNT_MED = 12;
    private static final int PARTICLE_COUNT_HIGH = 20;

    // Искажение предметов (ITEM_RENAME)
    private static final int ITEMRENAME_LEVEL_MIN = 31;
    private static final double ITEMRENAME_CHANCE_LOW = 0.30;
    private static final double ITEMRENAME_CHANCE_MED = 0.50;
    private static final double ITEMRENAME_CHANCE_HIGH = 0.70;
    private static final int ITEMRENAME_COUNT_LOW = 2;
    private static final int ITEMRENAME_COUNT_MED = 4;
    private static final int ITEMRENAME_COUNT_HIGH = 7;

    // NamespacedKeys для NBT
    private NamespacedKey teaBushKey;
    private NamespacedKey teaFruitKey;
    private NamespacedKey teaDryKey;
    private NamespacedKey teaJointKey;
    private NamespacedKey drynessKey;
    private NamespacedKey plantTimeKey;

    // База данных
    private Connection connection;
    private final Map<Location, TeaBushData> teaBushes = new ConcurrentHashMap<>();

    // Шкала напыханости
    private final Map<UUID, Integer> buzzLevels = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastEffectTime = new ConcurrentHashMap<>(); // Глобальный кулдаун эффектов

    private BukkitTask buzzTask;
    private BukkitTask jumpTask;
    private BukkitTask shakeTask;
    private BukkitTask speedWarpTask;
    private BukkitTask soundTask;
    private BukkitTask particleTask;
    private BukkitTask phantomParticleTask;
    private BukkitTask itemRenameTask;

    // Хранилище для искаженных ников
    private final Map<UUID, String> distortedNames = new ConcurrentHashMap<>();
    private final Map<UUID, Long> nameDistortExpiry = new ConcurrentHashMap<>();

    // Хранилище для искаженных предметов
    private final Map<UUID, Map<Integer, String>> itemRenames = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        // Инициализация NamespacedKeys
        teaBushKey = new NamespacedKey(this, "tea_bush");
        teaFruitKey = new NamespacedKey(this, "tea_fruit");
        teaDryKey = new NamespacedKey(this, "tea_dry");
        teaJointKey = new NamespacedKey(this, "tea_joint");
        drynessKey = new NamespacedKey(this, "dryness");
        plantTimeKey = new NamespacedKey(this, "plant_time");

        getServer().getPluginManager().registerEvents(this, this);

        // Инициализация БД
        initDatabase();

        // Загрузка кустов из БД
        loadTeaBushes();

        // Запуск задач
        startBushGrowthTask();
        startItemFrameCheckTask();
        startBuzzEffectsTask();
        startParticleTask();          // Постоянное обновление частиц для зрелых кустов
        startCleanupInvalidBushes(); // Периодическая очистка невалидных кустов
        startJumpTask();              // Задача для случайных прыжков
        startShakeTask();             // Задача для дрожания камеры
        startSpeedWarpTask();         // Задача для искажения скорости
        startSoundTask();             // Задача для случайных звуков
        startPhantomParticleTask();   // Задача для фантомных частиц
        startItemRenameTask();        // Задача для искажения предметов

        // Регистрация команд
        Objects.requireNonNull(getCommand("teacraft")).setExecutor(new TeaCraftCommand(this));
        Objects.requireNonNull(getCommand("teacraft")).setTabCompleter(new TeaCraftTabCompleter());

        // Регистрация крафта
        registerRecipes();

        getLogger().info("ZZZ_TeaCraft включен!");
    }

    @Override
    public void onDisable() {
        saveAllTeaBushes();
        closeDatabase();
        getLogger().info("ZZZ_TeaCraft выключен!");
    }

    // ==================== КЛАССЫ ДАННЫХ ====================

    public static class TeaBushData {
        private final Location location;
        private long plantTime;
        private boolean isMature;

        public TeaBushData(Location location, long plantTime, boolean isMature) {
            this.location = location;
            this.plantTime = plantTime;
            this.isMature = isMature;
        }

        public Location getLocation() {
            return location;
        }

        public long getPlantTime() {
            return plantTime;
        }

        public boolean isMature() {
            return isMature;
        }

        public void setMature(boolean mature) {
            isMature = mature;
        }

        public void setPlantTime(long plantTime) {
            this.plantTime = plantTime;
        }

        public int getGrowthProgress() {
            long elapsed = (System.currentTimeMillis() - plantTime) / 1000;
            return Math.min(100, (int) ((elapsed * 100) / GROW_TIME));
        }
    }

    // ==================== КОМАНДЫ ====================

    public static class TeaCraftCommand implements org.bukkit.command.CommandExecutor {
        private final ZZZ_teacraft plugin;

        public TeaCraftCommand(ZZZ_teacraft plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command,
                                 String label, String[] args) {
            if (!sender.hasPermission("teacraft.admin")) {
                sender.sendMessage(ChatColor.RED + "У вас нет прав для этой команды!");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage(ChatColor.GOLD + "=== ZZZ_TeaCraft Admin Commands ===");
                sender.sendMessage(ChatColor.YELLOW + "/teacraft give <игрок> <предмет> [количество]");
                sender.sendMessage(ChatColor.YELLOW + "/teacraft bushinfo");
                sender.sendMessage(ChatColor.YELLOW + "/teacraft setstage <рост/зрелый>");
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "give":
                    return handleGiveCommand(sender, args);
                case "bushinfo":
                    return handleBushInfoCommand(sender);
                case "setstage":
                    return handleSetStageCommand(sender, args);
                default:
                    sender.sendMessage(ChatColor.RED + "Неизвестная подкоманда!");
                    return true;
            }
        }

        private boolean handleGiveCommand(org.bukkit.command.CommandSender sender, String[] args) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Использование: /teacraft give <игрок> <предмет> [количество]");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Игрок не найден!");
                return true;
            }

            String itemType = args[2].toLowerCase();
            int amount = args.length > 3 ? Integer.parseInt(args[3]) : 1;

            ItemStack item = null;
            switch (itemType) {
                case "bush":
                case "куст":
                    item = plugin.createTeaBushItem();
                    break;
                case "fruit":
                case "плод":
                    item = plugin.createTeaFruitItem(0);
                    break;
                case "dry":
                case "сухой":
                    item = plugin.createDryTeaItem();
                    break;
                case "joint":
                case "скрутка":
                    item = plugin.createTeaJointItem();
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Неизвестный предмет! Доступны: bush, fruit, dry, joint");
                    return true;
            }

            item.setAmount(amount);
            target.getInventory().addItem(item);
            sender.sendMessage(ChatColor.GREEN + "Выдано " + amount + "x " + itemType + " игроку " + target.getName());
            return true;
        }

        private boolean handleBushInfoCommand(org.bukkit.command.CommandSender sender) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.RED + "Эту команду может использовать только игрок!");
                return true;
            }

            Block targetBlock = player.getTargetBlockExact(5);
            if (targetBlock == null || targetBlock.getType() != Material.FERN) {
                player.sendMessage(ChatColor.RED + "Вы не смотрите на куст чая!");
                return true;
            }

            TeaBushData bushData = plugin.teaBushes.get(targetBlock.getLocation());
            if (bushData == null) {
                player.sendMessage(ChatColor.RED + "Это не куст чая!");
                return true;
            }

            player.sendMessage(ChatColor.GOLD + "=== Информация о кусте ===");
            player.sendMessage(ChatColor.YELLOW + "Стадия: " + ChatColor.WHITE +
                    (bushData.isMature() ? "§aЗрелый" : "§eРастет"));
            player.sendMessage(ChatColor.YELLOW + "Прогресс: " + ChatColor.WHITE +
                    bushData.getGrowthProgress() + "%");
            return true;
        }

        private boolean handleSetStageCommand(org.bukkit.command.CommandSender sender, String[] args) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.RED + "Эту команду может использовать только игрок!");
                return true;
            }

            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Использование: /teacraft setstage <рост/зрелый>");
                return true;
            }

            Block targetBlock = player.getTargetBlockExact(5);
            if (targetBlock == null || targetBlock.getType() != Material.FERN) {
                player.sendMessage(ChatColor.RED + "Вы не смотрите на куст чая!");
                return true;
            }

            TeaBushData bushData = plugin.teaBushes.get(targetBlock.getLocation());
            if (bushData == null) {
                player.sendMessage(ChatColor.RED + "Это не куст чая!");
                return true;
            }

            String stage = args[1].toLowerCase();
            if (stage.equals("рост") || stage.equals("grow")) {
                bushData.setMature(false);
                bushData.setPlantTime(System.currentTimeMillis() - (GROW_TIME * 500L)); // 50% прогресса
                plugin.removeParticles(bushData.getLocation());
                player.sendMessage(ChatColor.GREEN + "Куст переведен в стадию роста!");
            } else if (stage.equals("зрелый") || stage.equals("mature")) {
                bushData.setMature(true);
                plugin.spawnParticles(bushData.getLocation());
                player.sendMessage(ChatColor.GREEN + "Куст переведен в зрелую стадию!");
            } else {
                player.sendMessage(ChatColor.RED + "Неверная стадия! Доступны: рост/зрелый");
                return true;
            }

            plugin.saveTeaBush(bushData);
            return true;
        }
    }

    @EventHandler
    public void onGrassBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material type = block.getType();

        // Проверяем, что это трава или высокая трава
        if (type == Material.SHORT_GRASS ||
                type == Material.TALL_GRASS ||
                type == Material.FERN ||
                type == Material.LARGE_FERN) {

            // Проверяем, что это НЕ наш культурный куст чая
            if (teaBushes.containsKey(block.getLocation())) {
                return; // Это наш посаженный куст, пропускаем
            }

            // Шанс 5% на выпадение саженца чая
            Random random = new Random();
            if (random.nextInt(100) < 5) {
                // Отменяем стандартное выпадение предметов
                event.setDropItems(false);

                // Уничтожаем блок без звука и эффектов
                block.setType(Material.AIR);

                // Спавним наш куст чая
                block.getWorld().dropItemNaturally(
                        block.getLocation().add(0.5, 0.5, 0.5),
                        createTeaBushItem()
                );

                // Добавляем частицы для визуального эффекта
                block.getWorld().spawnParticle(
                        Particle.HAPPY_VILLAGER,
                        block.getLocation().add(0.5, 0.5, 0.5),
                        10, 0.3, 0.3, 0.3, 0.1
                );
            }
        }
    }

    public static class TeaCraftTabCompleter implements org.bukkit.command.TabCompleter {
        @Override
        public List<String> onTabComplete(org.bukkit.command.CommandSender sender,
                                          org.bukkit.command.Command command,
                                          String alias, String[] args) {
            if (args.length == 1) {
                return Arrays.asList("give", "bushinfo", "setstage");
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
                return null; // Предложит список игроков
            }

            if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
                return Arrays.asList("bush", "fruit", "dry", "joint");
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("setstage")) {
                return Arrays.asList("рост", "зрелый");
            }

            return Collections.emptyList();
        }
    }

    // ==================== МЕТОДЫ СОЗДАНИЯ ПРЕДМЕТОВ ====================

    public ItemStack createTeaBushItem() {
        ItemStack item = new ItemStack(Material.FERN);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Куст чая");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Посадите на землю",
                ChatColor.GRAY + "Время роста: 5 минут"
        ));
        meta.getPersistentDataContainer().set(teaBushKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createTeaFruitItem(int dryness) {
        ItemStack item = new ItemStack(Material.SHORT_GRASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Плод чая");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Сырье для сушки");
        if (dryness > 0) {
            lore.add(ChatColor.GRAY + "Сушка: " + getProgressBar(dryness) +
                    ChatColor.WHITE + " " + dryness + "%");
        }
        meta.setLore(lore);

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(teaFruitKey, PersistentDataType.BOOLEAN, true);
        if (dryness > 0) {
            pdc.set(drynessKey, PersistentDataType.INTEGER, dryness);
        }

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createDryTeaItem() {
        ItemStack item = new ItemStack(Material.DEAD_BUSH);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Сухой чай");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Высушенный чайный лист",
                ChatColor.GRAY + "Используется для скруток"
        ));
        meta.getPersistentDataContainer().set(teaDryKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createTeaJointItem() {
        ItemStack item = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Чайная скрутка");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "ПКМ чтобы использовать",
                ChatColor.GRAY + "Дает эффект напыханости"
        ));
        meta.getPersistentDataContainer().set(teaJointKey, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    private String getProgressBar(int percent) {
        int bars = percent / 10;
        StringBuilder bar = new StringBuilder();
        bar.append(ChatColor.GREEN);
        for (int i = 0; i < bars; i++) bar.append("|");
        bar.append(ChatColor.GRAY);
        for (int i = bars; i < 10; i++) bar.append("|");
        return bar.toString();
    }

    // ==================== СИСТЕМА РОСТА КУСТОВ ====================

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (!item.hasItemMeta()) return;

        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if (pdc.has(teaBushKey, PersistentDataType.BOOLEAN)) {
            Block block = event.getBlock();
            Location loc = block.getLocation();

            TeaBushData bushData = new TeaBushData(loc, System.currentTimeMillis(), false);
            teaBushes.put(loc, bushData);
            saveTeaBush(bushData);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.FERN) {
            TeaBushData bushData = teaBushes.remove(block.getLocation());
            if (bushData != null) {
                removeParticles(block.getLocation());
                deleteTeaBush(bushData);

                // Выпадает только блок
                event.setDropItems(false);
                block.getWorld().dropItemNaturally(block.getLocation(), createTeaBushItem());
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.FERN) return;

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        TeaBushData bushData = teaBushes.get(block.getLocation());
        if (bushData == null) return;

        // Сбор ножницами
        if (tool.getType() == Material.SHEARS && bushData.isMature()) {
            event.setCancelled(true);

            // Выпадение плодов
            Random random = new Random();
            int fruitsAmount = random.nextInt(3) + 1; // 1-3 плода
            for (int i = 0; i < fruitsAmount; i++) {
                block.getWorld().dropItemNaturally(block.getLocation(), createTeaFruitItem(0));
            }

            // 30% шанс на выпадение саженца
            if (random.nextInt(100) < 30) {
                block.getWorld().dropItemNaturally(block.getLocation(), createTeaBushItem());
            }

            // Сброс куста
            removeParticles(block.getLocation());
            bushData.setMature(false);
            bushData.setPlantTime(System.currentTimeMillis());
            saveTeaBush(bushData);
        }
    }

    private void startBushGrowthTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();

                for (TeaBushData bushData : teaBushes.values()) {
                    if (bushData.isMature()) continue;

                    long elapsed = (currentTime - bushData.getPlantTime()) / 1000;
                    if (elapsed >= GROW_TIME && !bushData.isMature()) {
                        bushData.setMature(true);
                        spawnParticles(bushData.getLocation());
                        saveTeaBush(bushData);
                    }
                }
            }
        }.runTaskTimer(this, 20L, 20L); // Каждую секунду
    }

    public void spawnParticles(Location location) {
        if (location.getWorld() == null) return;

        Location center = location.clone().add(0.5, 1.0, 0.5);

        // Основные частицы - END_ROD (мерцающие)
        location.getWorld().spawnParticle(
                Particle.END_ROD,
                center,
                8, // Уменьшено для оптимизации
                0.2, 0.2, 0.2,
                0.02
        );

        // Случайные зеленые частицы для лучшей видимости
        if (ThreadLocalRandom.current().nextInt(4) == 0) { // 25% шанс
            location.getWorld().spawnParticle(
                    Particle.HAPPY_VILLAGER,
                    center,
                    4,
                    0.2, 0.3, 0.2,
                    0.1
            );
        }
    }

    public void removeParticles(Location location) {
        // Частицы исчезают сами, ничего делать не нужно
    }

    // ==================== СИСТЕМА СУШКИ ====================

    private void startItemFrameCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (org.bukkit.entity.Entity entity : player.getNearbyEntities(50, 50, 50)) {
                        if (entity instanceof ItemFrame frame) {
                            ItemStack item = frame.getItem();
                            if (item.getType() == Material.SHORT_GRASS && item.hasItemMeta()) {
                                ItemMeta meta = item.getItemMeta();
                                PersistentDataContainer pdc = meta.getPersistentDataContainer();

                                if (pdc.has(teaFruitKey, PersistentDataType.BOOLEAN)) {
                                    int dryness = pdc.getOrDefault(drynessKey, PersistentDataType.INTEGER, 0);
                                    dryness = Math.min(100, dryness + 2); // +2% за 5 секунд = 100% за 250 секунд (4+ мин)

                                    if (dryness >= 100) {
                                        // Становится сухим чаем
                                        frame.setItem(createDryTeaItem());
                                    } else {
                                        // Обновляем процент сушки
                                        frame.setItem(createTeaFruitItem(dryness));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 20L, 100L); // Каждые 5 секунд
    }

    // ==================== СИСТЕМА НАПЫХАНОСТИ ====================

    @EventHandler
    public void onJointUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.FIREWORK_ROCKET && item.hasItemMeta()) {
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
            if (pdc.has(teaJointKey, PersistentDataType.BOOLEAN)) {
                event.setCancelled(true);

                // Тратим предмет
                item.setAmount(item.getAmount() - 1);

                // Частицы дыма
                Location eyeLoc = player.getEyeLocation();
                player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,
                        eyeLoc, 30, 0.2, 0.2, 0.2, 0.05);

                // Добавляем напыханость
                UUID uuid = player.getUniqueId();
                int currentLevel = buzzLevels.getOrDefault(uuid, 0);
                buzzLevels.put(uuid, Math.min(100, currentLevel + BUZZ_INCREMENT));

                player.sendMessage(ChatColor.DARK_GREEN + "☁ " + ChatColor.GREEN +
                        "Напыханость: " + getBuzzBar(buzzLevels.get(uuid)));
            }
        }
    }

    @EventHandler
    public void onWaterDrink(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();

        // Проверяем, что это бутылка с водой (PotionType.WATER)
        if (item.getType() == Material.POTION) {
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            if (meta != null && meta.getBasePotionType() == PotionType.WATER) {
                Player player = event.getPlayer();
                UUID uuid = player.getUniqueId();
                int currentLevel = buzzLevels.getOrDefault(uuid, 0);

                if (currentLevel > 0) {
                    buzzLevels.put(uuid, Math.max(0, currentLevel - WATER_REDUCTION));
                    player.sendMessage(ChatColor.AQUA + "💧 " + ChatColor.WHITE +
                            "Вода снизила напыханость. Текущий уровень: " +
                            getBuzzBar(buzzLevels.get(uuid)));
                }
            }
        }
    }

    private void startBuzzEffectsTask() {
        buzzTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    int level = buzzLevels.getOrDefault(uuid, 0);

                    if (level <= 0) continue;

                    // Естественный спад
                    buzzLevels.put(uuid, Math.max(0, level - NATURAL_DECAY));

                    // Применяем эффекты
                    applyBuzzEffects(player, level);
                }
            }
        }.runTaskTimer(this, 0L, DECAY_INTERVAL);
    }

    private void startParticleTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                int particleCount = 0;

                for (TeaBushData bushData : teaBushes.values()) {
                    if (bushData.isMature()) {
                        Location loc = bushData.getLocation();

                        // Проверяем валидность локации
                        if (loc.getWorld() == null) continue;

                        // Проверяем, загружен ли чанк
                        if (!loc.getChunk().isLoaded()) continue;

                        // Проверяем, есть ли игроки в радиусе 32 блоков (для оптимизации)
                        boolean playersNearby = loc.getWorld().getPlayers().stream()
                                .anyMatch(player -> player.getLocation().distanceSquared(loc) < 1024); // 32^2

                        if (playersNearby) {
                            spawnParticles(loc);
                            particleCount++;
                        }
                    }
                }

                // Логирование для отладки (можно закомментировать)
                if (particleCount > 0 && getLogger().isLoggable(java.util.logging.Level.FINE)) {
                    getLogger().fine("Spawned particles for " + particleCount + " mature tea bushes");
                }
            }
        }.runTaskTimer(this, 60L, 30L); // Первый запуск через 3 секунды, затем каждые 1.5 секунды
    }

    private void startCleanupInvalidBushes() {
        new BukkitRunnable() {
            @Override
            public void run() {
                int removedCount = 0;
                Iterator<Map.Entry<Location, TeaBushData>> iterator = teaBushes.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<Location, TeaBushData> entry = iterator.next();
                    Location loc = entry.getKey();

                    // Проверяем валидность локации и блока
                    if (loc.getWorld() == null) {
                        iterator.remove();
                        deleteTeaBushByLocation(loc);
                        removedCount++;
                        continue;
                    }

                    // Проверяем, существует ли блок и является ли он fern
                    if (loc.getBlock().getType() != Material.FERN) {
                        iterator.remove();
                        deleteTeaBushByLocation(loc);
                        removedCount++;
                        continue;
                    }
                }

                if (removedCount > 0) {
                    getLogger().info("Cleaned up " + removedCount + " invalid tea bushes");
                }
            }
        }.runTaskTimer(this, 200L, 6000L); // Через 10 секунд после старта, затем каждые 5 минут
    }

    private void applyBuzzEffects(Player player, int level) {
        Random random = new Random();

        // Тошнота (раз в минуту на 5 секунд)
        if (level > 10 && random.nextInt(100) < 30) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.NAUSEA, 100, 0, false, true, true));
        }

        // Замедление (старый эффект, оставляем для обратной совместимости)
        int slownessLevel = Math.min(4, level / 25);
        if (slownessLevel > 0) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOWNESS, 40, slownessLevel - 1, false, true, true));
        }

        // Ночное зрение при 50%+
        if (level >= 50) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.NIGHT_VISION, 400, 0, false, false, true));
        }

        // НОВЫЕ ЭФФЕКТЫ:

        // 1. Темнота
        if (level >= DARKNESS_LEVEL_MIN) {
            double chance = level >= 81 ? DARKNESS_CHANCE_HIGH : DARKNESS_CHANCE_MED;
            int duration = level >= 81 ? DARKNESS_DURATION_HIGH : DARKNESS_DURATION_MED;

            if (random.nextDouble() < chance) {
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.DARKNESS, duration, 0, false, true, true));
            }
        }

        // 2. Размытость (медлительность + слабость)
        if (level >= BLUR_LEVEL_MIN) {
            double chance;
            int duration;
            int amplifier;

            if (level >= 81) {
                chance = BLUR_CHANCE_HIGH;
                duration = BLUR_DURATION_HIGH;
                amplifier = BLUR_AMPLIFIER_HIGH;
            } else if (level >= 61) {
                chance = BLUR_CHANCE_MED;
                duration = BLUR_DURATION_MED;
                amplifier = BLUR_AMPLIFIER_MED;
            } else {
                chance = BLUR_CHANCE_LOW;
                duration = BLUR_DURATION_LOW;
                amplifier = BLUR_AMPLIFIER_LOW;
            }

            if (random.nextDouble() < chance) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, amplifier, false, true, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, duration, amplifier, false, true, true));
            }
        }

        // 3. Шанс выронить предмет при 90%+ (оставляем как есть)
        if (level >= 90 && random.nextInt(100) < 15) {
            PlayerInventory inv = player.getInventory();
            int slot = player.getInventory().getHeldItemSlot();
            ItemStack item = inv.getItem(slot);
            if (item != null && item.getType() != Material.AIR) {
                inv.setItem(slot, null);
                player.getWorld().dropItemNaturally(player.getLocation(), item);
                player.sendMessage(ChatColor.RED + "☁ Ваши пальцы ослабли... вы уронили предмет!");
            }
        }
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    private long getGlobalCooldown(int level) {
        if (level >= 80) {
            return GLOBAL_COOLDOWN_HIGH;
        } else if (level >= 50) {
            return GLOBAL_COOLDOWN_MED;
        } else {
            return GLOBAL_COOLDOWN_LOW;
        }
    }

    // ==================== НОВЫЕ ЭФФЕКТЫ - ЗАДАЧИ ====================

    private void startJumpTask() {
        jumpTask = new BukkitRunnable() {
            private final Random random = new Random();
            private final Map<UUID, Long> lastJumpTime = new HashMap<>();

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    int level = buzzLevels.getOrDefault(uuid, 0);
                    if (level < JUMP_LEVEL_MIN) continue;

                    // Проверка глобального кулдауна
                    long now = System.currentTimeMillis();
                    long lastEffect = lastEffectTime.getOrDefault(uuid, 0L);
                    long cooldown = getGlobalCooldown(level);

                    if (now - lastEffect < cooldown) continue;

                    // Проверяем, не прыгали ли мы слишком недавно
                    long lastJump = lastJumpTime.getOrDefault(uuid, 0L);
                    if (now - lastJump < 1000) continue; // Не чаще раза в секунду

                    // Определяем частоту прыжков в минуту
                    int frequency;
                    float jumpPower;

                    if (level >= 81) {
                        frequency = random.nextInt(JUMP_FREQ_HIGH_MAX - JUMP_FREQ_HIGH_MIN + 1) + JUMP_FREQ_HIGH_MIN;
                        jumpPower = JUMP_POWER_HIGH;
                    } else if (level >= 61) {
                        frequency = random.nextInt(JUMP_FREQ_MED_MAX - JUMP_FREQ_MED_MIN + 1) + JUMP_FREQ_MED_MIN;
                        jumpPower = JUMP_POWER_MED;
                    } else {
                        frequency = random.nextInt(JUMP_FREQ_LOW_MAX - JUMP_FREQ_LOW_MIN + 1) + JUMP_FREQ_LOW_MIN;
                        jumpPower = JUMP_POWER_LOW;
                    }

                    // Частота в минуту -> шанс за тик (20 тиков в секунду, 1200 тиков в минуту)
                    double chancePerTick = frequency / 1200.0;

                    if (random.nextDouble() < chancePerTick) {
                        // Проверяем, на земле ли игрок
                        if (player.isOnGround()) {
                            // Устанавливаем скорость прыжка
                            Vector velocity = player.getVelocity();
                            velocity.setY(jumpPower);
                            player.setVelocity(velocity);

                            lastJumpTime.put(uuid, now);
                            lastEffectTime.put(uuid, now);
                        }
                    }
                }
            }
        }.runTaskTimer(this, 20L, 1L); // Каждый тик
    }

    private void startShakeTask() {
        shakeTask = new BukkitRunnable() {
            private final Random random = new Random();
            private final Map<UUID, Integer> shakeTicks = new HashMap<>();

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    int level = buzzLevels.getOrDefault(uuid, 0);

                    // Проверка глобального кулдауна
                    long now = System.currentTimeMillis();
                    long lastEffect = lastEffectTime.getOrDefault(uuid, 0L);
                    long cooldown = getGlobalCooldown(level);

                    if (now - lastEffect < cooldown) continue;

                    if (level >= SHAKE_LEVEL_MIN) {
                        // НЕ трясем, если игрок в воздухе
                        if (!player.isOnGround()) continue;

                        // Определяем частоту дрожания
                        int frequency;
                        int duration;
                        float yawAmplitude;
                        float pitchAmplitude;

                        if (level >= 81) {
                            frequency = random.nextInt(SHAKE_FREQ_HIGH_MAX - SHAKE_FREQ_HIGH_MIN + 1) + SHAKE_FREQ_HIGH_MIN;
                            duration = SHAKE_DURATION_HIGH;
                            yawAmplitude = SHAKE_AMPLITUDE_YAW_HIGH;
                            pitchAmplitude = SHAKE_AMPLITUDE_PITCH_HIGH;
                        } else if (level >= 61) {
                            frequency = random.nextInt(SHAKE_FREQ_MED_MAX - SHAKE_FREQ_MED_MIN + 1) + SHAKE_FREQ_MED_MIN;
                            duration = SHAKE_DURATION_MED;
                            yawAmplitude = SHAKE_AMPLITUDE_YAW_MED;
                            pitchAmplitude = SHAKE_AMPLITUDE_PITCH_MED;
                        } else {
                            frequency = random.nextInt(SHAKE_FREQ_LOW_MAX - SHAKE_FREQ_LOW_MIN + 1) + SHAKE_FREQ_LOW_MIN;
                            duration = SHAKE_DURATION_LOW;
                            yawAmplitude = SHAKE_AMPLITUDE_YAW_LOW;
                            pitchAmplitude = SHAKE_AMPLITUDE_PITCH_LOW;
                        }

                        // Если уже трясется, уменьшаем счетчик
                        if (shakeTicks.containsKey(uuid)) {
                            int ticksLeft = shakeTicks.get(uuid) - 1;
                            if (ticksLeft <= 0) {
                                shakeTicks.remove(uuid);
                            } else {
                                shakeTicks.put(uuid, ticksLeft);

                                // Меняем направление взгляда БЕЗ телепортации
                                Location loc = player.getLocation();

                                float newYaw = loc.getYaw() + (random.nextFloat() - 0.5f) * yawAmplitude;
                                float newPitch = loc.getPitch() + (random.nextFloat() - 0.5f) * pitchAmplitude;
                                newPitch = Math.max(-90, Math.min(90, newPitch));

                                // Устанавливаем новое направление без телепортации
                                player.setRotation(newYaw, newPitch);
                            }
                        } else {
                            // Проверяем, нужно ли начать тряску
                            double chancePerTick = frequency / 1200.0;
                            if (random.nextDouble() < chancePerTick) {
                                shakeTicks.put(uuid, duration);
                                lastEffectTime.put(uuid, now);
                            }
                        }
                    } else {
                        shakeTicks.remove(uuid);
                    }
                }
            }
        }.runTaskTimer(this, 20L, 1L); // Каждый тик
    }
    private void startSpeedWarpTask() {
        speedWarpTask = new BukkitRunnable() {
            private final Random random = new Random();
            private final Map<UUID, Integer> warpTicks = new HashMap<>();

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    int level = buzzLevels.getOrDefault(uuid, 0);

                    // Проверка глобального кулдауна
                    long now = System.currentTimeMillis();
                    long lastEffect = lastEffectTime.getOrDefault(uuid, 0L);
                    long cooldown = getGlobalCooldown(level);

                    if (now - lastEffect < cooldown) continue;

                    if (level >= SPEEDWARP_LEVEL_MIN) {
                        // Определяем частоту искажений
                        int frequency;
                        int duration;
                        int amplifier;

                        if (level >= 81) {
                            frequency = random.nextInt(SPEEDWARP_FREQ_HIGH_MAX - SPEEDWARP_FREQ_HIGH_MIN + 1) + SPEEDWARP_FREQ_HIGH_MIN;
                            duration = SPEEDWARP_DURATION_HIGH;
                            amplifier = SPEEDWARP_AMPLIFIER_HIGH;
                        } else if (level >= 61) {
                            frequency = random.nextInt(SPEEDWARP_FREQ_MED_MAX - SPEEDWARP_FREQ_MED_MIN + 1) + SPEEDWARP_FREQ_MED_MIN;
                            duration = SPEEDWARP_DURATION_MED;
                            amplifier = SPEEDWARP_AMPLIFIER_MED;
                        } else {
                            frequency = random.nextInt(SPEEDWARP_FREQ_LOW_MAX - SPEEDWARP_FREQ_LOW_MIN + 1) + SPEEDWARP_FREQ_LOW_MIN;
                            duration = SPEEDWARP_DURATION_LOW;
                            amplifier = SPEEDWARP_AMPLIFIER_LOW;
                        }

                        // Проверяем, активно ли искажение
                        if (warpTicks.containsKey(uuid)) {
                            int ticksLeft = warpTicks.get(uuid) - 1;
                            if (ticksLeft <= 0) {
                                warpTicks.remove(uuid);
                                // Снимаем эффекты
                                player.removePotionEffect(PotionEffectType.SPEED);
                                player.removePotionEffect(PotionEffectType.SLOWNESS);
                            } else {
                                warpTicks.put(uuid, ticksLeft);
                            }
                        } else {
                            // Проверяем, нужно ли начать искажение
                            double chancePerTick = frequency / 1200.0;
                            if (random.nextDouble() < chancePerTick) {
                                warpTicks.put(uuid, duration);
                                lastEffectTime.put(uuid, now);

                                // Случайно выбираем ускорение или замедление
                                if (random.nextBoolean()) {
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, amplifier, false, true, true));
                                } else {
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, duration, amplifier, false, true, true));
                                }
                            }
                        }
                    } else {
                        warpTicks.remove(uuid);
                        player.removePotionEffect(PotionEffectType.SPEED);
                        player.removePotionEffect(PotionEffectType.SLOWNESS);
                    }
                }
            }
        }.runTaskTimer(this, 20L, 1L); // Каждый тик
    }

    private void startSoundTask() {
        soundTask = new BukkitRunnable() {
            private final Random random = new Random();
            private final Sound[] sounds = {
                    Sound.ENTITY_CREEPER_PRIMED,
                    Sound.ENTITY_ARROW_SHOOT,
                    Sound.ENTITY_ZOMBIE_AMBIENT,
                    Sound.ENTITY_SKELETON_AMBIENT,
                    Sound.ENTITY_SPIDER_AMBIENT,
                    Sound.ENTITY_GHAST_SCREAM,
                    Sound.ENTITY_WITHER_AMBIENT,
                    Sound.ENTITY_ENDERMAN_SCREAM,
                    Sound.ENTITY_LIGHTNING_BOLT_THUNDER,
                    Sound.BLOCK_ANVIL_LAND,
                    Sound.BLOCK_CHEST_OPEN,
                    Sound.BLOCK_CHERRY_WOOD_DOOR_CLOSE,
                    Sound.BLOCK_PORTAL_AMBIENT,
                    Sound.ENTITY_TNT_PRIMED,
                    Sound.ENTITY_BLAZE_SHOOT
            };

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    int level = buzzLevels.getOrDefault(uuid, 0);
                    if (level < SOUND_LEVEL_MIN) continue;

                    // Проверка глобального кулдауна
                    long now = System.currentTimeMillis();
                    long lastEffect = lastEffectTime.getOrDefault(uuid, 0L);
                    long cooldown = getGlobalCooldown(level);

                    if (now - lastEffect < cooldown) continue;

                    // Определяем частоту звуков
                    int frequency;
                    float volume;

                    if (level >= 81) {
                        frequency = random.nextInt(SOUND_FREQ_HIGH_MAX - SOUND_FREQ_HIGH_MIN + 1) + SOUND_FREQ_HIGH_MIN;
                        volume = SOUND_VOLUME_HIGH;
                    } else if (level >= 61) {
                        frequency = random.nextInt(SOUND_FREQ_MED_MAX - SOUND_FREQ_MED_MIN + 1) + SOUND_FREQ_MED_MIN;
                        volume = SOUND_VOLUME_MED;
                    } else {
                        frequency = random.nextInt(SOUND_FREQ_LOW_MAX - SOUND_FREQ_LOW_MIN + 1) + SOUND_FREQ_LOW_MIN;
                        volume = SOUND_VOLUME_LOW;
                    }

                    // Частота в минуту -> шанс за тик
                    double chancePerTick = frequency / 1200.0;

                    if (random.nextDouble() < chancePerTick) {
                        Sound sound = sounds[random.nextInt(sounds.length)];
                        player.playSound(player.getLocation(), sound, volume, SOUND_PITCH);
                        lastEffectTime.put(uuid, now);
                    }
                }
            }
        }.runTaskTimer(this, 20L, 1L); // Каждый тик
    }

    private void startPhantomParticleTask() {
        phantomParticleTask = new BukkitRunnable() {
            private final Random random = new Random();
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

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    int level = buzzLevels.getOrDefault(uuid, 0);
                    if (level < PARTICLE_LEVEL_MIN) continue;

                    // Для частиц НЕ используем глобальный кулдаун, только свою частоту

                    // Определяем частоту и тип частиц
                    int frequency;
                    int count;
                    Particle[] particleSet;

                    if (level >= 81) {
                        frequency = PARTICLE_FREQ_HIGH;
                        count = PARTICLE_COUNT_HIGH;
                        particleSet = scaryParticles;
                    } else if (level >= 61) {
                        frequency = PARTICLE_FREQ_MED;
                        count = PARTICLE_COUNT_MED;
                        particleSet = neutralParticles;
                    } else {
                        frequency = PARTICLE_FREQ_LOW;
                        count = PARTICLE_COUNT_LOW;
                        particleSet = friendlyParticles;
                    }

                    double chancePerTick = 1.0 / frequency;

                    if (random.nextDouble() < chancePerTick) {
                        Particle particle = particleSet[random.nextInt(particleSet.length)];
                        Location loc = player.getLocation().add(
                                random.nextDouble() * 4 - 2,
                                random.nextDouble() * 3,
                                random.nextDouble() * 4 - 2
                        );
                        player.getWorld().spawnParticle(particle, loc, count, 0.2, 0.2, 0.2, 0.02);
                    }
                }
            }
        }.runTaskTimer(this, 20L, 1L); // Каждый тик
    }

    private void startItemRenameTask() {
        itemRenameTask = new BukkitRunnable() {
            private final Random random = new Random();
            private final String[] funnyNames = {
                    "Загадочная штука", "Непонятный предмет", "Глючный объект",
                    "Странная вещь", "Сомнительное нечто", "Подозрительный кусок",
                    "Магический артефакт", "Древняя реликвия", "Космический мусор",
                    "Сломанный предмет", "Чей-то мусор", "Блестяшка",
                    "Вкусняшка", "Нямка", "Хрустяшка",
                    "Тыгыдык", "Бдыщь", "Бабах",
                    "Секретный ингредиент", "Зельеварение", "Алкаголик"
            };

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    int level = buzzLevels.getOrDefault(uuid, 0);

                    // Проверка глобального кулдауна
                    long now = System.currentTimeMillis();
                    long lastEffect = lastEffectTime.getOrDefault(uuid, 0L);
                    long cooldown = getGlobalCooldown(level);

                    if (now - lastEffect < cooldown) continue;

                    if (level >= ITEMRENAME_LEVEL_MIN) {
                        // Определяем шанс и количество предметов
                        double chance;
                        int maxCount;

                        if (level >= 81) {
                            chance = ITEMRENAME_CHANCE_HIGH;
                            maxCount = ITEMRENAME_COUNT_HIGH;
                        } else if (level >= 61) {
                            chance = ITEMRENAME_CHANCE_MED;
                            maxCount = ITEMRENAME_COUNT_MED;
                        } else {
                            chance = ITEMRENAME_CHANCE_LOW;
                            maxCount = ITEMRENAME_COUNT_LOW;
                        }

                        // Проверяем каждый предмет в инвентаре
                        Map<Integer, String> renames = new HashMap<>();
                        ItemStack[] contents = player.getInventory().getContents();

                        for (int i = 0; i < contents.length; i++) {
                            ItemStack item = contents[i];
                            if (item != null && item.getType() != Material.AIR && random.nextDouble() < chance / maxCount) {
                                String funnyName = funnyNames[random.nextInt(funnyNames.length)];
                                renames.put(i, funnyName);
                            }
                        }

                        if (!renames.isEmpty()) {
                            itemRenames.put(uuid, renames);
                            lastEffectTime.put(uuid, now);
                        }
                    } else {
                        itemRenames.remove(uuid);
                    }
                }
            }
        }.runTaskTimer(this, 20L, 200L); // Каждые 10 секунд
    }

    private String getDefaultItemName(Material material) {
        String name = material.toString().toLowerCase();
        name = name.replace('_', ' ');
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    // ==================== ЧАТ ЭФФЕКТЫ ====================

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        int level = buzzLevels.getOrDefault(uuid, 0);

        if (level < CHATDISTORT_LEVEL_MIN && level < CATLANG_LEVEL_MIN && level < NAMEDISTORT_LEVEL_MIN) {
            return;
        }

        String message = event.getMessage();
        String originalFormat = event.getFormat();
        String playerName = player.getDisplayName();
        Random random = new Random();

        // 1. Искажение сообщения (растягивание гласных)
        if (level >= CHATDISTORT_LEVEL_MIN) {
            double chance;
            int minRepeat, maxRepeat;

            if (level >= 81) {
                chance = CHATDISTORT_CHANCE_HIGH;
                minRepeat = CHATDISTORT_REPEAT_HIGH_MIN;
                maxRepeat = CHATDISTORT_REPEAT_HIGH_MAX;
            } else if (level >= 61) {
                chance = CHATDISTORT_CHANCE_MED;
                minRepeat = CHATDISTORT_REPEAT_MED_MIN;
                maxRepeat = CHATDISTORT_REPEAT_MED_MAX;
            } else {
                chance = CHATDISTORT_CHANCE_LOW;
                minRepeat = CHATDISTORT_REPEAT_LOW_MIN;
                maxRepeat = CHATDISTORT_REPEAT_LOW_MAX;
            }

            if (random.nextDouble() < chance) {
                message = distortMessage(message, minRepeat, maxRepeat);
            }
        }

        // 2. Кошачий язык
        if (level >= CATLANG_LEVEL_MIN) {
            double chance;
            if (level >= 81) {
                chance = CATLANG_CHANCE_HIGH;
            } else if (level >= 61) {
                chance = CATLANG_CHANCE_MED;
            } else {
                chance = CATLANG_CHANCE_LOW;
            }

            if (random.nextDouble() < chance) {
                message = addCatLanguage(message, level);
            }
        }

        // 3. Искажение ника
        if (level >= NAMEDISTORT_LEVEL_MIN) {
            double chance;
            int maxChanges;

            if (level >= 81) {
                chance = NAMEDISTORT_CHANCE_HIGH;
                maxChanges = NAMEDISTORT_CHANGES_HIGH;
            } else if (level >= 61) {
                chance = NAMEDISTORT_CHANCE_MED;
                maxChanges = NAMEDISTORT_CHANGES_MED;
            } else {
                chance = NAMEDISTORT_CHANCE_LOW;
                maxChanges = NAMEDISTORT_CHANGES_LOW;
            }

            if (random.nextDouble() < chance) {
                String distortedName = distortName(playerName, maxChanges);
                // Обновляем формат с искаженным ником
                originalFormat = originalFormat.replace(playerName, distortedName);
                // Сохраняем для других эффектов (над головой)
                distortedNames.put(uuid, distortedName);
                nameDistortExpiry.put(uuid, System.currentTimeMillis() + 60000); // 1 минута
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
            if (vowelPattern.matcher(s).matches() && random.nextInt(3) == 0) { // 33% шанс на растяжение
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
            // Вставляем в середину или несколько раз
            String[] words = message.split(" ");
            if (words.length > 2) {
                int pos = random.nextInt(words.length - 1) + 1;
                words[pos] = words[pos] + catWords[random.nextInt(catWords.length)];
                return String.join(" ", words);
            }
        } else if (level >= 61) {
            // Заменяем знаки препинания
            if (message.endsWith(".") || message.endsWith("!") || message.endsWith("?")) {
                return message.substring(0, message.length() - 1) + catWords[random.nextInt(catWords.length)];
            }
        }

        // Просто добавляем в конец
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
                case 0: // Добавить случайный символ
                    sb.insert(pos, (char) (random.nextInt(26) + 'a'));
                    break;
                case 1: // Удалить символ
                    if (sb.length() > 1) {
                        sb.deleteCharAt(pos);
                    }
                    break;
                case 2: // Заменить на похожий
                    char c = sb.charAt(pos);
                    if (c == 'a') sb.setCharAt(pos, '4');
                    else if (c == 'e') sb.setCharAt(pos, '3');
                    else if (c == 'o') sb.setCharAt(pos, '0');
                    else sb.setCharAt(pos, (char) (c + 1));
                    break;
                case 3: // Переставить соседние символы
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

    // ==================== ЭФФЕКТ ПРОМАХА СТРЕЛЬБОЙ ====================

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        int level = buzzLevels.getOrDefault(player.getUniqueId(), 0);
        if (level < MISS_LEVEL_MIN) return;

        // Проверка глобального кулдауна
        long now = System.currentTimeMillis();
        long lastEffect = lastEffectTime.getOrDefault(player.getUniqueId(), 0L);
        long cooldown = getGlobalCooldown(level);

        if (now - lastEffect < cooldown) return;

        Random random = new Random();
        double missChance;

        if (level >= 81) {
            missChance = MISS_CHANCE_HIGH;
        } else if (level >= 61) {
            missChance = MISS_CHANCE_MED;
        } else {
            missChance = MISS_CHANCE_LOW;
        }

        if (random.nextDouble() < missChance) {
            // Добавляем случайное отклонение к направлению стрелы
            org.bukkit.entity.Arrow arrow = (org.bukkit.entity.Arrow) event.getProjectile();
            Vector direction = arrow.getVelocity();

            double spread = 0.5; // Радиус разброса
            direction.add(new Vector(
                    (random.nextDouble() - 0.5) * spread,
                    (random.nextDouble() - 0.5) * spread,
                    (random.nextDouble() - 0.5) * spread
            )).normalize().multiply(arrow.getVelocity().length());

            arrow.setVelocity(direction);
            lastEffectTime.put(player.getUniqueId(), now);
        }
    }

    // ==================== ЭФФЕКТ ПОВОРОТА ГОЛОВЫ ====================

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int level = buzzLevels.getOrDefault(player.getUniqueId(), 0);
        if (level < HEADTWITCH_LEVEL_MIN) return;

        // Проверка глобального кулдауна
        long now = System.currentTimeMillis();
        long lastEffect = lastEffectTime.getOrDefault(player.getUniqueId(), 0L);
        long cooldown = getGlobalCooldown(level);

        if (now - lastEffect < cooldown) return;

        Random random = new Random();
        double chance;
        int maxAngle;

        if (level >= 81) {
            chance = HEADTWITCH_CHANCE_HIGH;
            maxAngle = HEADTWITCH_ANGLE_HIGH;
        } else if (level >= 61) {
            chance = HEADTWITCH_CHANCE_MED;
            maxAngle = HEADTWITCH_ANGLE_MED;
        } else {
            chance = HEADTWITCH_CHANCE_LOW;
            maxAngle = HEADTWITCH_ANGLE_LOW;
        }

        // Проверяем каждый тик с шансом
        if (random.nextDouble() < chance / 20) { // Делим на 20, так как MoveEvent вызывается часто
            Location loc = player.getLocation();
            float newYaw = loc.getYaw() + (random.nextFloat() - 0.5f) * 2 * maxAngle;
            loc.setYaw(newYaw);
            player.teleport(loc);
            lastEffectTime.put(player.getUniqueId(), now);
        }
    }

    // ==================== КРАФТ ====================

    private void registerRecipes() {
        // Крафт скруток
        ShapelessRecipe jointRecipe = new ShapelessRecipe(
                new NamespacedKey(this, "tea_joint_craft"),
                createTeaJointItem()
        );
        jointRecipe.addIngredient(1, Material.PAPER);
        jointRecipe.addIngredient(1, Material.DEAD_BUSH);

        Bukkit.addRecipe(jointRecipe);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (event.isCancelled()) return;

        // Проверяем, что это крафт нашей скрутки
        ItemStack result = event.getCurrentItem();
        if (result == null || result.getType() != Material.FIREWORK_ROCKET) return;

        // Проверяем, что использован сухой чай с NBT
        boolean hasDryTea = false;

        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item != null && item.getType() == Material.DEAD_BUSH && item.hasItemMeta()) {
                PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
                if (pdc.has(teaDryKey, PersistentDataType.BOOLEAN)) {
                    hasDryTea = true;
                    break;
                }
            }
        }

        if (!hasDryTea) {
            // Если нет сухого чая, отменяем крафт
            event.setCancelled(true);
            return;
        }

        // Обработка Shift+ПКМ
        if (event.isShiftClick()) {
            // При Shift+ПКМ нужно обработать все возможные крафты
            Bukkit.getScheduler().runTask(this, () -> {
                ItemStack[] matrix = event.getInventory().getMatrix();
                int maxCrafts = getMaxCrafts(matrix);
                ItemStack jointItem = createTeaJointItem();
                jointItem.setAmount(2 * maxCrafts);

                // Добавляем предметы игроку
                Player player = (Player) event.getWhoClicked();
                HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(jointItem);

                // Если не влезло, выбрасываем
                if (!leftover.isEmpty()) {
                    for (ItemStack item : leftover.values()) {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                }

                // Убираем использованные ингредиенты
                for (int i = 0; i < matrix.length; i++) {
                    if (matrix[i] != null) {
                        matrix[i].setAmount(matrix[i].getAmount() - 1);
                    }
                }
                event.getInventory().setMatrix(matrix);
            });
        } else {
            // Обычный крафт
            event.setCurrentItem(createTeaJointItem());
            event.getCurrentItem().setAmount(2); // 2 скрутки
        }
    }

    private int getMaxCrafts(ItemStack[] matrix) {
        int paperCount = 0;
        int dryTeaCount = 0;

        for (ItemStack item : matrix) {
            if (item == null) continue;

            if (item.getType() == Material.PAPER) {
                paperCount += item.getAmount();
            } else if (item.getType() == Material.DEAD_BUSH && item.hasItemMeta()) {
                PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
                if (pdc.has(teaDryKey, PersistentDataType.BOOLEAN)) {
                    dryTeaCount += item.getAmount();
                }
            }
        }

        return Math.min(paperCount, dryTeaCount);
    }

    // ==================== БАЗА ДАННЫХ ====================

    private void initDatabase() {
        try {
            // Создаем директорию плагина, если её нет
            if (!getDataFolder().exists()) {
                if (getDataFolder().mkdirs()) {
                    getLogger().info("Created plugin directory: " + getDataFolder().getAbsolutePath());
                }
            }

            // Используем путь внутри папки плагина
            File dbFile = new File(getDataFolder(), "teabushes.db");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());

            // Включаем FOREIGN KEYS и другие оптимизации
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
                stmt.execute("PRAGMA journal_mode = WAL;");  // Write-Ahead Logging для производительности
                stmt.execute("PRAGMA synchronous = NORMAL;"); // Баланс скорости и надежности
            }

            // Создаем таблицу с правильными типами данных
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("""
                CREATE TABLE IF NOT EXISTS tea_bushes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    world VARCHAR(64) NOT NULL,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    z INTEGER NOT NULL,
                    plant_time BIGINT NOT NULL,
                    is_mature BOOLEAN NOT NULL DEFAULT 0,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    UNIQUE(world, x, y, z)
                )
                """);

                // Создаем индексы для быстрого поиска
                stmt.execute("CREATE INDEX IF NOT EXISTS idx_world_coords ON tea_bushes(world, x, y, z)");
                stmt.execute("CREATE INDEX IF NOT EXISTS idx_is_mature ON tea_bushes(is_mature)");
            }

            getLogger().info("Database initialized successfully at: " + dbFile.getAbsolutePath());

        } catch (SQLException e) {
            getLogger().severe("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void checkConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                getLogger().warning("Database connection lost, reconnecting...");
                initDatabase();
            }
        } catch (SQLException e) {
            getLogger().severe("Failed to check database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void closeDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                // Сохраняем все перед закрытием
                saveAllTeaBushes();
                connection.close();
                getLogger().info("Database connection closed");
            }
        } catch (SQLException e) {
            getLogger().severe("Failed to close database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadTeaBushes() {
        checkConnection();
        teaBushes.clear();

        String sql = "SELECT * FROM tea_bushes";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int loadedCount = 0;
            int skippedCount = 0;

            while (rs.next()) {
                String worldName = rs.getString("world");
                World world = Bukkit.getWorld(worldName);

                if (world == null) {
                    skippedCount++;
                    getLogger().warning("World '" + worldName + "' not found, skipping tea bush");
                    continue;
                }

                Location loc = new Location(world,
                        rs.getInt("x"),
                        rs.getInt("y"),
                        rs.getInt("z")
                );

                // Проверяем, существует ли блок до сих пор
                if (loc.getBlock().getType() != Material.FERN) {
                    skippedCount++;
                    deleteTeaBushByLocation(loc); // Очищаем невалидные записи
                    continue;
                }

                TeaBushData bushData = new TeaBushData(
                        loc,
                        rs.getLong("plant_time"),
                        rs.getBoolean("is_mature")
                );

                teaBushes.put(loc, bushData);
                loadedCount++;

                // Восстанавливаем частицы для зрелых кустов
                if (bushData.isMature()) {
                    spawnParticles(loc);
                }
            }

            getLogger().info(String.format("Loaded %d tea bushes, skipped %d invalid entries",
                    loadedCount, skippedCount));

        } catch (SQLException e) {
            getLogger().severe("Failed to load tea bushes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveTeaBush(TeaBushData bushData) {
        if (bushData == null || bushData.getLocation() == null || bushData.getLocation().getWorld() == null) {
            getLogger().warning("Attempted to save invalid tea bush data");
            return;
        }

        checkConnection();
        Location loc = bushData.getLocation();

        String sql = """
        INSERT OR REPLACE INTO tea_bushes (world, x, y, z, plant_time, is_mature, updated_at) 
        VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, loc.getWorld().getName());
            pstmt.setInt(2, loc.getBlockX());
            pstmt.setInt(3, loc.getBlockY());
            pstmt.setInt(4, loc.getBlockZ());
            pstmt.setLong(5, bushData.getPlantTime());
            pstmt.setBoolean(6, bushData.isMature());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            getLogger().severe("Failed to save tea bush at " + loc + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveTeaBushAsync(TeaBushData bushData) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            saveTeaBush(bushData);
        });
    }

    private void deleteTeaBush(TeaBushData bushData) {
        if (bushData == null || bushData.getLocation() == null) return;
        deleteTeaBushByLocation(bushData.getLocation());
    }

    private void deleteTeaBushByLocation(Location loc) {
        if (loc == null || loc.getWorld() == null) return;

        checkConnection();
        String sql = "DELETE FROM tea_bushes WHERE world = ? AND x = ? AND y = ? AND z = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, loc.getWorld().getName());
            pstmt.setInt(2, loc.getBlockX());
            pstmt.setInt(3, loc.getBlockY());
            pstmt.setInt(4, loc.getBlockZ());
            int deleted = pstmt.executeUpdate();

            if (deleted > 0) {
                getLogger().fine("Deleted tea bush at " + loc);
            }

        } catch (SQLException e) {
            getLogger().severe("Failed to delete tea bush at " + loc + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteTeaBushAsync(TeaBushData bushData) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            deleteTeaBush(bushData);
        });
    }

    private void saveAllTeaBushes() {
        if (teaBushes.isEmpty()) {
            getLogger().info("No tea bushes to save");
            return;
        }

        getLogger().info("Saving " + teaBushes.size() + " tea bushes...");
        int savedCount = 0;

        for (TeaBushData bushData : teaBushes.values()) {
            saveTeaBush(bushData);
            savedCount++;
        }

        getLogger().info("Saved " + savedCount + " tea bushes");
    }

    private void saveAllTeaBushesAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            saveAllTeaBushes();
        });
    }

    private String getBuzzBar(int level) {
        int filled = level / 10;
        StringBuilder bar = new StringBuilder();
        bar.append(ChatColor.GREEN);
        for (int i = 0; i < filled; i++) bar.append("▮");
        bar.append(ChatColor.GRAY);
        for (int i = filled; i < 10; i++) bar.append("▯");
        bar.append(ChatColor.WHITE).append(" ").append(level).append("%");
        return bar.toString();
    }
}