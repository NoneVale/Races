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

    private int taskId = -1;

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
        switch (level) {
            case 1:
            case 2:
            case 3:
                return 1;
            case 4:
                return 2;
        }
        return 0;
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
        return new String[0];
    }

    public void run(Player player) {
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
            if (!Bukkit.getOnlinePlayers().contains(player)) Bukkit.getScheduler().cancelTask(taskId);

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
                Bukkit.getScheduler().cancelTask(taskId);
            }
        }, 20, 20);
    }

    public void run(Event e) {
        PlayerData.AngelData celestialData = RacesPlugin.getPlayerData().angel;
        if (e instanceof PlayerJoinEvent) {
            PlayerJoinEvent event = (PlayerJoinEvent) e;
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                run(player);
            }
        } else if (e instanceof PlayerQuitEvent) {
            PlayerQuitEvent event = (PlayerQuitEvent) e;
            Player player = event.getPlayer();

            if (taskId != 0) {
                Bukkit.getScheduler().cancelTask(taskId);
                taskId = 0;
            }
        } else if (e instanceof AbilityUnlockEvent) {
            AbilityUnlockEvent event = (AbilityUnlockEvent) e;
            Player player = event.getPlayer();

            if (event.getAbility() == this) {
                run(player);
            }
        }
    }

    public int getId() {
        return 32;
    }

    public int getDuration(int level) {
        return 0;
    }
}