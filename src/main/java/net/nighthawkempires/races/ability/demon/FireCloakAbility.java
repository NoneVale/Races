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
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.ChatColor.RED;

public class FireCloakAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return 120 + getDuration(level);
    }

    public int getMaxLevel() {
        return 4;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.ORANGE_CANDLE;
    }

    public RaceType getRaceType() {
        return RaceType.DEMON;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DEMON, 1);
    }

    public String getName() {
        return "Fire Cloak";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] { "Attacking enemies now gain Slowness I." };
            case 3 -> new String[] { "Entities in a 5 block radius", "are set on fire." };
            case 4 -> new String[] { "Attacking enemies now gain Slowness II", "and Weakness I."};
            default -> new String[] { "By drawing energy from the underworld", "demons are able to set those", "who attack them on fire.", "", "Duration: 10s" };
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

                demonData.fireCloaked.add(player.getUniqueId());

                if (level > 2) {
                    for (Entity entity : player.getWorld().getEntitiesByClass(LivingEntity.class)) {
                        if (!(entity instanceof Player) || !AllyUtil.isAlly(player, (Player) entity)) {
                            if (entity.getLocation().distance(player.getLocation()) <= 5.0) {
                                entity.setFireTicks(getDuration(level) * 10);
                            }
                        }
                    }
                }

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Fire Cloak has been activated."));
                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    demonData.fireCloaked.remove(player.getUniqueId());
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Fire Cloak has worn off."));
                }, 600L);

                addCooldown(this, player, level);
            }
        } else if (e instanceof EntityDamageByEntityEvent event) {
            if (event.getEntity() instanceof Player player) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this) && demonData.fireCloaked.contains(player.getUniqueId())) {
                    int level = userModel.getLevel(this);

                    if (event.getDamager() instanceof LivingEntity entity) {
                        entity.setFireTicks(getDuration(level) * 5);
                        if (level > 1) {
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, getDuration(level) * 5, 0));
                        }

                        if (level > 3) {
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, getDuration(level) * 5, 1));
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, getDuration(level) * 5, 0));
                        }
                    }
                }
            }
        }
    }

    public int getId() {
        return 12;
    }

    public int getDuration(int level) {
        return 10;
    }
}
