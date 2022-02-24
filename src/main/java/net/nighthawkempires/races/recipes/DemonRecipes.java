package net.nighthawkempires.races.recipes;

import net.nighthawkempires.races.RacesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class DemonRecipes {

    public static ItemStack itemInfernalHeart() {
        ItemStack itemStack = new ItemStack(Material.NETHER_STAR);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "" + ChatColor.ITALIC + "Infernal Heart");
        itemMeta.getPersistentDataContainer().set(RacesPlugin.INFERNAL_HEART, PersistentDataType.STRING, "Infernal Heart");

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static boolean isInfernalHeart(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.getPersistentDataContainer().has(RacesPlugin.INFERNAL_HEART, PersistentDataType.STRING);
    }

    public ShapedRecipe recipeInfernalHeart() {
        return new ShapedRecipe(RacesPlugin.INFERNAL_HEART_RECIPE, itemInfernalHeart())
                .shape("FGS", "BNB", "SGF")
                .setIngredient('F', Material.WARPED_FUNGUS)
                .setIngredient('G', Material.GOLD_INGOT)
                .setIngredient('S', Material.NETHERITE_SCRAP)
                .setIngredient('B', Material.BLAZE_ROD)
                .setIngredient('N', Material.NETHER_STAR);
    }
}