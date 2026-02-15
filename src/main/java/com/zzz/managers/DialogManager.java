// ==================== Файл: managers/DialogManager.java ====================
package com.zzz.managers;

import com.zzz.ZZZ_teacraft;
import com.zzz.TeaBushData;

import org.bukkit.entity.Player;

public class DialogManager {
    private final ZZZ_teacraft plugin;

    public DialogManager(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    /**
     * Показывает информацию о кусте в чате (минималистичный вариант)
     */
    public void showBushInfo(Player player, TeaBushData bush) {
        int moisture = bush.getMoisture();
        int growth = bush.getEffectiveGrowthProgress();
        String timeLeft = bush.getFormattedTimeLeft();
        String stage = bush.isMature() ? "§aСОЗРЕЛ" : "§eРАСТЕТ";

        player.sendMessage("§7Влажность: §b" + moisture + "%");
        player.sendMessage("§7Рост: §a" + growth + "%");
        player.sendMessage("§7До созревания: §e" + timeLeft);
        player.sendMessage("§7Стадия: " + stage);
    }
}