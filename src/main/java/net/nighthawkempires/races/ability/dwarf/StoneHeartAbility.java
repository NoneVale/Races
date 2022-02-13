package net.nighthawkempires.races.ability.dwarf;


import net.nighthawkempires.core.util.PotionUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.util.Vector;

public class StoneHeartAbility implements Ability {

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
        return Material.SPLASH_POTION;
    }

    public RaceType getRaceType() {
        return RaceType.DWARF;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DWARF, 2);
    }

    public String getName() {
        return "Stone Heart";
    }

    public String[] getDescription(int level) {
        return new String[0];
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof EntityPotionEffectEvent event) {
            if (event.getEntity() instanceof Player player) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this)) {
                    int level = userModel.getLevel(this);

                    if (PotionUtil.isNegativeEffect(event.getModifiedType())) {
                        int chance = switch (level) {
                            case 2 -> 40;
                            case 3 -> 55;
                            case 4 -> 70;
                            case 5 -> 85;
                            default -> 25;
                        };

                        int random = Double.valueOf(Math.random() * 100).intValue();
                        if (random <= chance) {
                            player.removePotionEffect(event.getModifiedType());
                        }
                    }
                }
            }
        } if (e instanceof PotionSplashEvent event) {
            for (LivingEntity entity : event.getAffectedEntities()) {
                if (entity instanceof Player player) {
                    UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                    if (userModel.hasAbility(this)) {
                        int level = userModel.getLevel(this);

                        int chance = switch (level) {
                            case 3 -> 20;
                            case 4 -> 35;
                            case 5 -> 50;
                            default -> -1;
                        };

                        int random = Double.valueOf(Math.random() * 100).intValue();
                        // if this doesn't work cancel event, and launch projectile from player.
                        if (random <= chance) {
                            if (event.getEntity().getShooter() instanceof Entity target) {
                                Vector to = target.getLocation().toVector();
                                Vector from = player.getLocation().toVector();

                                Vector direction = to.subtract(from).normalize();

                                event.getPotion().setVelocity(direction.multiply(1.5));
                            }
                        }
                    }
                }
            }
        }
    }

    public int getId() {
        return 16;
    }

    public int getDuration(int level) {
        return 0;
    }
}
