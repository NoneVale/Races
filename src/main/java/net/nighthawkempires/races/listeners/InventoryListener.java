package net.nighthawkempires.races.listeners;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.inventory.PerksInventory;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.ChatColor;
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
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (getInventoryData().perksInventoryList.contains(event.getView().getTopInventory())) {
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
                        } /*else if (clickedSlot == 45) {
                            event.getWhoClicked().closeInventory();
                            event.getWhoClicked().openInventory(getCategoryRegistry().getCategoryInventory());
                        } else if (clickedSlot == 46) {
                            event.getWhoClicked().closeInventory();
                            event.getWhoClicked().openInventory(model.getPage(page - 1));
                        } else if (clickedSlot == 52) {
                            event.getWhoClicked().closeInventory();
                            event.getWhoClicked().openInventory(model.getPage(page + 1));
                        }*/ else if (clickedSlot == 53) {
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
                event.setCancelled(true);
            }/** else if (getInventoryData().categoryInventoryList.contains(event.getView().getTopInventory())) {
                if (getInventoryData().categoryInventoryList.contains(event.getClickedInventory())) {
                    if (event.getCurrentItem() != null) {
                        int clickedSlot = event.getSlot();

                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().openInventory(getCategoryRegistry().getCategories().get(clickedSlot).getPage(1));
                    }
                }
                event.setCancelled(true);
            } else if (getInventoryData().checkoutDataMap.containsKey(event.getView().getTopInventory())) {
                if (getInventoryData().checkoutDataMap.containsKey(event.getClickedInventory())) {
                    CategoryItem categoryItem = getInventoryData().checkoutDataMap.get(event.getClickedInventory());
                    if (event.getCurrentItem() != null) {
                        int clickedSlot = event.getSlot();
                        UserModel userModel = getUserRegistry().getUser(player.getUniqueId());
                        ServerType serverType = getConfigg().getServerType();

                        if ((clickedSlot >= 11 && clickedSlot <= 15)
                                || (clickedSlot >= 29 && clickedSlot <= 33)) {
                            int amount = 1;
                            if (clickedSlot >= 11 && clickedSlot <= 15) {
                                // BUY
                                amount = event.getClickedInventory().getItem(clickedSlot + 9).getAmount();

                                double price = amount * categoryItem.getBuyPrice();

                                if (userModel.getServerBalance(serverType) < price) {
                                    player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but you do not have enough money in your account to purchase this."));
                                    event.setCancelled(true);
                                    return;
                                }

                                userModel.removeServerBalance(serverType, price);
                                player.sendMessage(getMessages().getChatMessage(GRAY + "You bought " + GOLD + amount + AQUA + " "
                                        + categoryItem.enumName(categoryItem.getItemType().name()) + GRAY + " for " + GREEN + "$"
                                        + YELLOW + price + GRAY + "."));
                                player.getInventory().addItem(categoryItem.toItemStack(amount));
                                player.closeInventory();
                                player.openInventory(categoryItem.openCheckoutPage(player));
                            } else {
                                amount = event.getClickedInventory().getItem(clickedSlot - 9).getAmount();

                                double price = amount * categoryItem.getSellPrice();

                                if (!player.getInventory().contains(categoryItem.toItemStack().getType(), amount)) {
                                    player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but you do not have " + GOLD + amount + " " + AQUA
                                            + categoryItem.enumName(categoryItem.getItemType().name()) + GRAY + " in your inventory."));
                                    event.setCancelled(true);
                                    return;
                                }

                                removeAmount(player, categoryItem.toItemStack().getType(), amount);
                                userModel.addServerBalance(serverType, price);
                                player.sendMessage(getMessages().getChatMessage(GRAY + "You sold " + GOLD + amount + AQUA + " "
                                        + categoryItem.enumName(categoryItem.getItemType().name()) + GRAY + " for " + GREEN + "$"
                                        + YELLOW + price + GRAY + "."));
                                player.closeInventory();
                                player.openInventory(categoryItem.openCheckoutPage(player));                            }

                        } else if (clickedSlot == 40) {
                            int amount = categoryItem.getAmountOfItems(player, new ItemStack(categoryItem.getItemType()));

                            double price = amount * categoryItem.getSellPrice();

                            if (!player.getInventory().contains(categoryItem.toItemStack().getType(), amount)) {
                                player.sendMessage(getMessages().getChatMessage(GRAY + "I'm sorry, but you do not have " + GOLD + amount + " " + AQUA
                                        + categoryItem.enumName(categoryItem.getItemType().name()) + GRAY + " in your inventory."));
                                event.setCancelled(true);
                                return;
                            }

                            removeAmount(player, categoryItem.toItemStack().getType(), amount);
                            userModel.addServerBalance(serverType, price);
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You sold " + GOLD + amount + AQUA + " "
                                    + categoryItem.enumName(categoryItem.getItemType().name()) + GRAY + " for " + GREEN + "$"
                                    + YELLOW + price + GRAY + "."));
                            player.closeInventory();
                            player.openInventory(categoryItem.openCheckoutPage(player));
                        } else if (clickedSlot == 45) {
                            event.getWhoClicked().closeInventory();
                            event.getWhoClicked().openInventory(getCategoryRegistry().getCategoryInventory());
                        } else if (clickedSlot == 53) {
                            event.getWhoClicked().closeInventory();
                        }
                    }
                }
                event.setCancelled(true);
            }**/
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (getInventoryData().perksInventoryList.contains(event.getInventory())) {
            getInventoryData().perksInventoryList.remove(event.getInventory());
        }
    }
}