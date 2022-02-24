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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class AngelListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (player.getWorld().getEnvironment() == World.Environment.NORMAL) {
            if (player.getLocation().getBlockY() > 128) {
                if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
                    if (userModel.getRace().getRaceType() == RaceType.HUMAN) {
                        ItemStack itemStack = player.getInventory().getItemInMainHand();

                        if (itemStack.getType() == Material.GHAST_TEAR && itemStack.hasItemMeta()) {
                            ItemMeta itemMeta = itemStack.getItemMeta();

                            if (itemMeta.getPersistentDataContainer().has(RacesPlugin.TEAR_OF_GOD, PersistentDataType.STRING)) {
                                Race race = RacesPlugin.getRaceManager().getRace(RaceType.ANGEL, 1);

                                userModel.setRace(race);
                                player.getInventory().setItemInMainHand(null);

                                player.playSound(player, Sound.ENTITY_ENDER_DRAGON_FLAP, 1f, 0.5f);
                                player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1f, 0.5f);

                                Bukkit.getPluginManager().callEvent(new RaceChangeEvent(player, race));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.getRace().getRaceType() == RaceType.ANGEL) {
            BindingManager bindingManager = RacesPlugin.getBindingManager();
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (player.getEquipment().getItemInMainHand() != null
                        && player.getEquipment().getItemInMainHand().getItemMeta() != null) {
                    if (bindingManager.getBindings(player.getEquipment().getItemInMainHand()).size() > 0) {
                        bindingManager.getCurrentAbility(player.getEquipment().getItemInMainHand()).run(event);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        RacesPlugin.getAbilityManager().getAbility(6).run(event);
        RacesPlugin.getAbilityManager().getAbility(9).run(event);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        RacesPlugin.getAbilityManager().getAbility(4).run(event);
        RacesPlugin.getAbilityManager().getAbility(6).run(event);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        RacesPlugin.getAbilityManager().getAbility(4).run(event);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.getRace().getRaceType() == RaceType.ANGEL) {
            RacesPlugin.getAbilityManager().getAbility(1).run(event);
            RacesPlugin.getAbilityManager().getAbility(4).run(event);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.getRace().getRaceType() == RaceType.ANGEL) {
            RacesPlugin.getAbilityManager().getAbility(1).run(event);
            RacesPlugin.getAbilityManager().getAbility(4).run(event);
        }
    }

    @EventHandler
    public void onUnlockAbility(AbilityUnlockEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (event.getAbility().getRaceType() == RaceType.ANGEL) {
            if (event.getAbility().getAbilityType() == Ability.AbilityType.PASSIVE) {
                event.getAbility().run(event);
            }
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            RacesPlugin.getPlayerData().angel.rainList.add(event.getWorld());
        } else {
            RacesPlugin.getPlayerData().angel.rainList.remove(event.getWorld());
        }
    }
}