package net.nighthawkempires.races.listeners.races;

import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.event.RaceChangeEvent;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class DwarfListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (player.getWorld().getEnvironment() == World.Environment.NORMAL) {
            if (player.getLocation().getBlockY() < 0) {
                if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    if (userModel.getRace().getRaceType() == RaceType.HUMAN) {
                        ItemStack itemStack = player.getInventory().getItemInMainHand();

                        if (itemStack.getType() == Material.AMETHYST_SHARD && itemStack.hasItemMeta()) {
                            ItemMeta itemMeta = itemStack.getItemMeta();

                            if (itemMeta.getPersistentDataContainer().has(RacesPlugin.MINERS_TROPHY, PersistentDataType.STRING)) {
                                Race race = RacesPlugin.getRaceManager().getRace(RaceType.DWARF, 1);

                                userModel.setRace(race);
                                player.getInventory().setItemInMainHand(null);

                                player.playSound(player, Sound.BLOCK_ANVIL_USE, 1f, 0.5f);
                                player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1f, 0.5f);

                                Bukkit.getPluginManager().callEvent(new RaceChangeEvent(player, race));
                            }
                        }
                    }
                }
            }
        }
    }
}