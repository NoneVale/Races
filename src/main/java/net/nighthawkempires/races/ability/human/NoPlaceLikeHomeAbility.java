package net.nighthawkempires.races.ability.human;

import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.event.AbilityUnlockEvent;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NoPlaceLikeHomeAbility implements Ability {

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
            case 1, 2, 3 -> 1;
            case 4 -> 2;
            default -> 0;
        };
    }

    public Material getDisplayItem() {
        return Material.GRASS;
    }

    public RaceType getRaceType() {
        return RaceType.HUMAN;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.HUMAN, 1);
    }

    public String getName() {
        return "No Place Like Home";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Gain Strength I in the Overworld."};
            case 3 -> new String[] {"Gain Regeneration I in the Overworld."};
            case 4 -> new String[] {"Increase Speed effect."};
            default -> new String[] {"While in the Overworld, humans receive", "passive buffs.", "", "Gain Speed I in the Overworld."};
        };
    }

    public void run(Player player) {
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
            if (!Bukkit.getOnlinePlayers().contains(player)) Bukkit.getScheduler().cancelTask(RacesPlugin.getPlayerData().getTaskId(player, this));

            if (userModel.hasAbility(this)) {
                int level = userModel.getLevel(this);

                long time = player.getWorld().getTime();
                if (player.getWorld().getEnvironment() == World.Environment.NORMAL) {
                    switch (level) {
                        case 2:
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
                            break;
                        case 3:
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
                            break;
                        case 4:
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
                            break;
                        default:
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
                            break;
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
        return 32;
    }

    public int getDuration(int level) {
        return 0;
    }
}