// ==================== Файл: ZZZ_teacraft.java ====================
package com.zzz;

import com.zzz.listeners.*;
import com.zzz.tasks.*;
import com.zzz.commands.TeaCraftCommand;
import com.zzz.commands.TeaCraftTabCompleter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ZZZ_teacraft extends JavaPlugin {

    // ==================== NamespacedKeys ====================
    private NamespacedKey teaBushKey;
    private NamespacedKey teaFruitKey;
    private NamespacedKey teaDryKey;
    private NamespacedKey teaJointKey;
    private NamespacedKey drynessKey;
    private NamespacedKey plantTimeKey;

    // ==================== База данных ====================
    private Connection connection;
    private final Map<Location, TeaBushData> teaBushes = new ConcurrentHashMap<>();

    // ==================== Шкала напыханости ====================
    private final Map<UUID, Integer> buzzLevels = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastEffectTime = new ConcurrentHashMap<>();

    // ==================== Хранилища для эффектов ====================
    private final Map<UUID, String> distortedNames = new ConcurrentHashMap<>();
    private final Map<UUID, Long> nameDistortExpiry = new ConcurrentHashMap<>();
    private final Map<UUID, Map<Integer, String>> itemRenames = new ConcurrentHashMap<>();

    // ==================== Таски ====================
    private BukkitTask buzzTask;
    private BukkitTask jumpTask;
    private BukkitTask shakeTask;
    private BukkitTask speedWarpTask;
    private BukkitTask soundTask;
    private BukkitTask particleTask;
    private BukkitTask phantomParticleTask;
    private BukkitTask itemRenameTask;
    private BukkitTask bushGrowthTask;
    private BukkitTask itemFrameCheckTask;
    private BukkitTask cleanupTask;

    @Override
    public void onEnable() {
        initNamespacedKeys();
        registerListeners();
        initDatabase();
        loadTeaBushes();
        startAllTasks();
        registerCommands();
        registerRecipes();
        getLogger().info("ZZZ_TeaCraft включен!");
    }

    @Override
    public void onDisable() {
        saveAllTeaBushes();
        closeDatabase();
        cancelAllTasks();
        getLogger().info("ZZZ_TeaCraft выключен!");
    }

    private void initNamespacedKeys() {
        teaBushKey = new NamespacedKey(this, "tea_bush");
        teaFruitKey = new NamespacedKey(this, "tea_fruit");
        teaDryKey = new NamespacedKey(this, "tea_dry");
        teaJointKey = new NamespacedKey(this, "tea_joint");
        drynessKey = new NamespacedKey(this, "dryness");
        plantTimeKey = new NamespacedKey(this, "plant_time");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlantListener(this), this);
        getServer().getPluginManager().registerEvents(new BuzzListener(this), this);
        getServer().getPluginManager().registerEvents(new CraftListener(this), this);
        getServer().getPluginManager().registerEvents(new EffectListener(this), this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("teacraft")).setExecutor(new TeaCraftCommand(this));
        Objects.requireNonNull(getCommand("teacraft")).setTabCompleter(new TeaCraftTabCompleter());
    }

    private void startAllTasks() {
        bushGrowthTask = new BushGrowthTask(this).runTaskTimer(this, 20L, 20L);
        itemFrameCheckTask = new ItemFrameCheckTask(this).runTaskTimer(this, 20L, 100L);
        buzzTask = new BuzzDecayTask(this).runTaskTimer(this, 0L, Constants.DECAY_INTERVAL);
        particleTask = new ParticleTask(this).runTaskTimer(this, 60L, 30L);
        cleanupTask = new CleanupTask(this).runTaskTimer(this, 200L, 6000L);
        jumpTask = new JumpTask(this).runTaskTimer(this, 20L, 1L);
        shakeTask = new ShakeTask(this).runTaskTimer(this, 20L, 1L);
        speedWarpTask = new SpeedWarpTask(this).runTaskTimer(this, 20L, 1L);
        soundTask = new SoundTask(this).runTaskTimer(this, 20L, 1L);
        phantomParticleTask = new PhantomParticleTask(this).runTaskTimer(this, 20L, 1L);
        itemRenameTask = new ItemRenameTask(this).runTaskTimer(this, 20L, 200L);
    }

    private void cancelAllTasks() {
        if (bushGrowthTask != null) bushGrowthTask.cancel();
        if (itemFrameCheckTask != null) itemFrameCheckTask.cancel();
        if (buzzTask != null) buzzTask.cancel();
        if (particleTask != null) particleTask.cancel();
        if (cleanupTask != null) cleanupTask.cancel();
        if (jumpTask != null) jumpTask.cancel();
        if (shakeTask != null) shakeTask.cancel();
        if (speedWarpTask != null) speedWarpTask.cancel();
        if (soundTask != null) soundTask.cancel();
        if (phantomParticleTask != null) phantomParticleTask.cancel();
        if (itemRenameTask != null) itemRenameTask.cancel();
    }

    private void registerRecipes() {
        new RecipeManager(this).registerRecipes();
    }

    // ==================== Getters ====================
    public NamespacedKey getTeaBushKey() { return teaBushKey; }
    public NamespacedKey getTeaFruitKey() { return teaFruitKey; }
    public NamespacedKey getTeaDryKey() { return teaDryKey; }
    public NamespacedKey getTeaJointKey() { return teaJointKey; }
    public NamespacedKey getDrynessKey() { return drynessKey; }
    public NamespacedKey getPlantTimeKey() { return plantTimeKey; }
    public Map<Location, TeaBushData> getTeaBushes() { return teaBushes; }
    public Map<UUID, Integer> getBuzzLevels() { return buzzLevels; }
    public Map<UUID, Long> getLastEffectTime() { return lastEffectTime; }
    public Map<UUID, String> getDistortedNames() { return distortedNames; }
    public Map<UUID, Long> getNameDistortExpiry() { return nameDistortExpiry; }
    public Map<UUID, Map<Integer, String>> getItemRenames() { return itemRenames; }
    public Connection getConnection() { return connection; }
    public void setConnection(Connection connection) { this.connection = connection; }

    // ==================== Методы БД (делегирование) ====================
    public void initDatabase() { DatabaseManager.initDatabase(this); }
    public void checkConnection() { DatabaseManager.checkConnection(this); }
    public void closeDatabase() { DatabaseManager.closeDatabase(this); }
    public void loadTeaBushes() { DatabaseManager.loadTeaBushes(this); }
    public void saveTeaBush(TeaBushData bushData) { DatabaseManager.saveTeaBush(this, bushData); }
    public void saveTeaBushAsync(TeaBushData bushData) { DatabaseManager.saveTeaBushAsync(this, bushData); }
    public void deleteTeaBush(TeaBushData bushData) { DatabaseManager.deleteTeaBush(this, bushData); }
    public void deleteTeaBushByLocation(Location loc) { DatabaseManager.deleteTeaBushByLocation(this, loc); }
    public void deleteTeaBushAsync(TeaBushData bushData) { DatabaseManager.deleteTeaBushAsync(this, bushData); }
    public void saveAllTeaBushes() { DatabaseManager.saveAllTeaBushes(this); }
    public void saveAllTeaBushesAsync() { DatabaseManager.saveAllTeaBushesAsync(this); }

    // ==================== Методы предметов (делегирование) ====================
    public ItemStack createTeaBushItem() { return ItemFactory.createTeaBushItem(this); }
    public ItemStack createTeaFruitItem(int dryness) { return ItemFactory.createTeaFruitItem(this, dryness); }
    public ItemStack createDryTeaItem() { return ItemFactory.createDryTeaItem(this); }
    public ItemStack createTeaJointItem() { return ItemFactory.createTeaJointItem(this); }

    // ==================== Вспомогательные методы ====================
    public String getBuzzBar(int level) { return Utils.getBuzzBar(level); }
    public void spawnParticles(Location location) { Utils.spawnParticles(location); }
    public void removeParticles(Location location) { Utils.removeParticles(location); }
    public long getGlobalCooldown(int level) { return Utils.getGlobalCooldown(level); }
    public String getDefaultItemName(Material material) { return Utils.getDefaultItemName(material); }
}