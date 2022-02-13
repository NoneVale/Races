package net.nighthawkempires.races.ability.demon;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SyphonAbility implements Ability {
    public AbilityType getAbilityType() {
        return null;
    }

    public int getCooldown(int level) {
        return 300 + getDuration(level);
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        int cost = 0;
        switch (level) {
            case 2:
            case 3: cost = 1; break;
            default: cost = 2; break;
        }
        return cost;
    }

    public Material getDisplayItem() {
        return Material.RABBIT_HIDE;
    }

    public RaceType getRaceType() {
        return RaceType.DEMON;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DEMON, 3);
    }

    public String getName() {
        return "Syphon";
    }

    public String[] getDescription(int level) {
        return new String[0];
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        PlayerData.DemonData infernalData = RacesPlugin.getPlayerData().demon;
        if (e instanceof PlayerInteractEvent) {
            PlayerInteractEvent event = (PlayerInteractEvent) e;
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                if (infernalData.syphoned.contains(player.getUniqueId())) return;

                int level = userModel.getLevel(this);

                infernalData.syphon.add(player.getUniqueId());
                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Syphon has been activated."));
                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    infernalData.arcaneResistance.remove(player.getUniqueId());
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Syphon has worn off due to not being used."));
                }, 600L);
            }
        } else if (e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this)) {
                    int level = userModel.getLevel(this);
                    if (event.getEntity() instanceof Player) {
                        Player target = (Player) event.getEntity();
                        if (infernalData.syphoned.contains(player.getUniqueId())) return;

                        if (infernalData.syphon.contains(player.getUniqueId())) {
                            infernalData.syphon.remove(player.getUniqueId());
                            infernalData.syphoned.add(target.getUniqueId());

                            target.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Your powers are being syphoned."));
                            player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You are syphoning " + ChatColor.GREEN
                                    + target.getName() + "'s" + ChatColor.GRAY + " powers."));

                            if (level == getMaxLevel()) {
                                if (target.getHealth() <= 4.0) {
                                    player.setHealth(player.getHealth() + (target.getHealth() - 2.0));
                                    target.setHealth(2.0);
                                } else {
                                    target.setHealth(target.getHealth() - 4.0);
                                    player.setHealth(player.getHealth() + 4.0);
                                }
                            }

                            Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                                infernalData.syphoned.remove(target.getUniqueId());
                                target.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Your powers are no longer being syphoned."));
                                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GREEN + target.getName() + "'s" + ChatColor.GRAY
                                        + " powers are no longer being syphoned."));
                            }, getDuration(level) * 20L);
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
        int duration = 0;
        switch (level) {
            case 2: duration = 10; break;
            case 3: duration = 15; break;
            default: duration = 5; break;
        }
        return duration;
    }
}