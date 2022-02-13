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
        return new String[0];
    }

    public void run(Player player) {
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
            if (!Bukkit.getOnlinePlayers().contains(player)) Bukkit.getScheduler().cancelTask(taskId);

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
                Bukkit.getScheduler().cancelTask(taskId);
            }
        }, 20, 20);
    }

    public int taskId() {
        return this.taskId;
    }

    public void clearTaskId() {
        this.taskId = -1;
    }

    public void run(Event e) {
        passive(e);
    }

    public int getId() {
        return 1;
    }

    public int getDuration(int level) {
        return 0;
    }
}
