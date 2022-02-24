package net.nighthawkempires.races.recipes;

import net.nighthawkempires.races.RacesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;

public class VoidwalkerRecipes {

    public static ItemStack itemVoidForgedPendant() {
        ItemStack itemStack = new ItemStack(Material.ENDER_EYE);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Void-Forged Pendant");
        itemMeta.getPersistentDataContainer().set(RacesPlugin.VOID_FORGED_PENDANT, PersistentDataType.STRING, "Void-Forged Pendant");

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static boolean isVoidForgedPendant(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.getPersistentDataContainer().has(RacesPlugin.VOID_FORGED_PENDANT, PersistentDataType.STRING);
    }

    public ShapedRecipe recipeVoidForgedPendant() {
        return new ShapedRecipe(RacesPlugin.VOID_FORGED_PENDANT_RECIPE, itemVoidForgedPendant())
                .shape("OCO", "PEP", "ORO")
                .setIngredient('O', Material.OBSIDIAN)
                .setIngredient('C', Material.CHAIN)
                .setIngredient('P', Material.POPPED_CHORUS_FRUIT)
                .setIngredient('E', Material.END_CRYSTAL)
                .setIngredient('R', Material.END_ROD);
    }
}