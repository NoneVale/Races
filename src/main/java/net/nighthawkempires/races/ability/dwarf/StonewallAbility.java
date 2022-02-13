package net.nighthawkempires.races.ability.dwarf;

import com.google.common.collect.Lists;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class StonewallAbility implements Ability {

    private List<Block> blocks;

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return switch (level) {
            case 3 -> 90 + getDuration(level);
            default -> 120 + getDuration(level);
        };
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        return 2;
    }

    public Material getDisplayItem() {
        return Material.STONE;
    }

    public RaceType getRaceType() {
        return RaceType.DWARF;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DWARF, 3);
    }

    public String getName() {
        return "Stonewall";
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
                int level = userModel.getLevel(this);
                blocks = Lists.newArrayList();

                Location location = player.getLocation();

                for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
                    double radius = Math.sin(i);
                    double y = Math.cos(i);
                    for (double a = 0; a < Math.PI * 2; a+= Math.PI / 10) {
                        double x = Math.cos(a) * radius;
                        double z = Math.sin(a) * radius;
                        location.add(x, y, z);
                        if (location.getBlock().getType() == Material.AIR) {
                            blocks.add(location.getBlock());
                            location.getBlock().setType(Material.STONE);
                        }
                        location.subtract(x, y, z);
                    }
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    for (Block block : blocks) {
                        block.setType(Material.AIR);
                    }
                    blocks.clear();
                }, getDuration(level) * 20L);
            }
        } else if (e instanceof BlockBreakEvent event) {
            if (blocks.isEmpty()) return;

            Player player = event.getPlayer();

        }
    }

    public int getId() {
        return 18;
    }

    public int getDuration(int level) {
        return switch (level) {
            case 2, 3 -> 10;
            default -> 6;
        };
    }
}