package net.nighthawkempires.races.inventory;

import net.nighthawkempires.core.util.ItemUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.recipes.*;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import static org.bukkit.ChatColor.RED;

public class RaceRecipeListInventory {

    private Inventory getInventory(Player player, RaceType raceType) {
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        String name = raceType.getName() + " Recipes";
        Inventory inventory = Bukkit.createInventory(null, 27, name);

        switch (raceType) {
            case ANGEL -> inventory.setItem(13, AngelRecipes.itemTearOfGod());
            case DEMON -> inventory.setItem(13, DemonRecipes.itemInfernalHeart());
            case DWARF -> inventory.setItem(13, DwarfRecipes.itemMinersTrophy());
            case HUMAN -> inventory.setItem(13, HumanRecipes.itemElixirOfLife());
            case VOIDWALKER -> inventory.setItem(13, VoidwalkerRecipes.itemVoidForgedPendant());
        }

        inventory.setItem(18, ItemUtil.createSkull(RED + "Previous Page", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RjOWU0ZGNmYTQyMjFhMWZhZGMxYjViMmIxMWQ4YmVlYjU3ODc5YWYxYzQyMzYyMTQyYmFlMWVkZDUifX19"));

        return inventory;
    }


    public void open(Player player, RaceType raceType) {
        Inventory inventory = getInventory(player, raceType);
        RacesPlugin.getInventoryData().recipeListMap.put(inventory, raceType);
        player.openInventory(inventory);
    }
}