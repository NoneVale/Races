package net.nighthawkempires.races.ability.human;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import static org.bukkit.ChatColor.RED;

public class AardAbility implements Ability {

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
        switch (level) {
            case 3:
            case 4:
                return 2;
            default:
                return 1;
        }
    }

    public Material getDisplayItem() {
        return Material.FEATHER;
    }

    public RaceType getRaceType() {
        return RaceType.HUMAN;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.HUMAN, 3);
    }

    public String getName() {
        return "Aard";
    }

    public String[] getDescription(int level) {
        return new String[0];
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof PlayerInteractEvent) {
            PlayerInteractEvent event = (PlayerInteractEvent) e;
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

                int radius = level < 2 ? 5 : 10;
                int height = level < 2 ? 2 : 5;
                for (Entity entity : player.getNearbyEntities(radius, height, radius)) {
                    if (entity instanceof LivingEntity) {
                        if (entity instanceof Player) {
                            Player target = (Player) entity;

                            if (level == 3) {
                                int random = Double.valueOf(Math.random() * 100).intValue();
                                if (random <= .35) {
                                    int randomSlot = Double.valueOf(Math.random() * 8).intValue() - 1;
                                    player.getInventory().setHeldItemSlot(randomSlot);
                                }
                            }
                        }

                        Vector center = player.getLocation().toVector();
                        center.subtract(entity.getLocation().toVector());
                        entity.setVelocity(center.normalize().multiply(1.5).setY(.14));
                        if (level == 4) {
                            int random = Double.valueOf(Math.random() * 100).intValue();
                            if (random <= .15) {
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 1));
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                            }
                        }
                    }
                }

                addCooldown(this, player, level);
            }
        }
    }

    public int getId() {
        return 38;
    }

    public int getDuration(int level) {
        return 0;
    }
}