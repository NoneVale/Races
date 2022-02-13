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
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

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

                double speed = 1.5 + (double)(level / 3);
                boolean knockback = level > 1;
                boolean explode = level > 3;

                if (knockback) {
                    for (Entity entity : player.getWorld().getEntitiesByClass(LivingEntity.class)) {
                        if (entity instanceof LivingEntity livingEntity) {
                            if (livingEntity.getLocation().distance(player.getLocation()) <= 7) {
                                double x = livingEntity.getLocation().getX() - player.getLocation().getX();
                                double z = livingEntity.getLocation().getZ() - player.getLocation().getZ();
                                Vector vector = new Vector(x, 0.3, z);
                                vector.normalize();
                                livingEntity.setVelocity(livingEntity.getVelocity().add(vector.multiply(-1)));
                            }
                        }
                    }
                }

                if (explode) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                        player.getWorld().createExplosion(player.getLocation(), 4F, false, false, player);
                    }, 15);
                }

                player.setVelocity(new Vector(player.getVelocity().getX(), speed, player.getVelocity().getZ()));

                CorePlugin.getCooldowns().addCooldown(new Cooldown(player.getUniqueId(),
                        this.getClass().getSimpleName().toLowerCase(),
                        (System.currentTimeMillis() + (getCooldown(level) * 1000L))));
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
