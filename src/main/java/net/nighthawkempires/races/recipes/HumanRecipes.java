package net.nighthawkempires.races.recipes;

import net.nighthawkempires.races.RacesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;

public class HumanRecipes {

    public static ItemStack itemElixirOfLife() {
        ItemStack itemStack = new ItemStack(Material.POTION);

        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.setColor(Color.fromRGB(85, 255, 85));
        potionMeta.setDisplayName(ChatColor.GRAY + "" + ChatColor.ITALIC + "Elixir of Life");
        potionMeta.getPersistentDataContainer().set(RacesPlugin.ELIXIR_OF_LIFE, PersistentDataType.STRING, "Elixir of Life");
        potionMeta.addEnchant(RacesPlugin.BLANK_POTION_ENCHANTMENT, 1, false);

        itemStack.setItemMeta(potionMeta);

        return itemStack;
    }

    public ShapedRecipe recipeElixirOfLife() {
        return new ShapedRecipe(RacesPlugin.ELIXIR_OF_LIFE_RECIPE, itemElixirOfLife())
                .shape("AIG", "BPB", "GMA")
                .setIngredient('A', Material.GOLDEN_APPLE)
                .setIngredient('I', Material.IRON_NUGGET)
                .setIngredient('G', Material.GLOWSTONE_DUST)
                .setIngredient('B', Material.GLOW_BERRIES)
                .setIngredient('P', Material.POTION)
                .setIngredient('M', Material.MOSS_BLOCK);
    }
}