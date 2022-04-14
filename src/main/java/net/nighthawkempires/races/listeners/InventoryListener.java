package net.nighthawkempires.races.listeners;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.event.AbilitiesResetEvent;
import net.nighthawkempires.races.event.AbilityUnlockEvent;
import net.nighthawkempires.races.event.RaceChangeEvent;
import net.nighthawkempires.races.event.RaceUpgradeEvent;
import net.nighthawkempires.races.inventory.*;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.recipes.*;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static net.nighthawkempires.races.RacesPlugin.*;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
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

                                if (userModel.getPerkPoints() >= ability.getCost(level)) {
                                    event.getWhoClicked().closeInventory();
                                    userModel.setPerkPoints(userModel.getPerkPoints() - ability.getCost(level));
                                    userModel.addSpentPoints(ability.getCost(level));
                                    Bukkit.getPluginManager().callEvent(new AbilityUnlockEvent(player, ability));
                                    userModel.addAbility(ability, level);
                                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have purchased "
                                            + ability.getRaceType().getRaceColor() + ability.getName() + ChatColor.GRAY + " Level "
                                            + ChatColor.GOLD + level + ChatColor.GRAY + "."));
                                    new PerksInventory().open(player);
                                } else {
                                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "You do not have enough perk points to buy this ability."));
                                }
                            }
                        } else if (clickedSlot == 45) {
                            event.getWhoClicked().closeInventory();
                            new RaceGUIInventory().open(player);
                        } else if (clickedSlot == 49) {
                            ItemStack itemStack = event.getCurrentItem();
                            if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.getAmount() > 0) {
                                int nextTier = userModel.getRace().getTier() + 1;
                                int cost = userModel.getRace().getTier() * 5;

                                if (userModel.getPerkPoints() >= cost) {
                                    event.getWhoClicked().closeInventory();
                                    userModel.removePerkPoints(cost);
                                    Race race = RacesPlugin.getRaceManager().getRace(userModel.getRace().getRaceType(), nextTier);
                                    Bukkit.getPluginManager().callEvent(new RaceUpgradeEvent(player, race));
                                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have ranked up to "
                                            + userModel.getRace().getRaceType().getRaceColor() + race.getName() + ChatColor.GRAY + "."));
                                    new PerksInventory().open(player);
                                } else {
                                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "You do not have enough perk points to rank up."));
                                }
                            }
                        } else if (clickedSlot == 51) {
                            net.nighthawkempires.core.user.UserModel tokens = CorePlugin.getUserRegistry().getUser(player.getUniqueId());
                            if (tokens.getTokens() >= 10) {
                                event.getWhoClicked().closeInventory();
                                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have purchased a Perk Point for " + ChatColor.GOLD + "10 tokens" + ChatColor.GRAY + "."));
                                tokens.removeTokens(10);
                                userModel.addPerkPoints(1);
                                new PerksInventory().open(player);
                            }
                        } else if (clickedSlot == 53) {
                            if (getInventoryData().perkResetList.contains(player.getUniqueId())) {
                                event.getWhoClicked().closeInventory();
                                Bukkit.getPluginManager().callEvent(new AbilitiesResetEvent(player, Lists.newArrayList(userModel.getAbilities())));
                                userModel.clearAbilities();

                                int returned = (int) (userModel.getSpentPoints() * .75);
                                userModel.setSpentPoints(0);
                                userModel.addPerkPoints(returned);

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

                        RaceListInventory.RaceListType listType = getInventoryData().raceListTypeMap.get(event.getClickedInventory());
                        switch (listType) {
                            case INFECTION -> {
                                if (clickedSlot == 9 || clickedSlot == 11 || clickedSlot == 13 || clickedSlot == 15 || clickedSlot == 17) event.getWhoClicked().closeInventory();
                                switch (clickedSlot) {
                                    case 9 -> Bukkit.dispatchCommand(player, "race infection angel");
                                    case 11 -> Bukkit.dispatchCommand(player, "race infection demon");
                                    case 13 -> Bukkit.dispatchCommand(player, "race infection dwarf");
                                    case 15 -> Bukkit.dispatchCommand(player, "race infection human");
                                    case 17 -> Bukkit.dispatchCommand(player, "race infection voidwalker");
                                    default -> {}
                                }
                            }
                            case INFO -> {
                                if (clickedSlot == 9 || clickedSlot == 11 || clickedSlot == 13 || clickedSlot == 15 || clickedSlot == 17) event.getWhoClicked().closeInventory();
                                switch (clickedSlot) {
                                    case 9 -> Bukkit.dispatchCommand(player, "race info angel");
                                    case 11 -> Bukkit.dispatchCommand(player, "race info demon");
                                    case 13 -> Bukkit.dispatchCommand(player, "race info dwarf");
                                    case 15 -> Bukkit.dispatchCommand(player, "race info human");
                                    case 17 -> Bukkit.dispatchCommand(player, "race info voidwalker");
                                    default -> {}
                                }
                            }
                            case RECIPES -> {
                                switch (clickedSlot) {
                                    case 9 -> {
                                        event.getWhoClicked().closeInventory();
                                        new RaceRecipeListInventory().open(player, RaceType.ANGEL);
                                    }
                                    case 11 -> {
                                        event.getWhoClicked().closeInventory();
                                        new RaceRecipeListInventory().open(player, RaceType.DEMON);
                                    }
                                    case 13 -> {
                                        event.getWhoClicked().closeInventory();
                                        new RaceRecipeListInventory().open(player, RaceType.DWARF);
                                    }
                                    case 15 -> {
                                        event.getWhoClicked().closeInventory();
                                        new RaceRecipeListInventory().open(player, RaceType.HUMAN);
                                    }
                                    case 17 -> {
                                        event.getWhoClicked().closeInventory();
                                        new RaceRecipeListInventory().open(player, RaceType.VOIDWALKER);
                                    }
                                    default -> {}
                                }
                            }
                        }

                        if (clickedSlot == 18) {
                            event.getWhoClicked().closeInventory();
                            new RaceGUIInventory().open(player);
                        }
                    }
                }
                event.setCancelled(true);
            } else if (getInventoryData().recipeListMap.containsKey(event.getView().getTopInventory())) {
                event.setCancelled(true);
                if (getInventoryData().recipeListMap.containsKey(event.getClickedInventory())) {
                    if (event.getCurrentItem() != null) {
                        int clickedSlot = event.getSlot();

                        if (AngelRecipes.isTearOfGod(event.getCurrentItem())) {
                            event.getWhoClicked().closeInventory();
                            new RecipeInventory().open(player, event.getCurrentItem(), RaceType.ANGEL);
                        } else if (DemonRecipes.isInfernalHeart(event.getCurrentItem())) {
                            event.getWhoClicked().closeInventory();
                            new RecipeInventory().open(player, event.getCurrentItem(), RaceType.DEMON);
                        } else if (DwarfRecipes.isMinersTrophy(event.getCurrentItem())) {
                            event.getWhoClicked().closeInventory();
                            new RecipeInventory().open(player, event.getCurrentItem(), RaceType.DWARF);
                        } else if (HumanRecipes.isElixirOfLife(event.getCurrentItem())) {
                            event.getWhoClicked().closeInventory();
                            new RecipeInventory().open(player, event.getCurrentItem(), RaceType.HUMAN);
                        } else if (VoidwalkerRecipes.isVoidForgedPendant(event.getCurrentItem())) {
                            event.getWhoClicked().closeInventory();
                            new RecipeInventory().open(player, event.getCurrentItem(), RaceType.VOIDWALKER);
                        }

                        if (clickedSlot == 18) {
                            event.getWhoClicked().closeInventory();
                            new RaceListInventory().open(player, RaceListInventory.RaceListType.RECIPES);
                        }
                    }
                }
                event.setCancelled(true);
            } else if (getInventoryData().recipeMap.containsKey(event.getView().getTopInventory())) {
                event.setCancelled(true);
                if (getInventoryData().recipeMap.containsKey(event.getClickedInventory())) {
                    if (event.getCurrentItem() != null) {
                        int clickedSlot = event.getSlot();

                        if (clickedSlot == 36) {
                            RaceType raceType = getInventoryData().recipeMap.get(event.getClickedInventory());
                            event.getWhoClicked().closeInventory();
                            new RaceRecipeListInventory().open(player, raceType);
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