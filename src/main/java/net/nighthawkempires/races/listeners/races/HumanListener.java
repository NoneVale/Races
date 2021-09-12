package net.nighthawkempires.races.listeners.races;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.event.RaceChangeEvent;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.recipes.HumanRecipes;
import net.nighthawkempires.races.recipes.VampireRecipes;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class HumanListener implements Listener {

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (!event.getItem().isSimilar(HumanRecipes.itemElixirOfLife())) return;

        if (userModel.getRace().getRaceType() == RaceType.HUMAN) {
            player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "You are already human, so there's no purpose in drinking the Elixir of Life."));
            event.setCancelled(true);
        } else {
            Race race = RacesPlugin.getRaceManager().getRace(RaceType.HUMAN, 1);
            Bukkit.getPluginManager().callEvent(new RaceChangeEvent(player, race));
        }
    }

    @EventHandler
    public void onPrepare(PrepareItemCraftEvent event) {
        if (event.getRecipe() != null && event.getRecipe().getResult().isSimilar(HumanRecipes.itemElixirOfLife())) {
            event.getInventory().setResult(null);
            if (event.getInventory().getMatrix()[4].getType() == Material.POTION) {
                ItemStack itemStack = event.getInventory().getMatrix()[4];

                if (itemStack.getItemMeta() instanceof PotionMeta) {
                    PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
                    PotionData potionData = potionMeta.getBasePotionData();

                    if (potionData.getType() == PotionType.REGEN && potionData.isUpgraded()) {
                        event.getInventory().setResult(HumanRecipes.itemElixirOfLife());
                    }
                }
            }
        }
    }
}
