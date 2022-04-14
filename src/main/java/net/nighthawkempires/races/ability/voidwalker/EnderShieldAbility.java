package net.nighthawkempires.races.ability.voidwalker;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import net.nighthawkempires.regions.util.RegionUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.checkerframework.checker.units.qual.C;

public class EnderShieldAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.ACTIVE;
    }

    public int getCooldown(int level) {
        return 20 + getDuration(level);
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.ENDER_PEARL;
    }

    public RaceType getRaceType() {
        return RaceType.VOIDWALKER;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(getRaceType(), 3);
    }

    public String getName() {
        return "Ender Shield";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Increase absorbed damage to 50%."};
            case 3 -> new String[] {"Add additional pearl to shield."};
            case 4 -> new String[] {"Deflect 10% of damage when shield", "is broken."};
            case 5 -> new String[] {"Increase deflect to 20%."};
            default -> new String[] {"Dropping an Ender Pearl summons a", "shield around the Voidwalker.", "", "Absorb 20% combat damage."};
        };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        PlayerData.VoidwalkerData voidwalkerData = RacesPlugin.getPlayerData().voidwalker;
        if (e instanceof PlayerDropItemEvent event) {
            if (event.getItemDrop().getItemStack().getType() == Material.ENDER_PEARL) {
                Player player = event.getPlayer();
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this)) {
                    if (checkCooldown(this, player)) return;

                    if (!canUseRaceAbility(player)) {
                        player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "You can not use Race Abilities here."));
                        return;
                    } else if (isSyphoned(player)) {
                        player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "Your powers are being syphoned by a demon."));
                        return;
                    }

                    int level = userModel.getLevel(this);

                    if ((level < 3 && voidwalkerData.getPearls(player) > 0) || (level > 2 && voidwalkerData.getPearls(player) > 1)) {
                        player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "You already have the max amount of charges added to your shield."));
                        return;
                    }

                    voidwalkerData.addPearl(player);
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have added a charge to your Ender Shield"));

                    addCooldown(this, player, level);
                }
            }
        } else if (e instanceof EntityDamageByEntityEvent event) {
            if (event.getEntity() instanceof Player player) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this) && voidwalkerData.getPearls(player) > 0) {
                    if (!RegionUtil.canPVP(player) || event.isCancelled()) return;

                    voidwalkerData.removePearl(player);

                    int level = userModel.getLevel(this);

                    double absorbed = switch (level) {
                        case 2, 3, 4, 5 -> .50;
                        default -> .25;
                    };

                    double reflected = switch (level) {
                        case 4 -> .10;
                        case 5 -> .20;
                        default -> 0;
                    };

                    event.setDamage(event.getDamage() - (event.getDamage() * absorbed));
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Your Ender Shield absorbed some damage."));

                    if (reflected > 0) {
                        if (event.getDamager() instanceof LivingEntity entity) {
                            EntityDamageByEntityEvent hitEvent = new EntityDamageByEntityEvent(entity, player, EntityDamageEvent.DamageCause.CUSTOM, event.getDamage() * reflected);
                            Bukkit.getPluginManager().callEvent(hitEvent);

                            if (!hitEvent.isCancelled()) {
                                entity.damage(hitEvent.getFinalDamage());
                                entity.setLastDamageCause(hitEvent);
                                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Your Ender Shield reflected damage onto your opponent."));
                                if (entity instanceof Player target) {
                                    target.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Your opponent's Ender Shield reflected damage back onto you."));
                                }
                            }
                        } else if (event.getDamager() instanceof Projectile projectile) {
                            if (projectile.getShooter() instanceof LivingEntity entity) {
                                EntityDamageByEntityEvent hitEvent = new EntityDamageByEntityEvent(entity, player, EntityDamageEvent.DamageCause.CUSTOM, event.getDamage() * reflected);
                                Bukkit.getPluginManager().callEvent(hitEvent);

                                if (!hitEvent.isCancelled()) {
                                    entity.damage(hitEvent.getFinalDamage());
                                    entity.setLastDamageCause(hitEvent);
                                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Your Ender Shield reflected damage onto your opponent."));
                                    if (entity instanceof Player target) {
                                        target.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Your opponent's Ender Shield reflected damage back onto you."));
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
        return 48;
    }

    public int getDuration(int level) {
        return 0;
    }
}
