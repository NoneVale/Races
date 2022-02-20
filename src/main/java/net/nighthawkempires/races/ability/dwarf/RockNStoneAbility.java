package net.nighthawkempires.races.ability.dwarf;

import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.event.AbilityUnlockEvent;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RockNStoneAbility implements Ability {

    private int taskId;

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
        return 1;
    }

    public Material getDisplayItem() {
        return Material.STONE;
    }

    public RaceType getRaceType() {
        return RaceType.DWARF;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DWARF, 1);
    }

    public String getName() {
        return "Rock N Stone";
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

                long time = player.getWorld().getTime();
                if (player.getWorld().getEnvironment() == World.Environment.NORMAL) {
                    Location location = player.getLocation();
                    switch (level) {
                        case 2 -> {
                            if (location.getBlockY() < 63) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0));
                                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 0));
                            }
                        }
                        case 3 -> {
                            if (location.getBlockY() < 0) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 1));
                                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 0));
                            }
                        }
                        case 4 -> {
                            if (location.getBlockY() < 0) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 1));
                                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 0));
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0));
                            }
                        }
                        default -> {
                            if (location.getBlockY() < 63) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0));
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
        return 21;
    }

    public int getDuration(int level) {
        return 0;
    }
}
