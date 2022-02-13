package net.nighthawkempires.races.inventory;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.util.ItemUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RaceGUIInventory {

    private Inventory getInventory(Player player) {
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
        RaceType raceType = userModel.getRace().getRaceType();
        List<Ability> abilities = Lists.newArrayList();

        Inventory inventory = Bukkit.createInventory(null, 27, "Races Menu");

        ItemStack itemStack = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE + "Info");
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(10, itemStack);

        itemStack = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Infection");
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(12, itemStack);

        itemStack = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.YELLOW + "Recipes");
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(14, itemStack);

        itemStack = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Perks");
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(16, itemStack);

        itemStack = ItemUtil.getPlayerHead(player.getUniqueId());
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_GREEN + "Your Info");
        itemMeta.setLore(Lists.newArrayList(
                ChatColor.GRAY + "Race Type: " + userModel.getRace().getRaceType().getRaceColor() + userModel.getRace().getRaceType().getName(),
                ChatColor.GRAY + "Race: " + userModel.getRace().getRaceType().getRaceColor() + userModel.getRace().getName(),
                ChatColor.GRAY + "Perk Points: " + ChatColor.GOLD + userModel.getPerkPoints()));
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(22, itemStack);
        return inventory;
    }


    public void open(Player player) {
        Inventory inventory = getInventory(player);
        RacesPlugin.getInventoryData().raceGUIInventoryList.add(inventory);
        player.openInventory(inventory);
    }
}
