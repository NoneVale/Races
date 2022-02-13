package net.nighthawkempires.races.recipes;

import net.nighthawkempires.races.RacesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class TritonRecipes {

    public static ItemStack itemSeaPearl() {
        ItemStack itemStack = new ItemStack(Material.HEART_OF_THE_SEA);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.ITALIC + "Sea Pearl");
        itemMeta.getPersistentDataContainer().set(RacesPlugin.SEA_PEARL, PersistentDataType.STRING, "Sea Pearl");

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public ShapedRecipe recipeSeaPearl() {
        return new ShapedRecipe(RacesPlugin.SEA_PEARL_RECIPE, itemSeaPearl())
                .shape("DSD", "SHS", "DSD")
                .setIngredient('D', Material.GLOWSTONE_DUST)
                .setIngredient('S', Material.PRISMARINE_SHARD)
                .setIngredient('H', Material.HEART_OF_THE_SEA);
    }
}
