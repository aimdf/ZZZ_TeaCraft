// ==================== Файл: ZZZ_teacraft.java (ОПТИМИЗИРОВАННЫЙ) ====================
package com.zzz;

import com.zzz.managers.*;
import com.zzz.listeners.*;
import com.zzz.tasks.*;
import com.zzz.commands.TeaCraftCommand;
import com.zzz.commands.TeaCraftTabCompleter;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.util.Map;
import java.util.UUID;
import java.util.Objects;

public final class ZZZ_teacraft extends JavaPlugin {

    // ==================== NamespacedKeys ====================
    private NamespacedKey teaBushKey;
    private NamespacedKey teaFruitKey;
    private NamespacedKey teaDryKey;
    private NamespacedKey teaJointKey;
    private NamespacedKey drynessKey;
    private NamespacedKey plantTimeKey;

    // ==================== Менеджеры ====================
    private BuzzManager buzzManager;
    private BushManager bushManager;
    private DatabaseManager databaseManager;
    private ItemManager itemManager;
    private DialogManager dialogManager;

    // ==================== База данных ====================
    private Connection connection;

    // ==================== Таски ====================
    private BukkitTask bushGrowthTask;
    private BukkitTask itemFrameCheckTask;
    private BukkitTask buzzDecayTask;
    private BukkitTask combinedEffectsTask;
    private BukkitTask itemRenameTask;
    private BukkitTask cleanupTask;
    private BukkitTask particleTask;
    private BukkitTask moistureDrainTask; // НОВЫЙ ТАСК

    @Override
    public void onEnable() {
        initNamespacedKeys();
        initManagers();
        registerListeners();
        initDatabase();
        loadTeaBushes();
        startAllTasks();
        registerCommands();
        registerRecipes();
        getLogger().info("ZZZ_TeaCraft включен! Версия: 1.3.0 (оптимизированная)");
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

    private void initManagers() {
        this.buzzManager = new BuzzManager(this);
        this.bushManager = new BushManager(this);
        this.itemManager = new ItemManager(this);
        this.databaseManager = new DatabaseManager(this);
        this.dialogManager = new DialogManager(this);
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
        bushGrowthTask = new BushGrowthTask(this).runTaskTimer(this, 20L, 1L); // Каждый тик, но с проверкой
        itemFrameCheckTask = new ItemFrameCheckTask(this).runTaskTimer(this, 20L, 100L);
        buzzDecayTask = new BuzzDecayTask(this).runTaskTimer(this, 0L, Constants.DECAY_INTERVAL);
        combinedEffectsTask = new CombinedEffectsTask(this).runTaskTimer(this, 20L, 3L); // УВЕЛИЧЕНО ДО 3 ТИКОВ
        itemRenameTask = new ItemRenameTask(this).runTaskTimer(this, 20L, 200L);
        cleanupTask = new CleanupTask(this).runTaskTimer(this, 200L, 6000L);
        particleTask = new ParticleTask(this).runTaskTimer(this, 20L, 10L);
        moistureDrainTask = new MoistureDrainTask(this).runTaskTimer(this, 1200L, 1200L); // НОВЫЙ ТАСК (каждую минуту)
    }

    private void cancelAllTasks() {
        if (bushGrowthTask != null) bushGrowthTask.cancel();
        if (itemFrameCheckTask != null) itemFrameCheckTask.cancel();
        if (buzzDecayTask != null) buzzDecayTask.cancel();
        if (combinedEffectsTask != null) combinedEffectsTask.cancel();
        if (itemRenameTask != null) itemRenameTask.cancel();
        if (cleanupTask != null) cleanupTask.cancel();
        if (particleTask != null) particleTask.cancel();
        if (moistureDrainTask != null) moistureDrainTask.cancel();
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

    public BuzzManager getBuzzManager() { return buzzManager; }
    public BushManager getBushManager() { return bushManager; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public ItemManager getItemManager() { return itemManager; }
    public DialogManager getDialogManager() { return dialogManager; }

    public Connection getConnection() { return connection; }
    public void setConnection(Connection connection) { this.connection = connection; }

    // ==================== Методы для обратной совместимости ====================
    public Map<Location, TeaBushData> getTeaBushes() { return bushManager.getTeaBushes(); }
    public Map<UUID, Integer> getBuzzLevels() { return buzzManager.getBuzzLevels(); }
    public Map<UUID, Long> getLastEffectTime() { return buzzManager.getLastEffectTime(); }
    public Map<UUID, String> getDistortedNames() { return buzzManager.getDistortedNames(); }
    public Map<UUID, Long> getNameDistortExpiry() { return buzzManager.getNameDistortExpiry(); }
    public Map<UUID, Map<Integer, String>> getItemRenames() { return buzzManager.getItemRenames(); }

    // ==================== Методы БД (делегирование) ====================
    public void initDatabase() { databaseManager.initDatabase(); }
    public void checkConnection() { databaseManager.checkConnection(); }
    public void closeDatabase() { databaseManager.closeDatabase(); }
    public void loadTeaBushes() { databaseManager.loadTeaBushes(); }
    public void saveTeaBush(TeaBushData bushData) { databaseManager.saveTeaBush(bushData); }
    public void saveTeaBushAsync(TeaBushData bushData) { databaseManager.saveTeaBushAsync(bushData); }
    public void deleteTeaBush(TeaBushData bushData) { databaseManager.deleteTeaBush(bushData); }
    public void deleteTeaBushByLocation(Location loc) { databaseManager.deleteTeaBushByLocation(loc); }
    public void deleteTeaBushAsync(TeaBushData bushData) { databaseManager.deleteTeaBushAsync(bushData); }
    public void saveAllTeaBushes() { databaseManager.saveAllTeaBushes(); }
    public void saveAllTeaBushesAsync() { databaseManager.saveAllTeaBushesAsync(); }

    // ==================== Методы предметов (делегирование) ====================
    public ItemStack createTeaBushItem() { return itemManager.createTeaBushItem(); }
    public ItemStack createTeaFruitItem(int dryness) { return itemManager.createTeaFruitItem(dryness); }
    public ItemStack createDryTeaItem() { return itemManager.createDryTeaItem(); }
    public ItemStack createTeaJointItem() { return itemManager.createTeaJointItem(); }

    // ==================== Вспомогательные методы ====================
    public String getBuzzBar(int level) { return Utils.getBuzzBar(level); }
    public long getGlobalCooldown(int level) { return buzzManager.getGlobalCooldown(level); }
}