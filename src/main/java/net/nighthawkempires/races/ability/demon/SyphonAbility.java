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
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static org.bukkit.ChatColor.RED;

public class SyphonAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return 300 + getDuration(level);
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        return switch (level) {
            case 2, 3 -> 1;
            default -> 2;
        };
    }

    public Material getDisplayItem() {
        return Material.SOUL_LANTERN;
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
        return switch (level) {
            case 2 -> new String[]{"Increase duration to 10s."};
            case 3 -> new String[]{"You now steal health from syphoned", "players, increase duration to 15s."};
            default -> new String[]{"Demons can syphon the powers of", "others, making them unable to use", "their abilities.", "", "Duration 5s"};
        };
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
                if (checkCooldown(this, player)) return;

                if (!canUseRaceAbility(player)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(RED + "You can not use Race Abilities here."));
                    return;
                } else if (isSyphoned(player)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(RED + "Your powers are being syphoned by a demon."));
                    return;
                }

                int level = userModel.getLevel(this);

                infernalData.syphon.add(player.getUniqueId());
                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Syphon has been activated."));
                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    infernalData.arcaneResistance.remove(player.getUniqueId());
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Syphon has worn off due to not being used."));
                }, 600L);

                addCooldown(this, player, level);
            }
        } else if (e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
            if (event.getDamager() instanceof Player player) {
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
            } else if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player player) {
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
        return 18;
    }

    public int getDuration(int level) {
        return switch (level) {
            case 2 -> 10;
            case 3 -> 15;
            default -> 5;
        };
    }
}