package net.nighthawkempires.races.ability.angel;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.guilds.GuildsPlugin;
import net.nighthawkempires.guilds.guild.GuildModel;
import net.nighthawkempires.guilds.util.AllyUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.ChatColor.RED;

public class ShelteringWingsAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return 120;
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
        return Material.ELYTRA;
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.ANGEL, 1);
    }

    public String getName() {
        return "Sheltering Wings";
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
                if (checkCooldown(this, player)) return;

                if (!canUseRaceAbility(player)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(RED + "You can not use Race Abilities here."));
                    return;
                } else if (isSyphoned(player)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(RED + "Your powers are being syphoned by a demon."));
                    return;
                }

                int level = userModel.getLevel(this);

                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, getDuration(level) * 20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, getDuration(level) * 20, 0));

                if (level > 2) {
                    GuildModel guild = GuildsPlugin.getUserRegistry().getUser(player.getUniqueId()).getGuild();
                    int radius = switch (level) {
                        case 3 -> 5;
                        case 4 -> 8;
                        default -> 0;
                    };

                    for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
                        if (entity instanceof Player target) {
                            GuildModel targetGuild = GuildsPlugin.getUserRegistry().getUser(target.getUniqueId()).getGuild();

                            if (AllyUtil.isAlly(player, target)) {
                                target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, getDuration(level) * 20, 0));
                                target.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, getDuration(level) * 20, 0));
                            }
                        }
                    }
                }

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have activated Sheltering Wings."));

                addCooldown(this, player, level);
            }
        }
    }

    public int getId() {
        return 3;
    }

    public int getDuration(int level) {
        return switch (level) {
            case 2,3,4 -> 15;
            default -> 10;
        };
    }
}