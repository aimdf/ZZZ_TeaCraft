// ==================== Файл: RecipeManager.java ====================
package com.zzz;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapelessRecipe;

public class RecipeManager {
    private final ZZZ_teacraft plugin;

    public RecipeManager(ZZZ_teacraft plugin) {
        this.plugin = plugin;
    }

    public void registerRecipes() {
        registerJointRecipe();
    }

    private void registerJointRecipe() {
        ShapelessRecipe jointRecipe = new ShapelessRecipe(
                new NamespacedKey(plugin, "tea_joint_craft"),
                plugin.createTeaJointItem()
        );
        jointRecipe.addIngredient(1, Material.PAPER);
        jointRecipe.addIngredient(1, Material.DEAD_BUSH);

        Bukkit.addRecipe(jointRecipe);
    }
}