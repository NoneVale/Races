package net.nighthawkempires.races.recipes;

import net.nighthawkempires.races.RacesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class HellForgedDiamond {

    private ItemStack itemHellForgedDiamond() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND, 1);

        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("Hell Forged Diamond");
        itemMeta.getPersistentDataContainer().set(RacesPlugin.ITEM_KEY,  PersistentDataType.INTEGER, 1);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public BlastingRecipe recipeHellForgedDiamond() {
        return new BlastingRecipe(RacesPlugin.RECIPE_KEY, itemHellForgedDiamond(), Material.DIAMOND, 0, 600);
    }

    public BlastingRecipe recipeBeef() {
        return new BlastingRecipe(RacesPlugin.BEEF_KEY, new ItemStack(Material.COOKED_BEEF, 1), Material.BEEF, 0 , 1);
    }
}
