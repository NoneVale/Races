package net.nighthawkempires.races.ability.dwarf;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.guilds.util.AllyUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

import static org.bukkit.ChatColor.RED;

public class StonewallAbility implements Ability {

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
        PlayerData.DwarfData dwarfData = RacesPlugin.getPlayerData().dwarf;
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
                List<Block> blocks = Lists.newArrayList();

                Location center = player.getLocation();
                for (int x = -5; x <= 5; x++) {
                    for (int y = -5; y <= 5; y++) {
                        for (int z = -5; z <= 5; z++) {
                            Location location = new Location(center.getWorld(), center.getX() + x, center.getY() + y, center.getZ() + z);
                            double distance = location.distance(center);
                            if (distance > 4 && distance <= 5 && location.getBlock().getType() == Material.AIR) {
                                blocks.add(location.getBlock());
                                location.getBlock().setType(Material.STONE);
                            }
                        }
                    }
                }

                if (level > 1) {
                    player.getWorld().getEntitiesByClasses(new Class[] {LivingEntity.class, Projectile.class, Item.class }).stream().filter(
                            (target) -> target.getLocation().distance(player.getLocation()) <= 5).forEach((target) -> {
                        if (target instanceof LivingEntity entity) {
                            double x = entity.getLocation().getX() - player.getLocation().getX();
                            double z = entity.getLocation().getZ() - player.getLocation().getZ();

                            Vector vector = new Vector(x, 0.14, z);

                            entity.setVelocity(vector.normalize().multiply(1.5));
                        }
                    });
                }

                dwarfData.stonewall.put(player.getUniqueId(), blocks);
                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Stonewall has been activated."));

                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    for (Block block : blocks) {
                        block.setType(Material.AIR);
                    }
                    dwarfData.stonewall.remove(player.getUniqueId());
                }, getDuration(level) * 20L);

                addCooldown(this, player, level);
            }
        } else if (e instanceof BlockBreakEvent event) {
            if (dwarfData.stonewall.isEmpty()) return;
            Player player = event.getPlayer();

            for (List<Block> blocks : dwarfData.stonewall.values()) {
                if (blocks.isEmpty()) return;

                if (blocks.contains(event.getBlock())) {
                    for (UUID uuid : dwarfData.stonewall.keySet()) {
                        if (dwarfData.stonewall.get(uuid) == blocks) {
                            Player caster = Bukkit.getPlayer(uuid);
                            UserModel userModel = RacesPlugin.getUserRegistry().getUser(caster.getUniqueId());
                            if (userModel.hasAbility(this) && userModel.getLevel(this) > 2) {
                                if (player.getUniqueId() != caster.getUniqueId()) {
                                    if (!AllyUtil.isAlly(caster, player)) {
                                        player.getWorld().createExplosion(event.getBlock().getLocation(), 3, false, false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int getId() {
        return 28;
    }

    public int getDuration(int level) {
        return switch (level) {
            case 2, 3 -> 10;
            default -> 6;
        };
    }
}