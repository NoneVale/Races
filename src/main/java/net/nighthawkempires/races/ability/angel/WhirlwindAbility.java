package net.nighthawkempires.races.ability.angel;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.core.util.RandomUtil;
import net.nighthawkempires.guilds.util.AllyUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
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
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.util.Vector;

import static org.bukkit.ChatColor.RED;

public class WhirlwindAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return 180 + getDuration(level);
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCost(int level) {
        return 2;
    }

    public Material getDisplayItem() {
        return Material.STRING;
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.ANGEL, 3);
    }

    public String getName() {
        return "Whirlwind";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"The tornado is twice as strong."};
            case 3 -> new String[] {"Increase duration to 20s."};
            case 4 -> new String[] {"Entities close to the center will", "be launched upwards."};
            case 5 -> new String[] {"The tornado now summon lightning strikes."};
            default -> new String[] {"Angels can control the wind around", " them to summon a tornado that",  "moves enemies.", "", "Duration: 10s"};
        };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        PlayerData.AngelData angel = RacesPlugin.getPlayerData().angel;
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

                boolean launch = level > 3;
                boolean lightning = level > 4;
                double strength = level > 1 ? .2 : .1;

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You summoned a whirlwind at your location."));

                Location location = player.getLocation();
                for (int i = 0; i < 20 * getDuration(level); i++) {
                    if (i % 2 == 0) {
                        int finalI = i;
                        Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                            if (finalI % 20 == 0) {
                                location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1F, 1F);
                                if (lightning && RandomUtil.chance(30)) {
                                    double x = location.getX() + (double)(RandomUtil.randomNumber(14) - 7);
                                    double z = location.getZ() + (double)(RandomUtil.randomNumber(14) - 7);
                                    double y = location.getY();
                                    Location strike = new Location(location.getWorld(), x, y, z);
                                    location.getWorld().strikeLightning(strike);
                                }
                            }

                            location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 30, 1, 10, 1, 1F, null, true);
                            location.getWorld().getEntitiesByClasses(new Class[] { LivingEntity.class, Item.class, Projectile.class }).stream().filter(
                                    (entity) -> entity.getLocation().distance(location) <= 15D).forEach((entity) -> {
                                if ((!(entity instanceof Player) || !AllyUtil.isAlly((Player) entity, player))) {
                                    double angle = Math.toRadians(14);
                                    double radius = Math.abs(entity.getLocation().distance(location));
                                    double x = entity.getLocation().getX() - location.getX();
                                    double z = entity.getLocation().getZ() - location.getZ();
                                    double dx = x * Math.cos(angle) - z * Math.sin(angle);
                                    double dz = x * Math.sin(angle) + z * Math.cos(angle);
                                    Location target = new Location(entity.getWorld(), dx + location.getX(), entity.getLocation().getY(), dz + location.getZ());
                                    double ix = entity.getLocation().getX() - target.getX();
                                    double iz = entity.getLocation().getZ() - target.getZ();
                                    Vector vector = new Vector(ix, -0.2D, iz);
                                    vector.normalize();

                                    if (launch && radius < 2D) {
                                        vector.setY(-2.3D);
                                    }

                                    double amplifier = strength + 2D / radius;
                                    entity.setVelocity(entity.getVelocity().add(vector).multiply(-amplifier));

                                    if (entity instanceof Player effected) {
                                        if (!angel.whirlwind.contains(effected.getUniqueId())) angel.whirlwind.add(effected.getUniqueId());

                                        Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () ->
                                                angel.whirlwind.remove(effected.getUniqueId()), 100);
                                    }
                                }
                            });
                        }, i);
                    }
                }

                addCooldown(this, player, level);
            }
        } else if (e instanceof PlayerKickEvent event) {
            Player player = event.getPlayer();

            if (event.getCause() == PlayerKickEvent.Cause.FLYING_PLAYER) {
                if (angel.whirlwind.contains(player.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public int getId() {
        return 7;
    }

    public int getDuration(int level) {
        return switch (level) {
            case 3, 4, 5 -> 20;
            default -> 10;
        };
    }
}