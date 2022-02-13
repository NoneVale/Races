package net.nighthawkempires.races.recipes;

import net.nighthawkempires.races.RacesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class DwarfRecipes {

    public static ItemStack itemMinersTrophy() {
        ItemStack itemStack = new ItemStack(Material.AMETHYST_SHARD);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.ITALIC + "Miner's Trophy");
        itemMeta.getPersistentDataContainer().set(RacesPlugin.MINERS_TROPHY, PersistentDataType.STRING, "Miner's Trophy");

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public ShapelessRecipe recipeMinersTrophy() {
        return new ShapelessRecipe(RacesPlugin.MINERS_TROPHY_RECIPE, itemMinersTrophy())
                .addIngredient(Material.DIAMOND_PICKAXE).addIngredient(Material.DIAMOND).addIngredient(Material.EMERALD)
                .addIngredient(Material.GOLD_INGOT).addIngredient(Material.IRON_INGOT).addIngredient(Material.COAL)
                .addIngredient(Material.REDSTONE).addIngredient(Material.LAPIS_LAZULI).addIngredient(Material.AMETHYST_SHARD);
    }
}
