package net.nighthawkempires.races.ability.angel;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.core.util.LocationUtil;
import net.nighthawkempires.guilds.GuildsPlugin;
import net.nighthawkempires.guilds.guild.GuildModel;
import net.nighthawkempires.guilds.util.AllyUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.logging.Level;

import static org.bukkit.ChatColor.RED;

public class LanceOfLonginusAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return switch (level) {
            case 4 -> 120 + getDuration(level);
            default -> 150 + getDuration(level);
        };
    }

    public int getMaxLevel() {
        return 4;
    }

    public int getCost(int level) {
        return switch (level) {
            case 3,4 -> 2;
            default -> 1;
        };
    }

    public Material getDisplayItem() {
        return Material.BLAZE_ROD;
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.ANGEL, 2);
    }

    public String getName() {
        return "Lance of Longinus";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Increases damage to 5, and applies", "Weakness I for 5s to target."};
            case 3 -> new String[] {"Increase damage to 7, applies", "Slowness I for 5s to target,", "and increases distance to 20 blocks."};
            case 4 -> new String[] {"Reduces cooldown to " + getCooldown(level) + "s."};
            default -> new String[] {"Angels summon a condensed beam of", "light that pierces through enemies.", "", "Deals 3 damage, 15 block distance"};
        };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof PlayerInteractEvent event) {
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
            GuildModel guild = GuildsPlugin.getUserRegistry().getUser(player.getUniqueId()).getGuild();

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

                int length = switch (level) {
                    case 3, 4 -> 20;
                    default -> 15;
                };

                double damage = switch (level) {
                    case 2 -> 5;
                    case 3, 4 -> 7;
                    default -> 3;
                };

                if (LocationUtil.getTargetPlayer(player, length, 0.5) == null) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "You must be looking at a player to activate this ability."));
                    return;
                }

                Player target = LocationUtil.getTargetPlayer(player, length, 0.5);
                GuildModel targetGuild = GuildsPlugin.getUserRegistry().getUser(target.getUniqueId()).getGuild();

                if (AllyUtil.isAlly(player, target)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "This ability can not be used on guild members or allies."));
                    return;
                }

                Location start = player.getEyeLocation();
                start.setY(player.getEyeLocation().getY() - .15);
                Location end = target.getEyeLocation();
                Vector vector = end.toVector().subtract(start.toVector());

                double distance = vector.length();
                for (double i = .01; i < distance; i += 0.25) {
                    vector.multiply(i);
                    start.add(vector);
                    start.getWorld().spawnParticle(Particle.WAX_ON, start, 3);
                    start.subtract(vector);
                    vector.normalize();
                }

                target.damage(damage, player);

                switch (level) {
                    case 2 -> {
                        target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0));
                    }
                    case 3 -> {
                        target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0));
                        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0));
                    }
                    default -> {}
                }

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have activated Lance of Longinus."));

                addCooldown(this, player, level);
            }
        }
    }

    public int getId() {
        return 5;
    }

    public int getDuration(int level) {
        return 0;
    }
}
