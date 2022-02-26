package net.nighthawkempires.races.ability.human;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static org.bukkit.ChatColor.RED;

public class MarkedForDeathAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.ACTIVE;
    }

    public int getCooldown(int level) {
        return switch (level) {
            case 2 -> 60 + getDuration(level);
            case 3 -> 45 + getDuration(level);
            default -> 90 + getDuration(level);
        };
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        return switch (level) {
            case 3 -> 2;
            default -> 1;
        };
    }

    public Material getDisplayItem() {
        return Material.TARGET;
    }

    public RaceType getRaceType() {
        return RaceType.HUMAN;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.HUMAN, 2);
    }

    public String getName() {
        return "Marked For Death";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Increase mark chance to 15%, increase", "damage to 20%, and reduce cooldown", "to " + getCooldown(level) + "s."};
            case 3 -> new String[] {"Increase mark chance to 20%, increase", "duration to 15s, and reduce cooldown", "to " + getCooldown(level) + "s."};
            default -> new String[] {"Attacking enemies with ranged weapons", "has a chance to mark them", "for death.", "", "10% chance to mark enemy", "15% damage increase if marked", "Duration: 10s"};
        };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        PlayerData.HumanData humanData = RacesPlugin.getPlayerData().human;
        if (e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    Player player = (Player) arrow.getShooter();
                    UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                    if (event.getEntity() instanceof Player) {
                        Player target = (Player) event.getEntity();

                        if (userModel.hasAbility(this)) {
                            if (checkCooldown(this, player, false)) return;

                            if (!canUseRaceAbility(player)) {
                                return;
                            } else if (isSyphoned(player)) {
                                return;
                            }

                            int level = userModel.getLevel(this);

                            if (humanData.markedForDeath.containsKey(player.getUniqueId())
                                    && humanData.markedForDeath.get(player.getUniqueId()) == target.getUniqueId()) {
                                double increase;
                                switch (level) {
                                    case 2:
                                    case 3:
                                        increase = .20;
                                        break;
                                    default:
                                        increase = .15;
                                        break;
                                }

                                event.setDamage((event.getDamage() * increase) + event.getDamage());
                            }

                            int chance;
                            switch (level) {
                                case 2:
                                    chance = 15;
                                    break;
                                case 3:
                                    chance = 20;
                                    break;
                                default:
                                    chance = 10;
                                    break;
                            }

                            int random = Double.valueOf(Math.random() * 100).intValue();
                            if (random <= chance) {
                                humanData.markedForDeath.put(player.getUniqueId(), target.getUniqueId());
                                //TODO: Set Target Glow
                                target.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have been marked for death by "
                                        + ChatColor.GREEN + player.getName() + ChatColor.GRAY + "."));
                                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have marked "
                                        + ChatColor.GREEN + player.getName() + ChatColor.GRAY + " for death."));

                                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                                    humanData.markedForDeath.remove(player.getUniqueId());
                                    //TODO: Remove Target Glow
                                    target.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You are no longer marked for death."));
                                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GREEN + target.getName() + ChatColor.GRAY + " is no longer marked for death."));
                                },  getDuration(level) * 20L);

                                addCooldown(this, player, level);
                            }
                        }
                    }
                }
            }
        }
    }

    public int getId() {
        return 34;
    }

    public int getDuration(int level) {
        switch (level) {
            case 3:
                return 15;
            default:
                return 10;
        }
    }
}
