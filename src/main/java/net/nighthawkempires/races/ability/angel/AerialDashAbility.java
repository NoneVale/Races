package net.nighthawkempires.races.ability.angel;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import static org.bukkit.ChatColor.RED;

public class AerialDashAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return switch (level) {
            case 5 -> 90;
            default -> 120;
        };
    }

    public int getMaxLevel() {
        return 4;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.QUARTZ;
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.ANGEL, 3);
    }

    public String getName() {
        return "Aerial Dash";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Knockback nearby entities."};
            case 3 -> new String[] {"Launches you 50% faster"};
            case 4 -> new String[] {"Reduce cooldown to " + getCooldown(level) + "s."};
            default -> new String[] {"Angels can launch themselves in the", "air using their wings."};
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

                double speed = 1.5 + (double)(level / 3);
                boolean knockback = level > 1;
                boolean explode = level > 3;

                Location explosion = player.getLocation();

                if (knockback) {
                    for (Entity entity : player.getWorld().getEntitiesByClass(LivingEntity.class)) {
                        if (entity instanceof LivingEntity || entity instanceof Item || entity instanceof Projectile ) {
                            if (entity.getLocation().distance(player.getLocation()) <= 7) {
                                double x = entity.getLocation().getX() - player.getLocation().getX();
                                double z = entity.getLocation().getZ() - player.getLocation().getZ();
                                Vector vector = new Vector(x, 0.3, z);
                                vector.normalize();
                                entity.setVelocity(entity.getVelocity().add(vector.multiply(-1.6)));
                            }
                        }
                    }
                }

                if (explode) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                        player.getWorld().createExplosion(explosion, 4F, false, false, player);
                    }, 10);
                }

                player.setVelocity(new Vector(player.getVelocity().getX(), speed, player.getVelocity().getZ()));

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have activated Aerial Dash."));

                addCooldown(this, player, level);
            }
        }
    }

    public int getId() {
        return 8;
    }

    public int getDuration(int level) {
        return 0;
    }
}
