package net.nighthawkempires.races.recipes;

import com.google.common.collect.Lists;
import net.nighthawkempires.races.RacesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;

public class LycanRecipes {

    public static ItemStack itemElixirOfLifeLycan() {
        ItemStack itemStack = new ItemStack(Material.POTION);

        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.setColor(Color.fromRGB(170, 0, 170));
        potionMeta.setDisplayName(ChatColor.GRAY + "" + ChatColor.ITALIC + "Elixir of Life (" + ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Lycan" + ChatColor.GRAY + ")");
        potionMeta.getPersistentDataContainer().set(RacesPlugin.ELIXIR_OF_LIFE_VAMPIRE, PersistentDataType.STRING, "Elixir of Life (Lycan)");
        potionMeta.setLore(Lists.newArrayList("",
                ChatColor.GRAY + "This Elixir of Life cures",
                ChatColor.GRAY + "infections brought by",
                ChatColor.GRAY + "lycans."));
        potionMeta.addEnchant(RacesPlugin.BLANK_POTION_ENCHANTMENT, 1, false);

        itemStack.setItemMeta(potionMeta);

        return itemStack;
    }

    public ShapedRecipe recipeElixirOfLifeLycan() {
        return new ShapedRecipe(RacesPlugin.ELIXIR_OF_LIFE_VAMPIRE_RECIPE, itemElixirOfLifeLycan())
                .shape("AOG", "BPB", "GMA")
                .setIngredient('A', Material.GOLDEN_APPLE)
                .setIngredient('O', Material.AIR)
                .setIngredient('G', Material.GLOWSTONE_DUST)
                .setIngredient('B', Material.GLOW_BERRIES)
                .setIngredient('P', Material.POTION)
                .setIngredient('M', Material.MOSS_BLOCK);
    }
}
