package net.nighthawkempires.races.listeners.races;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.binding.BindingManager;
import net.nighthawkempires.races.event.AbilitiesResetEvent;
import net.nighthawkempires.races.event.AbilityUnlockEvent;
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
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        RacesPlugin.getAbilityManager().getAbility(22).run(event);
    }

    @EventHandler
    public void onChange(PlayerArmorChangeEvent event) {
        RacesPlugin.getAbilityManager().getAbility(25).run(event);
    }

    @EventHandler
    public void onDamage(PlayerItemDamageEvent event) {
        RacesPlugin.getAbilityManager().getAbility(25).run(event);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        RacesPlugin.getAbilityManager().getAbility(28).run(event);
        RacesPlugin.getAbilityManager().getAbility(29).run(event);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        RacesPlugin.getAbilityManager().getAbility(24).run(event);
    }

    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent event) {
        RacesPlugin.getAbilityManager().getAbility(26).run(event);
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        RacesPlugin.getAbilityManager().getAbility(26).run(event);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.getRace().getRaceType() == RaceType.DWARF) {
            RacesPlugin.getAbilityManager().getAbility(21).run(event);
            RacesPlugin.getAbilityManager().getAbility(22).run(event);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.getRace().getRaceType() == RaceType.DWARF) {
            RacesPlugin.getAbilityManager().getAbility(21).run(event);
            RacesPlugin.getAbilityManager().getAbility(22).run(event);
        }
    }

    @EventHandler
    public void onUnlockAbility(AbilityUnlockEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (event.getAbility().getRaceType() == RaceType.DWARF) {
            if (event.getAbility().getAbilityType() == Ability.AbilityType.PASSIVE) {
                event.getAbility().run(event);
            }
        }

        RacesPlugin.getAbilityManager().getAbility(25).run(event);
    }

    @EventHandler
    public void onReset(AbilitiesResetEvent event) {
        RacesPlugin.getAbilityManager().getAbility(25).run(event);
    }
}