package net.nighthawkempires.races.ability.voidwalker;

import com.google.common.collect.Lists;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

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
        return 2;
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
        return new String[0];
    }

    public void run(Player player) {

    }

    public void run(Event e) {
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
