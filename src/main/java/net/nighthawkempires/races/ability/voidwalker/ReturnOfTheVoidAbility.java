package net.nighthawkempires.races.ability.voidwalker;

import com.mojang.authlib.yggdrasil.response.User;
import net.nighthawkempires.core.util.RandomUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class ReturnOfTheVoidAbility implements Ability {

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
        return Material.EXPERIENCE_BOTTLE;
    }

    public RaceType getRaceType() {
        return RaceType.VOIDWALKER;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(getRaceType(), 3);
    }

    public String getName() {
        return "Return of the Void";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Increase base chance to 15%, night", "chance increased to 30%. (Drops 1-2", "additional pearls.)"};
            case 3 -> new String[] {"Increase base chance to 30%, night", "chance increased to 50%. (Drops 2-3", "additional pearls.)"};
            default -> new String[] {"Voidwalkers are able to gain Ender", "Pearls killing undead mobs.", "", "5% base chance for undead mobs", "to drop an additional Ender Pearl.", "15% chance at night."};
        };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof EntityDeathEvent event) {
            Player player = event.getEntity().getKiller();
            if (player != null) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this)) {
                    int level = userModel.getLevel(this);

                    int baseChance = switch (level) {
                        case 2 -> 15;
                        case 3 -> 30;
                        default -> 5;
                    };

                    int boostedChance = switch (level) {
                        case 2 -> 30;
                        case 3 -> 50;
                        default -> 15;
                    };

                    int pearls = switch (level) {
                        case 3 -> 2;
                        default -> 1;
                    };

                    if (RandomUtil.fiftyfifty()) {
                        pearls++;
                    }

                    if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
                        if (RandomUtil.chance(baseChance)) {
                            player.getWorld().dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.ENDER_PEARL, pearls));
                        }
                    } else {
                        long time = player.getWorld().getTime();
                        if (!(time < 12300 || time > 23850)) {
                            if (RandomUtil.chance(boostedChance)) {
                                player.getWorld().dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.ENDER_PEARL, pearls));

                            }
                        } else {
                            if (RandomUtil.chance(baseChance)) {
                                player.getWorld().dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.ENDER_PEARL, pearls));
                            }
                        }
                    }
                }
            }
        }
    }

    public int getId() {
        return 49;
    }

    public int getDuration(int level) {
        return 0;
    }
}
