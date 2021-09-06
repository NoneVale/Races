package net.nighthawkempires.races.inventory;

import com.google.common.collect.Lists;
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
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

import static org.bukkit.ChatColor.DARK_GRAY;

public class PerksInventory {

    private Inventory getInventory(Player player) {
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
        RaceType raceType = userModel.getRace().getRaceType();
        List<Ability> abilities = Lists.newArrayList();

        switch (userModel.getRace().getTier()) {
            case 1:
                abilities.addAll(RacesPlugin.getRaceManager().getRace(raceType, 1).getAbilities());
                break;
            case 2:
                abilities.addAll(RacesPlugin.getRaceManager().getRace(raceType, 1).getAbilities());
                abilities.addAll(RacesPlugin.getRaceManager().getRace(raceType, 2).getAbilities());
                break;
            case 3:
                abilities.addAll(RacesPlugin.getRaceManager().getRace(raceType, 1).getAbilities());
                abilities.addAll(RacesPlugin.getRaceManager().getRace(raceType, 2).getAbilities());
                abilities.addAll(RacesPlugin.getRaceManager().getRace(raceType, 3).getAbilities());
                break;
        }


        Inventory inventory = Bukkit.createInventory(null, 54, "Perk Shop");

        int count = 0;

        for (Ability ability : abilities) {
            if (ability.getId() == 0) continue;
            for (int level = 1; level <= ability.getMaxLevel(); level++) {
                ItemStack itemStack = new ItemStack(Material.RED_CONCRETE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                List<String> lore = Lists.newArrayList();

                if (userModel.hasAbility(ability) && level <= userModel.getLevel(ability)) {
                    itemStack.setType(ability.getDisplayItem());
                    itemStack.setAmount(level);

                    itemMeta.setDisplayName(raceType.getRaceColor() + ability.getName());

                    lore.add("");
                    lore.add(ChatColor.GRAY + "Type: " + ChatColor.AQUA + enumName(ability.getAbilityType().name()));
                    lore.add(ChatColor.GRAY + "Level: " + ChatColor.GOLD + level);
                    if (ability.getCooldown(level) != 0) lore.add(ChatColor.GRAY
                            + "Cooldown Time: " + ChatColor.AQUA + ability.getCooldown(level) + " seconds.");
                    lore.add("");
                    for (String s : ability.getDescription(level)) {
                        lore.add(ChatColor.GRAY + s);
                    }
                } else {
                    itemStack.setAmount(level);

                    itemMeta.setDisplayName(raceType.getRaceColor() + ability.getName());

                    lore.add(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "LOCKED");
                    lore.add("");
                    lore.add(ChatColor.GRAY + "Cost: " + ChatColor.GOLD + ability.getCost(level));
                    lore.add(ChatColor.GRAY + "Type: " + ChatColor.AQUA + enumName(ability.getAbilityType().name()));
                    lore.add(ChatColor.GRAY + "Level: " + ChatColor.GOLD + level);
                    if (ability.getCooldown(level) != 0) lore.add(ChatColor.GRAY
                            + "Cooldown Time: " + ChatColor.AQUA + ability.getCooldown(level) + " seconds.");
                    lore.add("");
                    for (String s : ability.getDescription(level)) {
                        lore.add(ChatColor.GRAY + s);
                    }
                }

                itemMeta.setLore(lore);
                itemMeta.getPersistentDataContainer().set(RacesPlugin.PERK_INVENTORY_ABILITY,
                        PersistentDataType.INTEGER_ARRAY, new int[] { ability.getId(), level });
                itemStack.setItemMeta(itemMeta);

                int slot = count + ((level - 1) * 9);
                inventory.setItem(slot, itemStack);
            }

            count++;
        }

        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Reset Perks");
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(53, itemStack);

        return inventory;
    }


    public void open(Player player) {
        Inventory inventory = getInventory(player);
        RacesPlugin.getInventoryData().perksInventoryList.add(inventory);
        player.openInventory(inventory);
    }

    private String enumName(String s) {
        if (s.contains("_")) {
            String[] split = s.split("_");

            StringBuilder matName = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                matName.append(enumName(split[i]));

                if (i < split.length - 1) {
                    matName.append(" ");
                }
            }

            return matName.toString();
        }

        return s.toUpperCase().substring(0, 1) + s.substring(1).toLowerCase();
    }
}
