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
                int level = userModel.getLevel(this);
                if (CorePlugin.getCooldowns().hasActiveCooldown(player.getUniqueId(),
                        this.getClass().getSimpleName().toLowerCase())) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "There is another "
                            + CorePlugin.getCooldowns().getActive(player.getUniqueId(), this.getClass().getSimpleName().toLowerCase()).timeLeft()
                            + " before you can use this ability again."));
                    return;
                }

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

                CorePlugin.getCooldowns().addCooldown(new Cooldown(player.getUniqueId(),
                        this.getClass().getSimpleName().toLowerCase(),
                        (System.currentTimeMillis() + (getCooldown(level) * 1000L))));
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