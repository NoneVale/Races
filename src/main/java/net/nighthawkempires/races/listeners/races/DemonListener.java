package net.nighthawkempires.races.listeners.races;

import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.binding.BindingManager;
import net.nighthawkempires.races.event.AbilityUnlockEvent;
import net.nighthawkempires.races.event.RaceChangeEvent;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class DemonListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
            if (player.getLocation().getBlock().getBiome() == Biome.BASALT_DELTAS) {
                if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    if (userModel.getRace().getRaceType() == RaceType.HUMAN) {
                        ItemStack itemStack = player.getInventory().getItemInMainHand();

                        if (itemStack.getType() == Material.NETHER_STAR && itemStack.hasItemMeta()) {
                            ItemMeta itemMeta = itemStack.getItemMeta();

                            if (itemMeta.getPersistentDataContainer().has(RacesPlugin.INFERNAL_HEART, PersistentDataType.STRING)) {
                                Race race = RacesPlugin.getRaceManager().getRace(RaceType.DEMON, 1);

                                userModel.setRace(race);
                                player.getInventory().setItemInMainHand(null);

                                player.playSound(player, Sound.ENTITY_GHAST_SCREAM, 1f, 0.5f);
                                player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1f, 0.5f);

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
        RacesPlugin.getAbilityManager().getAbility(12).run(event);
        RacesPlugin.getAbilityManager().getAbility(14).run(event);
        RacesPlugin.getAbilityManager().getAbility(16).run(event);
        RacesPlugin.getAbilityManager().getAbility(18).run(event);
        RacesPlugin.getAbilityManager().getAbility(19).run(event);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        RacesPlugin.getAbilityManager().getAbility(13).run(event);
        RacesPlugin.getAbilityManager().getAbility(19).run(event);
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent event) {
        RacesPlugin.getAbilityManager().getAbility(14).run(event);
        RacesPlugin.getAbilityManager().getAbility(15).run(event);
        RacesPlugin.getAbilityManager().getAbility(16).run(event);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        RacesPlugin.getAbilityManager().getAbility(15).run(event);

    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        RacesPlugin.getAbilityManager().getAbility(11).run(event);
        RacesPlugin.getAbilityManager().getAbility(15).run(event);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        RacesPlugin.getAbilityManager().getAbility(14).run(event);
        RacesPlugin.getAbilityManager().getAbility(16).run(event);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setVisualFire(false);
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        RacesPlugin.getAbilityManager().getAbility(11).run(event);
        RacesPlugin.getAbilityManager().getAbility(15).run(event);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        RacesPlugin.getAbilityManager().getAbility(11).run(event);
        RacesPlugin.getAbilityManager().getAbility(15).run(event);
    }

    @EventHandler
    public void onUnlockAbility(AbilityUnlockEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (event.getAbility().getRaceType() == RaceType.DEMON) {
            if (event.getAbility().getAbilityType() == Ability.AbilityType.PASSIVE) {
                event.getAbility().run(event);
            }
        }
    }
}
