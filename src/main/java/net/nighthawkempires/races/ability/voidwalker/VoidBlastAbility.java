package net.nighthawkempires.races.ability.voidwalker;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.util.RandomUtil;
import net.nighthawkempires.guilds.util.AllyUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import static org.bukkit.ChatColor.RED;

public class VoidBlastAbility implements Ability {

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
        return Material.BEACON;
    }

    public RaceType getRaceType() {
        return RaceType.VOIDWALKER;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(getRaceType(), 3);
    }

    public String getName() {
        return "Void Blast";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Now knocks enemies backwards."};
            case 3 -> new String[] {"Increase radius to 8 blocks."};
            case 4 -> new String[] {"Staggers enemies for 5s."};
            default -> new String[] {"By manipulating the End's energy", "Voidwalkers are able to summon a", "blast that stuns enemies.", "", "5 Block Radius"};
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
                int radius = level > 2 ? 8 : 5;

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have activated Void Blast"));

                player.getWorld().getEntitiesByClasses(new Class[] {LivingEntity.class, Projectile.class, Item.class }).stream().filter(
                        (target) -> target.getLocation().distance(player.getLocation()) <= radius).forEach((target) -> {
                            if (target instanceof LivingEntity entity) {
                                if ((!(entity instanceof Player) || !AllyUtil.isAlly((Player) entity, player))) {
                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 1));
                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                                }


                                if (level > 1) {
                                    double x = entity.getLocation().getX() - player.getLocation().getX();
                                    double z = entity.getLocation().getZ() - player.getLocation().getZ();

                                    Vector vector = new Vector(x, 0.14, z);

                                    entity.setVelocity(vector.normalize().multiply(1.5));
                                }
                            }
                });

                if (level > 3) {
                    for (int i = 0; i < 20 * getDuration(level); i++) {
                        if (i % 4 == 0) {
                            Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                                for (Entity entity : player.getWorld().getEntitiesByClasses(LivingEntity.class, Item.class)) {
                                    if (entity.getLocation().distance(player.getLocation()) <= radius) {
                                        if (!(entity instanceof Player) || !AllyUtil.isAlly(player, (Player) entity)) {
                                            double range = 1 * .6;
                                            double minimum = range / 2;
                                            double x = RandomUtil.randomDouble(range, 0.0D) - minimum;
                                            double z = RandomUtil.randomDouble(range, 0.0D) - minimum;
                                            entity.setVelocity(entity.getVelocity().add(new Vector(x, entity.getVelocity().getY(), z)));
                                        }
                                    }
                                }
                            }, i);
                        }
                    }
                }

                addCooldown(this, player, level);
            }
        }
    }

    public int getId() {
        return 47;
    }

    public int getDuration(int level) {
        return switch (level) {
            case 4 -> 5;
            default -> 0;
        };
    }
}
