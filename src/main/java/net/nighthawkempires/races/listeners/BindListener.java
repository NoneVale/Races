package net.nighthawkempires.races.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.binding.BindingManager;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BindListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UserModel user = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            BindingManager bindingManager = RacesPlugin.getBindingManager();
            ItemStack itemStack = player.getInventory().getItemInMainHand();

            if (bindingManager.getBindings(itemStack).size() >= 1
                    && bindingManager.getBinder(itemStack).toString().equals(player.getUniqueId().toString())) {
                if (bindingManager.getCurrentAbility(itemStack).getRaceType() != user.getRace().getRaceType()) {
                    if (bindingManager.getCurrentAbility(itemStack).getId() != 0) {
                        ItemStack clearedBindings = bindingManager.clearBindings(itemStack);
                        player.getInventory().setItemInMainHand(clearedBindings);
                        player.saveData();
                        return;
                    }
                }

                ItemStack scrolledItemStack = bindingManager.scrollNextAbility(itemStack);
                player.getInventory().setItemInMainHand(scrolledItemStack);
                player.saveData();

                if (bindingManager.getCurrentAbility(itemStack).getRaceType() != user.getRace().getRaceType()) {
                    if (bindingManager.getCurrentAbility(itemStack).getId() != 0) {
                        ItemStack clearedBindings = bindingManager.clearBindings(itemStack);
                        player.getInventory().setItemInMainHand(clearedBindings);
                        player.saveData();
                        return;
                    }
                }

                if (bindingManager.getCurrentAbilityIndex(scrolledItemStack) == 0) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "No Active Ability"));
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY + "Active Ability "
                            + ChatColor.of(user.getRace().getRaceType().getRaceColor().name()) + "" + ChatColor.BOLD + "" + ChatColor.ITALIC
                            + bindingManager.getCurrentAbility(itemStack).getName()));
                }
            }
        }
    }
}