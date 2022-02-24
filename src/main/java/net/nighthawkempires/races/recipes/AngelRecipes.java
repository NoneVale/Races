package net.nighthawkempires.races.recipes;

import net.nighthawkempires.races.RacesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class AngelRecipes {

    public static ItemStack itemTearOfGod() {
        ItemStack itemStack = new ItemStack(Material.GHAST_TEAR);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.ITALIC + "Tear of God");
        itemMeta.getPersistentDataContainer().set(RacesPlugin.TEAR_OF_GOD, PersistentDataType.STRING, "Tear of God");

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static boolean isTearOfGod(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.getPersistentDataContainer().has(RacesPlugin.TEAR_OF_GOD, PersistentDataType.STRING);
    }

    public ShapedRecipe recipeTearOfGod() {
        return new ShapedRecipe(RacesPlugin.TEAR_OF_GOD_RECIPE, itemTearOfGod())
                .shape("GFG", "ATA", "GFG")
                .setIngredient('G', Material.GLOWSTONE)
                .setIngredient('F', Material.FEATHER)
                .setIngredient('A', Material.GOLD_BLOCK)
                .setIngredient('T', Material.GHAST_TEAR);
    }
}