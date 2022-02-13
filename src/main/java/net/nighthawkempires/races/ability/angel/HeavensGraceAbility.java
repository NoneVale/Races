package net.nighthawkempires.races.ability.angel;

import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HeavensGraceAbility implements Ability {

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
        return 1;
    }

    public Material getDisplayItem() {
        return Material.PHANTOM_MEMBRANE;
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.ANGEL, 2);
    }

    public String getName() {
        return "Heaven's Grace";
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
                if (player.getLocation().getBlockY() >= 128) {
                    if (player.getWorld().getEnvironment() == World.Environment.NORMAL) {
                        switch (level) {
                            case 4 -> player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 1));
                            default -> player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 0));
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
        if (e instanceof EntityDamageEvent event) {
            if (event.getEntity() instanceof Player player) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this)) {
                    if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                        event.setCancelled(true);
                    }
                }
            }
        } else if (e instanceof PlayerMoveEvent event) {
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                int level = userModel.getLevel(this);

                if (level == 4) {
                    Block block = player.getLocation().getBlock();
                    boolean apply = true;
                    for (int y = 0; y < 9; y++) {
                        if (!block.getRelative(BlockFace.DOWN, y).getType().isAir()) {
                            apply = false;
                            break;
                        }
                    }

                    if (apply) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 10, 0));
                    }
                }
            }
        }
    }

    public int getId() {
        return 4;
    }

    public int getDuration(int level) {
        return 0;
    }
}
