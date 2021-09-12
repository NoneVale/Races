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


public class VampireRecipes {

    public static ItemStack itemElixirOfLifeVampire() {
        ItemStack itemStack = new ItemStack(Material.POTION);

        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.setColor(Color.fromRGB(170, 0, 0));
        potionMeta.setDisplayName(ChatColor.GRAY + "" + ChatColor.ITALIC + "Elixir of Life (" + ChatColor.DARK_RED + "" + ChatColor.ITALIC + "Vampire" + ChatColor.GRAY + ")");
        potionMeta.getPersistentDataContainer().set(RacesPlugin.ELIXIR_OF_LIFE_VAMPIRE, PersistentDataType.STRING, "Elixir of Life (Vampire)");
        potionMeta.setLore(Lists.newArrayList("",
                ChatColor.GRAY + "This Elixir of Life cures",
                ChatColor.GRAY + "infections brought by",
                ChatColor.GRAY + "vampires."));
        potionMeta.addEnchant(RacesPlugin.BLANK_POTION_ENCHANTMENT, 1, false);

        itemStack.setItemMeta(potionMeta);

        return itemStack;
    }

    public ShapedRecipe recipeElixirOfLifeVampire() {
        return new ShapedRecipe(RacesPlugin.ELIXIR_OF_LIFE_VAMPIRE_RECIPE, itemElixirOfLifeVampire())
                .shape("AOG", "BPB", "GMA")
                .setIngredient('A', Material.GOLDEN_APPLE)
                .setIngredient('O', Material.AIR)
                .setIngredient('G', Material.GLOWSTONE_DUST)
                .setIngredient('B', Material.GLOW_BERRIES)
                .setIngredient('P', Material.POTION)
                .setIngredient('M', Material.MOSS_BLOCK);
    }
}