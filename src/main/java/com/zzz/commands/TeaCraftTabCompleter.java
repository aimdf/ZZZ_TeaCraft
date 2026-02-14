// ==================== Файл: commands/TeaCraftTabCompleter.java ====================
package com.zzz.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TeaCraftTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0],
                    Arrays.asList("give", "bushinfo", "setstage"),
                    new ArrayList<>());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return null; // Предложит список игроков
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            return StringUtil.copyPartialMatches(args[2],
                    Arrays.asList("bush", "fruit", "dry", "joint"),
                    new ArrayList<>());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("setstage")) {
            return StringUtil.copyPartialMatches(args[1],
                    Arrays.asList("рост", "зрелый"),
                    new ArrayList<>());
        }

        return Collections.emptyList();
    }
}