package net.nighthawkempires.races.ability.human;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.core.util.ItemUtil;
import net.nighthawkempires.core.util.RandomUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

import static org.bukkit.ChatColor.RED;

public class PiercingEdgeAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.ACTIVE;
    }

    public int getCooldown(int level) {
        return switch (level) {
            case 3, 4 -> 25 + getDuration(level);
            case 5 -> 15 + getDuration(level);
            default -> 30 + getDuration(level);
        };
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.IRON_SWORD;
    }

    public RaceType getRaceType() {
        return RaceType.HUMAN;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.HUMAN, 1);
    }

    public String getName() {
        return "Piercing Edge";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Increase bleed chance to 15%, and", "duration to 3s."};
            case 3 -> new String[] {"Increase duration to 4s, and reduce", "cooldown to " + getCooldown(level) + "s."};
            case 4 -> new String[] {"Increase bleed chance to 20%, and", "duration to 5s."};
            case 5 -> new String[] {"Reduce cooldown to " + getCooldown(level) + "s."};
            default -> new String[] {"Attacking enemies with a sword or", "axe deals a chance to make", "them bleed.", "", "10% bleed chance", "Duration: 2s"};
        };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof EntityDamageByEntityEvent event) {
            if (event.getDamager() instanceof Player player) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (event.getEntity() instanceof LivingEntity target) {
                    if (userModel.hasAbility(this)) {
                        if (checkCooldown(this, player, false)) return;

                        if (!canUseRaceAbility(player)) {
                            return;
                        } else if (isSyphoned(player)) {
                            return;
                        }

                        int level = userModel.getLevel(this);

                        List<Material> sharpTools = Lists.newArrayList(Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD,
                                Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD, Material.WOODEN_AXE, Material.STONE_AXE,
                                Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE);

                        if (!sharpTools.contains(player.getInventory().getItemInMainHand().getType())) return;

                        int chance = switch (level) {
                            case 2, 3 -> 15;
                            case 4, 5 -> 20;
                            default -> 10;
                        };

                        if (RandomUtil.chance(chance)) {
                            target.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You're now bleeding."));
                            if (target instanceof Player) {
                                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have cut " + ChatColor.GREEN
                                        + target.getName() + ChatColor.GRAY + ", they're now bleeding."));
                            }

                            int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
                                target.damage(.25, player);
                            }, 0, 10);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                                Bukkit.getScheduler().cancelTask(taskId);
                            },  getDuration(level) * 20L);

                            addCooldown(this, player, level);
                        }
                    }
                }
            }
        }
    }

    public int getId() {
        return 33;
    }

    public int getDuration(int level) {
        switch (level) {
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
            case 5:
                return 5;
            default:
                return 2;
        }
    }
}
