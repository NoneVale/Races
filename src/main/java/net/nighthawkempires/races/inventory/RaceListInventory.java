package net.nighthawkempires.races.inventory;

import net.nighthawkempires.core.util.ItemUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.ChatColor.RED;

public class RaceListInventory {

    public enum RaceListType {
        INFO, INFECTION, RECIPES
    }

    private Inventory getInventory(Player player, RaceListType raceListType) {
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
        RaceType raceType = userModel.getRace().getRaceType();

        String name = switch (raceListType) {
            case INFO -> "Information Menu";
            case INFECTION -> "Infection Menu";
            case RECIPES -> "Recipes Menu";
            default -> "";
        };

        Inventory inventory = Bukkit.createInventory(null, 27, name);

        ItemStack itemStack = new ItemStack(Material.FEATHER, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(RaceType.ANGEL.getRaceColor() + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + RaceType.ANGEL.getName());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(9, itemStack);

        itemStack = new ItemStack(Material.BLAZE_POWDER, 1);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(RaceType.DEMON.getRaceColor() + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + RaceType.DEMON.getName());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(11, itemStack);

        itemStack = new ItemStack(Material.NETHER_STAR, 1);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(RaceType.DWARF.getRaceColor() + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + RaceType.DWARF.getName());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(13, itemStack);

        itemStack = new ItemStack(Material.APPLE, 1);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(RaceType.HUMAN.getRaceColor() + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + RaceType.HUMAN.getName());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(15, itemStack);

        /*itemStack = new ItemStack(Material.GLOW_BERRIES, 1);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(RaceType.ELF.getRaceColor() + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + RaceType.ELF.getName());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(6, itemStack);*/

        /*itemStack = new ItemStack(Material.BONE, 1);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(RaceType.LYCAN.getRaceColor() + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + RaceType.LYCAN.getName());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(14, itemStack);

        itemStack = new ItemStack(Material.SLIME_BALL, 1);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(RaceType.ORC.getRaceColor() + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + RaceType.ORC.getName());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(16, itemStack);

        inventory.setItem(18, ItemUtil.createSkull(RED + "Previous Page", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RjOWU0ZGNmYTQyMjFhMWZhZGMxYjViMmIxMWQ4YmVlYjU3ODc5YWYxYzQyMzYyMTQyYmFlMWVkZDUifX19"));

        itemStack = new ItemStack(Material.HEART_OF_THE_SEA, 1);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(RaceType.TRITON.getRaceColor() + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + RaceType.TRITON.getName());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(20, itemStack);

        itemStack = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(RaceType.VAMPIRE.getRaceColor() + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + RaceType.VAMPIRE.getName());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(22, itemStack); */

        itemStack = new ItemStack(Material.POPPED_CHORUS_FRUIT, 1);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(RaceType.VOIDWALKER.getRaceColor() + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + RaceType.VOIDWALKER.getName());
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(17, itemStack);

        inventory.setItem(18, ItemUtil.createSkull(RED + "Previous Page", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RjOWU0ZGNmYTQyMjFhMWZhZGMxYjViMmIxMWQ4YmVlYjU3ODc5YWYxYzQyMzYyMTQyYmFlMWVkZDUifX19"));

        return inventory;
    }


    public void open(Player player, RaceListType raceListType) {
        Inventory inventory = getInventory(player, raceListType);
        RacesPlugin.getInventoryData().raceListTypeMap.put(inventory, raceListType);
        player.openInventory(inventory);
    }
}
