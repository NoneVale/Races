package net.nighthawkempires.races.listeners.races;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.binding.BindingManager;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

public class VoidwalkerListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

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

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.getRace().getRaceType() == RaceType.VOIDWALKER) {
                RacesPlugin.getAbilityManager().getAbility(81).run(event);
                RacesPlugin.getAbilityManager().getAbility(85).run(event);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.getRace().getRaceType() == RaceType.VOIDWALKER) {
            RacesPlugin.getAbilityManager().getAbility(87).run(event);

            Block block = player.getLocation().getBlock();
            PlayerData.Voidwalker data = RacesPlugin.getPlayerData().voidwalker;
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
    public void onToggleGlide(EntityToggleGlideEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.getRace().getRaceType() == RaceType.VOIDWALKER) {
                RacesPlugin.getAbilityManager().getAbility(87).run(event);
            }
        }
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        Player player = (Player) event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.getRace().getRaceType() == RaceType.VOIDWALKER) {
            RacesPlugin.getAbilityManager().getAbility(87).run(event);
        }
    }

    @EventHandler
    public void onJump(PlayerJumpEvent event) {
        Player player = (Player) event.getPlayer();

        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.getRace().getRaceType() == RaceType.VOIDWALKER) {
            RacesPlugin.getAbilityManager().getAbility(87).run(event);
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
            RacesPlugin.getAbilityManager().getAbility(82).run(event);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.getRace().getRaceType() == RaceType.VOIDWALKER) {
            RacesPlugin.getAbilityManager().getAbility(82).run(event);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.getRace().getRaceType() == RaceType.VOIDWALKER) {
                RacesPlugin.getAbilityManager().getAbility(85).run(event);
            }
        } else if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.getRace().getRaceType() == RaceType.VOIDWALKER) {
                RacesPlugin.getAbilityManager().getAbility(85).run(event);
            }
        } else if (event.getEntity() instanceof Enderman) {
            RacesPlugin.getAbilityManager().getAbility(85).run(event);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Enderman) {
            RacesPlugin.getAbilityManager().getAbility(85).run(event);
        }
    }
}