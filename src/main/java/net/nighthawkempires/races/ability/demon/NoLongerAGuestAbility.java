package net.nighthawkempires.races.ability.demon;

import com.google.common.collect.Lists;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class NoLongerAGuestAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.PASSIVE;
    }

    public int getCooldown(int level) {
        return 0;
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.NETHER_STAR;
    }

    public RaceType getRaceType() {
        return RaceType.DEMON;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DEMON, 2);
    }

    public String getName() {
        return "No Longer a Guest";
    }

    public String[] getDescription(int level) {
        return new String[0];
    }

    public void run(Player player) {
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
            if (!Bukkit.getOnlinePlayers().contains(player)) Bukkit.getScheduler().cancelTask(RacesPlugin.getPlayerData().getTaskId(player, this));

            if (userModel.hasAbility(this)) {
                int level = userModel.getLevel(this);

                if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
                    switch (level) {
                        case 2 -> {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
                        }
                        case 3 -> {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
                        }
                        case 4 -> {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
                        }
                        default -> {}
                    }
                }
            } else {
                Bukkit.getScheduler().cancelTask(RacesPlugin.getPlayerData().getTaskId(player, this));
            }
        }, 20, 20);

        RacesPlugin.getPlayerData().setTaskId(player, this, taskId);
    }

    public void run(Event e) {
        passive(e, this);
        if (e instanceof EntityDamageEvent event) {
            if (event.getEntity() instanceof Player player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this)) {
                    event.setCancelled(true);
                }
            }
        } else if (e instanceof EntityTargetLivingEntityEvent event) {
            List<EntityType> hellMobs = Lists.newArrayList(EntityType.MAGMA_CUBE, EntityType.BLAZE, EntityType.WITHER_SKELETON,
                    EntityType.GHAST, EntityType.HOGLIN, EntityType.ZOGLIN, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.ZOMBIFIED_PIGLIN);
            if (event.getTarget() instanceof Player player) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this) && userModel.getLevel(this) > 1) {
                    if (!RacesPlugin.getMobData().isPet(event.getEntity())) {
                        if (hellMobs.contains(event.getEntityType())) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }  else if (e instanceof PlayerMoveEvent event) {
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this) && userModel.getLevel(this) > 3) {
                Block block = player.getLocation().getBlock();
                boolean apply = true;
                for (int y = 0; y < 6; y++) {
                    if (!block.getRelative(BlockFace.DOWN, y).getType().isAir()) {
                        apply = false;
                        break;
                    }
                }

                if (apply) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 10, 0));
                }
            }
        }
    }

    public int getId() {
        return 15;
    }

    public int getDuration(int level) {
        return 0;
    }
}
