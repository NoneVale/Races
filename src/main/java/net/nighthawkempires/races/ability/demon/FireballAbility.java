package net.nighthawkempires.races.ability.demon;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
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
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.ChatColor.RED;

public class FireballAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return 60 + getDuration(level);
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCost(int level) {
        return 2;
    }

    public Material getDisplayItem() {
        return Material.FIRE_CHARGE;
    }

    public RaceType getRaceType() {
        return RaceType.DEMON;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DEMON, 1);
    }

    public String getName() {
        return "Fireball";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Fireball will now set blocks on", "fire."};
            case 3 -> new String[] {"Doubles fireball explosion radius."};
            case 4 -> new String[] {"Fireball velocity increased."};
            case 5 -> new String[] {"Fireball has a aftershock effect that", "stuns nearby entities."};
            default -> new String[] {"Demons are capable of shooting fireballs."};
        };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        PlayerData.DemonData demonData = RacesPlugin.getPlayerData().demon;
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

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have activated Fireball."));

                Fireball fireball = player.launchProjectile(Fireball.class);
                fireball.setShooter(player);

                boolean incendiary = level > 1;
                float yield = level > 2 ? fireball.getYield() * 2 : fireball.getYield();

                fireball.setIsIncendiary(incendiary);
                fireball.setYield(yield);

                if (level > 3) {
                    fireball.setVelocity(fireball.getLocation().getDirection().normalize().multiply(2.0));
                }

                if (level > 4) {
                    demonData.fireball.add(fireball);
                }

                addCooldown(this, player, level);
            }
        } else if (e instanceof ProjectileHitEvent event) {
            if (event.getEntity() instanceof Fireball fireball) {
                if (demonData.fireball.contains(fireball)) {
                    demonData.fireball.remove(fireball);
                    if (fireball.getShooter() instanceof Player player) {
                        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                        if (userModel.hasAbility(this) && userModel.getLevel(this) > 4) {
                            Location hitLocation = (event.getHitEntity() != null ? event.getHitEntity().getLocation() :
                                    (event.getHitBlock() != null ? event.getHitBlock().getLocation() : null));

                            if (hitLocation != null) {
                                for (Entity entity : hitLocation.getNearbyEntities(5, 5, 5)) {
                                    if (entity instanceof LivingEntity livingEntity) {
                                        if ((!(livingEntity instanceof Player) || !AllyUtil.isAlly((Player) livingEntity, player))) {
                                            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
                                            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 1));
                                            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                                        }
                                    }
                                }
                                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                                    hitLocation.getWorld().createExplosion(hitLocation, 2f, false, false);
                                }, 30);
                            }
                        }
                    }
                }
            }
        } else if (e instanceof EntityExplodeEvent event) {
            if (event.getEntity() instanceof Fireball) {

            }
        }
    }

    public int getId() {
        return 13;
    }

    public int getDuration(int level) {
        return 0;
    }
}
