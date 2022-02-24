package net.nighthawkempires.races.ability.angel;

import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AngelicFaithAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.PASSIVE;
    }

    public int getCooldown(int level) {
        return 0;
    }

    public int getMaxLevel() {
        return 4;
    }

    public int getCost(int level) {
        return switch (level) {
            case 4 -> 2;
            default -> 1;
        };
    }

    public Material getDisplayItem() {
        return Material.FEATHER;
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.ANGEL, 1);
    }

    public String getName() {
        return "Angelic Faith";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[]{"Gain Strength I in light areas."};
            case 3 -> new String[]{"Gain Regeneration I in light areas."};
            case 4 -> new String[]{"Gain Speed II in light areas."};
            default ->  new String[] {"Angels obtain passive boosts while in", "the Overworld during the day and", "in light", "", "Gain Speed I in light areas."};
        };
    }

    public void run(Player player) {
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
            if (!Bukkit.getOnlinePlayers().contains(player)) Bukkit.getScheduler().cancelTask(RacesPlugin.getPlayerData().getTaskId(player, this));

            if (userModel.hasAbility(this)) {
                int level = userModel.getLevel(this);

                long time = player.getWorld().getTime();
                if (player.getLocation().getBlock().getLightFromSky() >= 8) {
                    if (player.getWorld().getEnvironment() == World.Environment.NORMAL) {
                        if (time < 12300 || time > 23850) {
                            if (!RacesPlugin.getPlayerData().angel.rainList.contains(player.getWorld())) {
                                switch (level) {
                                    case 2 -> {
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
                                    }
                                    case 3 -> {
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
                                    }
                                    case 4 -> {
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
                                    }
                                    default -> {
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
                                    }
                                }
                            }
                        }
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
    }

    public int getId() {
        return 1;
    }

    public int getDuration(int level) {
        return 0;
    }
}
