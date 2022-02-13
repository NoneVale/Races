package net.nighthawkempires.races.ability.angel;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
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
        return "Vortex";
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
                if (CorePlugin.getCooldowns().hasActiveCooldown(player.getUniqueId(),
                        this.getClass().getSimpleName().toLowerCase())) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "There is another "
                            + CorePlugin.getCooldowns().getActive(player.getUniqueId(), this.getClass().getSimpleName().toLowerCase()).timeLeft()
                            + " before you can use this ability again."));
                    return;
                }


                boolean launch = level > 3;
                boolean lightning = level > 4;
                double strength = level > 1 ? .2 : .1;

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

                            location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 30, 50, 50, 50, 1F);
                            location.getWorld().getEntitiesByClasses(new Class[] { LivingEntity.class, Item.class, Projectile.class }).stream().filter((entity) -> {
                                return entity.getLocation().distance(location) <= 15D;
                            }).forEach((entity) -> {
                                if ((!(entity instanceof Player) || AllyUtil.isAlly((Player) entity, player))) {
                                    double angle = Math.toRadians(14D);
                                    double radius = Math.abs(entity.getLocation().distance(location));
                                    double x = player.getLocation().getX() - location.getX();
                                    double z = player.getLocation().getZ() - location.getZ();
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
                                }
                            });
                        }, i);
                    }
                }

                CorePlugin.getCooldowns().addCooldown(new Cooldown(player.getUniqueId(),
                        this.getClass().getSimpleName().toLowerCase(),
                        (System.currentTimeMillis() + (getCooldown(level) * 1000L))));
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