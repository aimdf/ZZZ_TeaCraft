// ==================== Файл: PlantListener.java ====================
package com.zzz.listeners;

import com.zzz.TeaBushData;
import com.zzz.Utils;
import com.zzz.ZZZ_teacraft;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.Random;

public class PlantListener implements Listener {
    private final ZZZ_teacraft plugin;

    public PlantListener(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (!item.hasItemMeta()) return;

        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if (pdc.has(plugin.getTeaBushKey(), PersistentDataType.BOOLEAN)) {
            Block block = event.getBlock();
            Location loc = block.getLocation();

            TeaBushData bushData = new TeaBushData(loc, System.currentTimeMillis(), false);
            plugin.getTeaBushes().put(loc, bushData);
            plugin.saveTeaBush(bushData);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.FERN) {
            Map<Location, TeaBushData> teaBushes = plugin.getTeaBushes();
            TeaBushData bushData = teaBushes.remove(block.getLocation());
            if (bushData != null) {
                Utils.removeParticles(block.getLocation());
                plugin.deleteTeaBush(bushData);

                event.setDropItems(false);
                block.getWorld().dropItemNaturally(block.getLocation(), plugin.createTeaBushItem());
            }
        }
    }

    @EventHandler
    public void onGrassBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Material type = block.getType();

        if (type == Material.SHORT_GRASS ||
                type == Material.TALL_GRASS ||
                type == Material.FERN ||
                type == Material.LARGE_FERN) {

            if (plugin.getTeaBushes().containsKey(block.getLocation())) {
                return;
            }

            Random random = new Random();
            if (random.nextInt(100) < 5) {
                event.setDropItems(false);
                block.setType(Material.AIR);
                block.getWorld().dropItemNaturally(
                        block.getLocation().add(0.5, 0.5, 0.5),
                        plugin.createTeaBushItem()
                );
                block.getWorld().spawnParticle(
                        Particle.HAPPY_VILLAGER,
                        block.getLocation().add(0.5, 0.5, 0.5),
                        10, 0.3, 0.3, 0.3, 0.1
                );
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

        TeaBushData bushData = plugin.getTeaBushes().get(block.getLocation());
        if (bushData == null) return;

        if (tool.getType() == Material.SHEARS && bushData.isMature()) {
            event.setCancelled(true);

            Random random = new Random();
            int fruitsAmount = random.nextInt(3) + 1;
            for (int i = 0; i < fruitsAmount; i++) {
                block.getWorld().dropItemNaturally(block.getLocation(), plugin.createTeaFruitItem(0));
            }

            if (random.nextInt(100) < 30) {
                block.getWorld().dropItemNaturally(block.getLocation(), plugin.createTeaBushItem());
            }

            Utils.removeParticles(block.getLocation());
            bushData.setMature(false);
            bushData.setPlantTime(System.currentTimeMillis());
            plugin.saveTeaBush(bushData);
        }
    }
}