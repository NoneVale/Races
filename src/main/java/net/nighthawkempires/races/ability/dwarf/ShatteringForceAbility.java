package net.nighthawkempires.races.ability.dwarf;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.core.util.RandomUtil;
import net.nighthawkempires.guilds.util.AllyUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import static org.bukkit.ChatColor.RED;

public class ShatteringForceAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return switch (level) {
            case 4 -> 150 + getDuration(level);
            default -> 180 + getDuration(level);
        };
    }

    public int getMaxLevel() {
        return 4;
    }

    public int getCost(int level) {
        return switch (level) {
            case 4 -> 3;
            default -> 2;
        };
    }

    public Material getDisplayItem() {
        return Material.NETHER_STAR;
    }

    public RaceType getRaceType() {
        return RaceType.DWARF;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DWARF, 3);
    }

    public String getName() {
        return "Shattering Force";
    }

    public String[] getDescription(int level) {
        return new String[0];
    }

    public void run(Player player) {

    }

    public void run(Event e) {
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

                int radius = 8;
                int strength = level > 3 ? 2 : 1;

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Shattering Force has been activated"));

                for (int i = 0; i < 20 * getDuration(level); i++) {
                    if (i % 4 == 0) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                            for (Entity entity : player.getWorld().getEntitiesByClasses(LivingEntity.class, Item.class)) {
                                if (entity.getLocation().distance(player.getLocation()) <= radius) {
                                    if (!(entity instanceof Player) || !AllyUtil.isAlly(player, (Player) entity)) {
                                        double range = strength * .6;
                                        double minimum = range / 2;
                                        double x = RandomUtil.randomDouble(range, 0.0D) - minimum;
                                        double z = RandomUtil.randomDouble(range, 0.0D) - minimum;
                                        entity.setVelocity(entity.getVelocity().add(new Vector(x, entity.getVelocity().getY(), z)));
                                    }
                                }
                            }

                            if (RandomUtil.chance(30) && level == 4) {
                                double x = RandomUtil.randomNumber(2 * radius) - radius;
                                double z = RandomUtil.randomNumber(2 * radius) - radius;

                                Location location = new Location(player.getWorld(), player.getLocation().getX() - x,
                                        player.getLocation().getY(), player.getLocation().getZ() - z);
                                Block block = findAir(location);

                                for (int j = 0; j < 5; j++) {
                                    int finalJ = j;
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                                        Block spike = block.getRelative(BlockFace.UP, finalJ);
                                        if (spike.getType().isAir()) {
                                            spike.setType(Material.DEEPSLATE);

                                            Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                                                if (spike.getType() == Material.DEEPSLATE) {
                                                    block.setType(Material.AIR);
                                                }
                                            }, 120 - (finalJ * 20));
                                        }
                                    }, j * 6);
                                }
                            }
                        }, i);
                    }
                }

                addCooldown(this, player, level);
            }
        }
    }

    private Block findAir(Location location) {
        if (!location.getBlock().getRelative(BlockFace.DOWN).getType().isAir()) {
            return location.getBlock();
        } else {
            for(int i = 2; i < 7; ++i) {
                if (!location.getBlock().getRelative(BlockFace.DOWN, i).getType().isAir()) {
                    return location.getBlock().getRelative(BlockFace.DOWN, i - 1);
                }
            }

            return location.getBlock().getRelative(BlockFace.DOWN, 3);
        }
    }

    public int getId() {
        return 27;
    }

    public int getDuration(int level) {
        return switch (level) {
            case 2, 3, 4 -> 15;
            default -> 10;
        };
    }
}