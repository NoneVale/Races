package net.nighthawkempires.races.ability.angel;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.guilds.util.AllyUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

import static org.bukkit.ChatColor.RED;

public class ConcentratedGroundAbility implements Ability {

    private int particleTask = -1;
    private int damageTask = -1;

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return switch (level) {
            case 4,5 -> 90 + getDuration(level);
            default -> 120 + getDuration(level);
        };
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCost(int level) {
        return switch (level) {
            case 4,5 -> 2;
            default -> 1;
        };
    }

    public Material getDisplayItem() {
        return Material.RAW_GOLD_BLOCK;
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.ANGEL, 1);
    }

    public String getName() {
        return "Concentrated Ground";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Increase duration to 15s."};
            case 3 -> new String[] {"Increase radius to 8 blocks."};
            case 4 -> new String[] {"Reduce cooldown to " + getCooldown(level) + "s."};
            case 5 -> new String[] {"Doubles the burn damage, and increases", "duration to 20s."};
            default -> new String[] {"Angels can cleanse the land around", "them burning away impurities of others.", "", "Radius: 5 Blocks", "Duration: 10s"};
        };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        PlayerData.AngelData angelData = RacesPlugin.getPlayerData().angel;
        if (e instanceof PlayerInteractEvent event) {
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                if (checkCooldown(this, player)) return;

                if (!canUseRaceAbility(player)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(RED + "You can not use Race Abilities here."));
                    return;
                } else if (isSyphoned(player)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(RED + "Your powers are being syphoned by a demon."));
                    return;
                }

                int level = userModel.getLevel(this);

                int radius = switch (level) {
                    case 3,4,5 -> 8;
                    default -> 5;
                };

                double damage = switch (level) {
                    case 5 -> 1;
                    default -> .50;
                };

                Location location = player.getLocation();
                List<Block> blocks = Lists.newArrayList();
                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            location.add(x, y, z);
                            Block block = location.getBlock();
                            if (block.getType().isAir() && block.getRelative(BlockFace.DOWN).getType().isSolid()) {
                                blocks.add(block);
                            }
                            location.subtract(x, y, z);
                        }
                    }
                }

                if (!blocks.isEmpty()) {
                    damageTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
                        for (Player target : location.getWorld().getPlayers()) {
                            if (AllyUtil.isAlly(player, target)) continue;

                            Location playerLocation = target.getLocation();
                            if (blocks.contains(playerLocation.getBlock())) {
                                target.damage(damage, player);
                            }
                        }
                    }, 0, 15);

                    particleTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
                        for (Block block : blocks) {
                            location.getWorld().spawnParticle(Particle.WAX_ON, block.getLocation(), 5, .75, .5, .75);
                            location.getWorld().spawnParticle(Particle.WAX_OFF, block.getLocation(), 5, .75, .5, .75);
                        }
                    }, 0, 30L);


                    Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                        blocks.clear();
                        Bukkit.getScheduler().cancelTask(particleTask);
                        Bukkit.getScheduler().cancelTask(damageTask);
                        particleTask = damageTask = -1;
                    }, getDuration(level) * 20L);
                }

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have activated Concentrated Ground."));

                addCooldown(this, player, level);
            }
        }
    }

    public int getId() {
        return 2;
    }

    public int getDuration(int level) {
        return switch (level) {
            case 2,3,4 -> 15;
            case 5 -> 20;
            default -> 10;
        };
    }
}