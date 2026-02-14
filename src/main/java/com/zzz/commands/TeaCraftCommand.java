// ==================== Файл: commands/TeaCraftCommand.java ====================
package com.zzz.commands;

import com.zzz.ZZZ_teacraft;
import com.zzz.TeaBushData;
import com.zzz.Constants;
import com.zzz.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TeaCraftCommand implements CommandExecutor {
    private final ZZZ_teacraft plugin;

    public TeaCraftCommand(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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

    private boolean handleGiveCommand(CommandSender sender, String[] args) {
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

    private boolean handleBushInfoCommand(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду может использовать только игрок!");
            return true;
        }

        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock == null || targetBlock.getType() != Material.FERN) {
            player.sendMessage(ChatColor.RED + "Вы не смотрите на куст чая!");
            return true;
        }

        TeaBushData bushData = plugin.getTeaBushes().get(targetBlock.getLocation());
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

    private boolean handleSetStageCommand(CommandSender sender, String[] args) {
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

        TeaBushData bushData = plugin.getTeaBushes().get(targetBlock.getLocation());
        if (bushData == null) {
            player.sendMessage(ChatColor.RED + "Это не куст чая!");
            return true;
        }

        String stage = args[1].toLowerCase();
        if (stage.equals("рост") || stage.equals("grow")) {
            bushData.setMature(false);
            bushData.setPlantTime(System.currentTimeMillis() - (Constants.GROW_TIME * 500L));
            Utils.removeParticles(bushData.getLocation());
            player.sendMessage(ChatColor.GREEN + "Куст переведен в стадию роста!");
        } else if (stage.equals("зрелый") || stage.equals("mature")) {
            bushData.setMature(true);
            Utils.spawnParticles(bushData.getLocation());
            player.sendMessage(ChatColor.GREEN + "Куст переведен в зрелую стадию!");
        } else {
            player.sendMessage(ChatColor.RED + "Неверная стадия! Доступны: рост/зрелый");
            return true;
        }

        plugin.saveTeaBush(bushData);
        return true;
    }
}