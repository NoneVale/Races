package net.nighthawkempires.races.ability.voidwalker;

import com.google.common.collect.Lists;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class EnderRightsAbility implements Ability {

    private static final List<EntityType> endMobs = Lists.newArrayList(EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.SHULKER, EntityType.SHULKER_BULLET);

    public AbilityType getAbilityType() {
        return AbilityType.ACTIVE;
    }

    public int getCooldown(int level) {
        return 0;
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.ENDER_EYE;
    }

    public RaceType getRaceType() {
        return RaceType.VOIDWALKER;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(getRaceType(), 2);
    }

    public String getName() {
        return "Ender Rights";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"End mobs no longer attack you."};
            case 3 -> new String[] {"Gain Nightvision."};
            default -> new String[] {"After spending their share of time", "in the End, Voidwalkers are given", "buffs that assist them.", "", "No longer take fall damage in", "the End."};
        };
    }

    public void run(Player player) {
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
            if (!Bukkit.getOnlinePlayers().contains(player)) Bukkit.getScheduler().cancelTask(RacesPlugin.getPlayerData().getTaskId(player, this));

            if (userModel.hasAbility(this)) {
                int level = userModel.getLevel(this);

                if (level > 2) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 300, 0));
                }
            } else {
                Bukkit.getScheduler().cancelTask(RacesPlugin.getPlayerData().getTaskId(player, this));
            }
        }, 20, 20);

        RacesPlugin.getPlayerData().setTaskId(player, this, taskId);
    }

    public void run(Event e) {
        passive(e, this);
        if (e instanceof EntityTargetLivingEntityEvent event) {
            if (event.getTarget() instanceof Player player) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this) && userModel.getLevel(this) > 1) {
                    if (!RacesPlugin.getMobData().isPet(event.getEntity())) {
                        if (endMobs.contains(event.getEntityType())) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        } else if (e instanceof EntityDamageEvent event) {
            if (event.getEntity() instanceof Player player) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this) && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    int level = userModel.getLevel(this);

                    if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
            }
        }
    }

    public int getId() {
        return 46;
    }

    public int getDuration(int level) {
        return 0;
    }
}
