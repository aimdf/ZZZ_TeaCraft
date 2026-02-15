// ==================== Файл: managers/DatabaseManager.java ====================
package com.zzz.managers;

import com.zzz.ZZZ_teacraft;
import com.zzz.TeaBushData;
import com.zzz.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;
import java.sql.*;
import java.util.Map;

public class DatabaseManager {
    private final ZZZ_teacraft plugin;

    public DatabaseManager(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    public void initDatabase() {
        try {
            if (!plugin.getDataFolder().exists()) {
                if (plugin.getDataFolder().mkdirs()) {
                    plugin.getLogger().info("Created plugin directory: " + plugin.getDataFolder().getAbsolutePath());
                }
            }

            File dbFile = new File(plugin.getDataFolder(), "teabushes.db");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
            plugin.setConnection(connection);

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
                stmt.execute("PRAGMA journal_mode = WAL;");
                stmt.execute("PRAGMA synchronous = NORMAL;");
            }

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

                stmt.execute("CREATE INDEX IF NOT EXISTS idx_world_coords ON tea_bushes(world, x, y, z)");
                stmt.execute("CREATE INDEX IF NOT EXISTS idx_is_mature ON tea_bushes(is_mature)");
            }

            plugin.getLogger().info("Database initialized successfully at: " + dbFile.getAbsolutePath());

        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void checkConnection() {
        try {
            Connection connection = plugin.getConnection();
            if (connection == null || connection.isClosed()) {
                plugin.getLogger().warning("Database connection lost, reconnecting...");
                initDatabase();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to check database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void closeDatabase() {
        try {
            Connection connection = plugin.getConnection();
            if (connection != null && !connection.isClosed()) {
                saveAllTeaBushes();
                connection.close();
                plugin.getLogger().info("Database connection closed");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to close database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadTeaBushes() {
        checkConnection();
        Map<Location, TeaBushData> teaBushes = plugin.getTeaBushes();
        teaBushes.clear();

        String sql = "SELECT * FROM tea_bushes";

        try (Statement stmt = plugin.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int loadedCount = 0;
            int skippedCount = 0;

            while (rs.next()) {
                String worldName = rs.getString("world");
                World world = Bukkit.getWorld(worldName);

                if (world == null) {
                    skippedCount++;
                    plugin.getLogger().warning("World '" + worldName + "' not found, skipping tea bush");
                    continue;
                }

                Location loc = new Location(world,
                        rs.getInt("x"),
                        rs.getInt("y"),
                        rs.getInt("z")
                );

                if (loc.getBlock().getType() != Material.FERN) {
                    skippedCount++;
                    deleteTeaBushByLocation(loc);
                    continue;
                }

                TeaBushData bushData = new TeaBushData(
                        loc,
                        rs.getLong("plant_time"),
                        rs.getBoolean("is_mature")
                );

                teaBushes.put(loc, bushData);
                loadedCount++;

                if (bushData.isMature()) {
                    Utils.spawnParticles(loc);
                }
            }

            plugin.getLogger().info(String.format("Loaded %d tea bushes, skipped %d invalid entries",
                    loadedCount, skippedCount));

        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to load tea bushes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveTeaBush(TeaBushData bushData) {
        if (bushData == null || bushData.getLocation() == null || bushData.getLocation().getWorld() == null) {
            plugin.getLogger().warning("Attempted to save invalid tea bush data");
            return;
        }

        checkConnection();
        Location loc = bushData.getLocation();

        String sql = """
        INSERT OR REPLACE INTO tea_bushes (world, x, y, z, plant_time, is_mature, updated_at) 
        VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
        """;

        try (PreparedStatement pstmt = plugin.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, loc.getWorld().getName());
            pstmt.setInt(2, loc.getBlockX());
            pstmt.setInt(3, loc.getBlockY());
            pstmt.setInt(4, loc.getBlockZ());
            pstmt.setLong(5, bushData.getPlantTime());
            pstmt.setBoolean(6, bushData.isMature());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save tea bush at " + loc + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveTeaBushAsync(TeaBushData bushData) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            saveTeaBush(bushData);
        });
    }

    public void deleteTeaBush(TeaBushData bushData) {
        if (bushData == null || bushData.getLocation() == null) return;
        deleteTeaBushByLocation(bushData.getLocation());
    }

    public void deleteTeaBushByLocation(Location loc) {
        if (loc == null || loc.getWorld() == null) return;

        checkConnection();
        String sql = "DELETE FROM tea_bushes WHERE world = ? AND x = ? AND y = ? AND z = ?";

        try (PreparedStatement pstmt = plugin.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, loc.getWorld().getName());
            pstmt.setInt(2, loc.getBlockX());
            pstmt.setInt(3, loc.getBlockY());
            pstmt.setInt(4, loc.getBlockZ());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to delete tea bush at " + loc + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteTeaBushAsync(TeaBushData bushData) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            deleteTeaBush(bushData);
        });
    }

    public void saveAllTeaBushes() {
        Map<Location, TeaBushData> teaBushes = plugin.getTeaBushes();
        if (teaBushes.isEmpty()) {
            plugin.getLogger().info("No tea bushes to save");
            return;
        }

        plugin.getLogger().info("Saving " + teaBushes.size() + " tea bushes...");
        int savedCount = 0;

        for (TeaBushData bushData : teaBushes.values()) {
            saveTeaBush(bushData);
            savedCount++;
        }

        plugin.getLogger().info("Saved " + savedCount + " tea bushes");
    }

    public void saveAllTeaBushesAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            saveAllTeaBushes();
        });
    }
}