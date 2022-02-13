package net.nighthawkempires.races.ability.angel;

import net.nighthawkempires.core.util.EntityUtil;
import net.nighthawkempires.core.util.RandomUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PurgeAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.ACTIVE;
    }

    public int getCooldown(int level) {
        return 0;
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCost(int level) {
        return level == 4 ? 2 : 1;
    }

    public Material getDisplayItem() {
        return Material.IRON_SWORD;
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.ANGEL, 3);
    }

    public String getName() {
        return "Purge";
    }

    public String[] getDescription(int level) {
        return new String[0];
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof EntityDamageByEntityEvent event) {
            if (event.getDamager() instanceof Player player) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this)) {
                    int level = userModel.getLevel(this);
                    if (((!(event.getEntity() instanceof Player) && EntityUtil.isUnholy(event.getEntity()))
                            || !RacesPlugin.getUserRegistry().getUser(event.getEntity().getUniqueId()).getRace().getRaceType().isHolyRace())) {
                        int damage = switch (level) {
                            case 3 -> 4;
                            case 5 -> 6;
                            default -> 2;
                        };

                        int chance = switch (level) {
                            case 1 -> 0;
                            case 4, 5 -> 20;
                            default -> 10;
                        };

                        double addedPercent = switch (level) {
                            case 1 -> 0;
                            case 4, 5 -> .15;
                            default -> .5;
                        };

                        double finalDamage = event.getDamage() + damage;
                        if (RandomUtil.chance(chance)) {
                            finalDamage += addedPercent * event.getDamage();
                        }

                        event.setDamage(finalDamage);
                    }
                }
            }
        }
    }

    public int getId() {
        return 9;
    }

    public int getDuration(int level) {
        return 0;
    }
}
