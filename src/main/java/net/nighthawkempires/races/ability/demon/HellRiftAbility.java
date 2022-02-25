package net.nighthawkempires.races.ability.demon;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.util.RandomUtil;
import net.nighthawkempires.guilds.util.AllyUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import static org.bukkit.ChatColor.RED;

public class HellRiftAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return 180 + getDuration(level);
    }

    public int getMaxLevel() {
        return 4;
    }

    public int getCost(int level) {
        return 2;
    }

    public Material getDisplayItem() {
        return Material.RESPAWN_ANCHOR;
    }

    public RaceType getRaceType() {
        return RaceType.DEMON;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DEMON, 3);
    }

    public String getName() {
        return "Hell Rift";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[]{"Pull from the rift is twice", "as strong."};
            case 3 -> new String[]{"Duration is increased to 16s."};
            case 4 -> new String[]{"The rift explodes when it closes."};
            default ->  new String[] {"Demons can summon a rift that", "draws enemies inwards.", "", "Duration: 8s"};
        };
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

                boolean explode = level > 3;
                double strength = level > 1 ? .2 : .05;

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have opened a Hell Rift."));

                Location location = player.getLocation();
                for (int i = 0; i < 20 * getDuration(level); i++) {
                    if (i % 2 == 0) {
                        final int k = i;
                        Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                            location.getWorld().spawnParticle(Particle.PORTAL, location, 10, 1, 1, 1, 1F, null, true);
                            location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 10, 1, 1, 1, 1F, null, true);
                            location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 10, 1, 1, 1, 1F, null, true);
                            location.getWorld().spawnParticle(Particle.FLAME, location, 10, 1, 1, 1, 1F, null, true);
                            location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 10, 1, 1, 1, 1F, null, true);
                            location.getWorld().spawnParticle(Particle.DRIP_LAVA, location, 10, 1, 1, 1, 1F, null, true);

                            if (k % 20 == 0) {
                                location.getWorld().playSound(location, Sound.BLOCK_PORTAL_TRIGGER, 1F, 1F);
                            }

                            location.getWorld().getEntitiesByClasses(new Class[] { LivingEntity.class, Item.class, Projectile.class }).stream().filter((entity) -> {
                                return entity.getLocation().distance(location) <= 8D;
                            }).forEach((entity) -> {
                                if ((!(entity instanceof Player) || !AllyUtil.isAlly((Player) entity, player))) {
                                    double x = player.getLocation().getX() - location.getX();
                                    double y = player.getLocation().getY() - location.getY();
                                    double z = player.getLocation().getZ() - location.getZ();

                                    Vector vector = new Vector(x, y, z);
                                    vector.normalize();

                                    double amplifier = strength + 1D / entity.getLocation().distance(location);
                                    entity.setVelocity(entity.getVelocity().add(vector).multiply(-amplifier));
                                }
                            });
                        }, i);
                    }
                }

                if (explode) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                        location.getWorld().createExplosion(location, 5, false, false);
                    }, getDuration(level) * 20L);
                }
                addCooldown(this, player, level);
            }
        }
    }

    public int getId() {
        return 17;
    }

    public int getDuration(int level) {
        return switch (level) {
            case 3, 4 -> 16;
            default -> 8;
        };
    }
}