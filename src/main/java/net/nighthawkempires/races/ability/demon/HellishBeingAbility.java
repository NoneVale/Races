package net.nighthawkempires.races.ability.demon;

import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
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

public class HellishBeingAbility implements Ability {

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
        return level > 4 ? 1 : 2;
    }

    public Material getDisplayItem() {
        return Material.BLAZE_POWDER;
    }

    public RaceType getRaceType() {
        return RaceType.DEMON;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DEMON, 1);
    }

    public String getName() {
        return "Hellish Being";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[]{"Gain Speed I in the Nether."};
            case 3 -> new String[]{"Gain Strength I in the Nether."};
            case 4 -> new String[]{"Gain Regeneration I in the Nether."};
            default ->  new String[] {"Demons obtain passive boosts while in", "the Nether.", "", "Gain Fire Resistance (Regardless of world)."};
        };
    }

    public void run(Player player) {
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
            if (!Bukkit.getOnlinePlayers().contains(player)) Bukkit.getScheduler().cancelTask(RacesPlugin.getPlayerData().getTaskId(player, this));

            if (userModel.hasAbility(this)) {
                int level = userModel.getLevel(this);

                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0));
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
    }

    public int getId() {
        return 11;
    }

    public int getDuration(int level) {
        return 0;
    }
}
