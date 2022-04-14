package net.nighthawkempires.races.listeners.races;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.binding.BindingManager;
import net.nighthawkempires.races.data.PlayerData;
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
import org.bukkit.block.Block;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class VoidwalkerListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
            int x = player.getLocation().getBlockX();
            int z = player.getLocation().getBlockZ();

            if ((x >= 1000 || x <= -1000) && (z >= 1000 || z <= -1000)) {
                if (userModel.getRace().getRaceType() == RaceType.HUMAN) {
                    ItemStack itemStack = player.getInventory().getItemInMainHand();

                    if (itemStack.getType() == Material.ENDER_EYE && itemStack.hasItemMeta()) {
                        ItemMeta itemMeta = itemStack.getItemMeta();

                        if (itemMeta.getPersistentDataContainer().has(RacesPlugin.VOID_FORGED_PENDANT, PersistentDataType.STRING)) {
                            Race race = RacesPlugin.getRaceManager().getRace(RaceType.VOIDWALKER, 1);

                            userModel.setRace(race);
                            player.getInventory().setItemInMainHand(null);
                            player.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 0.5f);
                            player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 0.5f);

                            Bukkit.getPluginManager().callEvent(new RaceChangeEvent(player, race));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        RacesPlugin.getAbilityManager().getAbility(43).run(event);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        RacesPlugin.getAbilityManager().getAbility(44).run(event);
        RacesPlugin.getAbilityManager().getAbility(45).run(event);
        RacesPlugin.getAbilityManager().getAbility(48).run(event);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        RacesPlugin.getAbilityManager().getAbility(46).run(event);
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent event) {
        RacesPlugin.getAbilityManager().getAbility(42).run(event);
        RacesPlugin.getAbilityManager().getAbility(44).run(event);
        RacesPlugin.getAbilityManager().getAbility(46).run(event);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        RacesPlugin.getAbilityManager().getAbility(44).run(event);
        RacesPlugin.getAbilityManager().getAbility(49).run(event);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        RacesPlugin.getAbilityManager().getAbility(48).run(event);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        RacesPlugin.getAbilityManager().getAbility(43).run(event);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        RacesPlugin.getAbilityManager().getAbility(44).run(event);

        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.getRace().getRaceType() == RaceType.VOIDWALKER) {

            Block block = player.getLocation().getBlock();
            PlayerData.VoidwalkerData data = RacesPlugin.getPlayerData().voidwalker;
            if (block.isLiquid() && (block.getType() == Material.WATER || block.getType() == Material.WATER_CAULDRON)) {

                if (!data.waterMap.containsKey(player.getUniqueId())) {
                    data.waterMap.put(player.getUniqueId(),
                            Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
                                player.damage(1.0);
                    }, 20L, 20L));
                }
            } else if (!block.isLiquid()) {
                if (data.waterMap.containsKey(player.getUniqueId())) {
                    Bukkit.getScheduler().cancelTask(data.waterMap.get(player.getUniqueId()));
                    data.waterMap.remove(player.getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            RacesPlugin.getPlayerData().voidwalker.rainMap.put(event.getWorld(),
                    Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
                        for (Player player : event.getWorld().getPlayers()) {
                            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                            if (userModel.getRace().getRaceType() == RaceType.VOIDWALKER) {
                                double temperature = player.getLocation().getBlock().getTemperature();
                                int highestBlock = event.getWorld().getHighestBlockYAt(player.getLocation());

                                if (player.getLocation().getBlockY() >= highestBlock) {
                                    if (temperature >= 0.15 && temperature <= 0.95) {
                                        player.damage(1.0);
                                    }
                                }
                            }
                        }
            }, 20L, 20L));
        } else {
            if (RacesPlugin.getPlayerData().voidwalker.rainMap.containsKey(event.getWorld())) {
                Bukkit.getScheduler().cancelTask(RacesPlugin.getPlayerData().voidwalker.rainMap.get(event.getWorld()));
                RacesPlugin.getPlayerData().voidwalker.rainMap.remove(event.getWorld());
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.getRace().getRaceType() == RaceType.VOIDWALKER) {
            RacesPlugin.getAbilityManager().getAbility(41).run(event);
            RacesPlugin.getAbilityManager().getAbility(46).run(event);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.getRace().getRaceType() == RaceType.VOIDWALKER) {
            RacesPlugin.getAbilityManager().getAbility(41).run(event);
            RacesPlugin.getAbilityManager().getAbility(46).run(event);
        }
    }

    @EventHandler
    public void onUnlockAbility(AbilityUnlockEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (event.getAbility().getRaceType() == RaceType.VOIDWALKER) {
            if (event.getAbility().getAbilityType() == Ability.AbilityType.PASSIVE) {
                event.getAbility().run(event);
            }
        }
    }
}