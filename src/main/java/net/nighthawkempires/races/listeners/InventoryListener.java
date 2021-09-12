package net.nighthawkempires.races.listeners;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.inventory.PerksInventory;
import net.nighthawkempires.races.inventory.RaceGUIInventory;
import net.nighthawkempires.races.inventory.RaceListInventory;
import net.nighthawkempires.races.inventory.RaceRecipeInventory;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Stack;

import static net.nighthawkempires.races.RacesPlugin.*;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (getInventoryData().perksInventoryList.contains(event.getView().getTopInventory())) {
                event.setCancelled(true);
                if (getInventoryData().perksInventoryList.contains(event.getClickedInventory())) {

                    if (event.getCurrentItem() != null) {
                        int clickedSlot = event.getSlot();
                        if (clickedSlot >= 0 && clickedSlot <= 44) {
                            ItemStack itemStack = event.getCurrentItem();
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            if (itemMeta.getPersistentDataContainer().has(PERK_INVENTORY_ABILITY, PersistentDataType.INTEGER_ARRAY)) {
                                int[] abilityArray = itemMeta.getPersistentDataContainer().get(PERK_INVENTORY_ABILITY, PersistentDataType.INTEGER_ARRAY);
                                int id = abilityArray[0];
                                int level = abilityArray[1];

                                Ability ability = getAbilityManager().getAbility(id);
                                if (ability == null) return;

                                int playerLevel = userModel.getLevel(ability);
                                if (level != playerLevel + 1) {
                                    if (level <= playerLevel) {
                                        player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "You have already unlocked this ability."));
                                    } else {
                                        player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "You must unlock level " + (playerLevel + 1) + " of this ability first."));
                                    }
                                    event.setCancelled(true);
                                    return;
                                }

                                event.getWhoClicked().closeInventory();
                                userModel.addAbility(ability, level);
                                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have purchased "
                                        + ability.getRaceType().getRaceColor() + ability.getName() + ChatColor.GRAY + " Level "
                                        + ChatColor.GOLD + level + ChatColor.GRAY + "."));
                                new PerksInventory().open(player);

                                /*if (userModel.getPerkPoints() >= ability.getCost(level)) {
                                    event.getWhoClicked().closeInventory();
                                    userModel.setPerkPoints(userModel.getPerkPoints() - ability.getCost(level));
                                    userModel.addAbility(ability, level);
                                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have purchased "
                                            + ability.getRaceType().getRaceColor() + ability.getName() + ChatColor.GRAY + " Level "
                                            + ChatColor.GOLD + level + ChatColor.GRAY + "."));
                                    new PerksInventory().open(player);
                                } else {
                                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "You do not have enough perk points to buy this ability."));
                                    return;
                                }*/
                            }
                        } else if (clickedSlot == 45) {
                            event.getWhoClicked().closeInventory();
                            new RaceGUIInventory().open(player);
                        } else if (clickedSlot == 53) {
                            if (getInventoryData().perkResetList.contains(player.getUniqueId())) {
                                event.getWhoClicked().closeInventory();
                                userModel.clearAbilities();
                                //TODO: give player 75% of perk points back
                                getInventoryData().perkResetList.remove(player.getUniqueId());
                                new PerksInventory().open(player);
                            } else {
                                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Click again to reset your perks."));
                                getInventoryData().perkResetList.add(player.getUniqueId());
                            }
                        }
                    }
                }
            } else if (getInventoryData().raceGUIInventoryList.contains(event.getView().getTopInventory())) {
                event.setCancelled(true);
                if (getInventoryData().raceGUIInventoryList.contains(event.getClickedInventory())) {
                    if (event.getCurrentItem() != null) {
                        int clickedSlot = event.getSlot();

                        if (clickedSlot == 10) {
                            event.getWhoClicked().closeInventory();
                            new RaceListInventory().open(player, RaceListInventory.RaceListType.INFO);
                        } else if (clickedSlot == 12) {
                            event.getWhoClicked().closeInventory();
                            new RaceListInventory().open(player, RaceListInventory.RaceListType.INFECTION);
                        } else if (clickedSlot == 14) {
                            event.getWhoClicked().closeInventory();
                            new RaceListInventory().open(player, RaceListInventory.RaceListType.RECIPES);
                        } else if (clickedSlot == 16) {
                            event.getWhoClicked().closeInventory();
                            new PerksInventory().open(player);
                        }
                    }
                }
            } else if (getInventoryData().raceListTypeMap.containsKey(event.getView().getTopInventory())) {
                event.setCancelled(true);
                if (getInventoryData().raceListTypeMap.containsKey(event.getClickedInventory())) {
                    if (event.getCurrentItem() != null) {
                        int clickedSlot = event.getSlot();

                        if (clickedSlot == 2) {
                            event.getWhoClicked().closeInventory();
                            new RaceRecipeInventory().open(player, RaceType.CELESTIAL);
                        } else if (clickedSlot == 4) {
                            event.getWhoClicked().closeInventory();
                            new RaceRecipeInventory().open(player, RaceType.DWARF);
                        } else if (clickedSlot == 6) {
                            event.getWhoClicked().closeInventory();
                            new RaceRecipeInventory().open(player, RaceType.ELF);
                        } else if (clickedSlot == 10) {
                            event.getWhoClicked().closeInventory();
                            new RaceRecipeInventory().open(player, RaceType.HUMAN);
                        } else if (clickedSlot == 12) {
                            event.getWhoClicked().closeInventory();
                            new RaceRecipeInventory().open(player, RaceType.INFERNAL);
                        } else if (clickedSlot == 14) {
                            event.getWhoClicked().closeInventory();
                            new RaceRecipeInventory().open(player, RaceType.LYCAN);
                        } else if (clickedSlot == 16) {
                            event.getWhoClicked().closeInventory();
                            new RaceRecipeInventory().open(player, RaceType.ORC);
                        } else if (clickedSlot == 18) {
                            event.getWhoClicked().closeInventory();
                            new RaceGUIInventory().open(player);
                        } else if (clickedSlot == 20) {
                            event.getWhoClicked().closeInventory();
                            new RaceRecipeInventory().open(player, RaceType.TRITON);
                        } else if (clickedSlot == 22) {
                            event.getWhoClicked().closeInventory();
                            new RaceRecipeInventory().open(player, RaceType.VAMPIRE);
                        } else if (clickedSlot == 24) {
                            event.getWhoClicked().closeInventory();
                            new RaceRecipeInventory().open(player, RaceType.VOIDWALKER);
                        }
                    }
                }
                event.setCancelled(true);
            } else if (getInventoryData().recipeListMap.containsKey(event.getView().getTopInventory())) {
                event.setCancelled(true);
                if (getInventoryData().recipeListMap.containsKey(event.getClickedInventory())) {
                    if (event.getCurrentItem() != null) {
                        int clickedSlot = event.getSlot();

                        if (clickedSlot == 18) {
                            event.getWhoClicked().closeInventory();
                            new RaceListInventory().open(player, RaceListInventory.RaceListType.RECIPES);
                        }
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        getInventoryData().perksInventoryList.remove(event.getInventory());
        getInventoryData().raceGUIInventoryList.remove(event.getInventory());
        getInventoryData().raceListTypeMap.remove(event.getInventory());
    }
}