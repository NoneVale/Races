package net.nighthawkempires.races.commands;

import net.nighthawkempires.core.lang.Messages;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.event.RaceChangeEvent;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.core.lang.Messages.PLAYER_NOT_FOUND;
import static net.nighthawkempires.core.lang.Messages.PLAYER_NOT_ONLINE;
import static org.bukkit.ChatColor.*;

public class RacesAdminCommand implements CommandExecutor {

    public RacesAdminCommand() {
        getCommandManager().registerCommands("racesadmin", new String[] {
                "ne.racesadmin"
        });
    }

    private final String[] help = new String[] {
            getMessages().getMessage(Messages.CHAT_HEADER),
            ChatColor.translateAlternateColorCodes('&', "&8Command&7: Bind   &8-   [Optional], <Required>"),
            getMessages().getMessage(Messages.CHAT_FOOTER),
            getMessages().getCommand("racesadmin", "help", "Show this help menu."),
            getMessages().getCommand("racesadmin", "raceitem <race> [player]", "Spawns infection item for given race."),
            getMessages().getCommand("racesadmin", "setpoints <player> <points>", "Set a player's Perk Points."),
            getMessages().getCommand("racesadmin", "setrace <player> <race> <level>", "Set a player's race."),
            getMessages().getMessage(Messages.CHAT_FOOTER)
    };

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.racesadmin")) {
                player.sendMessage(getMessages().getChatTag(Messages.NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    break;
                case 1:
                    switch (args[0].toLowerCase()) {
                        case "help":
                            player.sendMessage(help);
                            break;
                        default:
                            player.sendMessage(getMessages().getChatTag(Messages.INVALID_SYNTAX));
                            break;
                    }
                case 2:
                    switch (args[0].toLowerCase()) {
                        case "raceitem":
                            String name = args[1];
                            RaceType raceType = RaceType.getRace(name.toUpperCase());
                            if (raceType == null) {
                                player.sendMessage(getMessages().getChatMessage(RED + "That race does not exist, make sure you spelled it correctly."));
                                return true;
                            }

                            player.getInventory().addItem(raceType.getRaceItem());
                            player.sendMessage(getMessages().getChatMessage(GRAY + "The race item for " + raceType.getRaceColor() + raceType.getName()
                                    + GRAY + " has been added to your inventory."));
                            return true;
                        default:
                            player.sendMessage(getMessages().getChatTag(Messages.INVALID_SYNTAX));
                            return true;
                    }
                case 3:
                    switch (args[0].toLowerCase()) {
                        case "raceitem":
                            String name = args[1];
                            RaceType raceType = RaceType.getRace(name.toUpperCase());
                            if (raceType == null) {
                                player.sendMessage(getMessages().getChatMessage(RED + "That race does not exist, make sure you spelled it correctly."));
                                return true;
                            }

                            name = args[2];
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                            if (offlinePlayer.isOnline()) {
                                Player target = offlinePlayer.getPlayer();

                                target.getInventory().addItem(raceType.getRaceItem());
                                target.sendMessage(getMessages().getChatMessage(GRAY + "The race item for " + raceType.getRaceColor() + raceType.getName()
                                        + GRAY + " has been added to your inventory."));
                                player.sendMessage(getMessages().getChatMessage(GRAY + "You have added race item for " + raceType.getRaceColor()
                                        + raceType.getName() + GRAY + " has been added to " + GREEN + target.getName() + "'s " + GRAY + "inventory."));
                                return true;
                            } else {
                                player.sendMessage(getMessages().getChatTag(PLAYER_NOT_ONLINE));
                                return true;
                            }
                        case "setpoints":
                            name = args[1];
                            offlinePlayer = Bukkit.getOfflinePlayer(name);

                            if (!RacesPlugin.getUserRegistry().userExists(offlinePlayer.getUniqueId())) {
                                player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                                return true;
                            }

                            UserModel target = RacesPlugin.getUserRegistry().getUser(offlinePlayer.getUniqueId());

                            if (!NumberUtils.isDigits(args[2])) {
                                player.sendMessage(getMessages().getChatMessage(GRAY + "Please make sure the time is a valid number."));
                                return true;
                            }

                            int points = Integer.parseInt(args[2]);
                            target.setPerkPoints(points);
                            player.sendMessage(getMessages().getChatMessage(GRAY + "You have set " + GREEN + offlinePlayer.getName() + "'s"
                                    + GRAY + " Perk Points to " + GOLD + points + GRAY + "."));
                            if (offlinePlayer.isOnline())
                                offlinePlayer.getPlayer().sendMessage(getMessages().getChatMessage(GRAY + "Your Perk Points have been set to " + GOLD + points + GRAY + "."));
                            break;
                        default:
                            player.sendMessage(getMessages().getChatTag(Messages.INVALID_SYNTAX));
                            break;
                    }
                case 4:
                    switch (args[0].toLowerCase()) {
                        case "setrace":
                            String name = args[1];
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);

                            if (!RacesPlugin.getUserRegistry().userExists(offlinePlayer.getUniqueId())) {
                                player.sendMessage(getMessages().getChatTag(PLAYER_NOT_FOUND));
                                return true;
                            }

                            UserModel target = RacesPlugin.getUserRegistry().getUser(offlinePlayer.getUniqueId());

                            name = args[2];
                            RaceType raceType = RaceType.getRace(name.toUpperCase());
                            if (raceType == null) {
                                player.sendMessage(getMessages().getChatMessage(RED + "That race does not exist, make sure you spelled it correctly."));
                                return true;
                            }

                            if (NumberUtils.isDigits(args[3])) {
                                int level = Integer.parseInt(args[3]);
                                if (level < 1 || level > 3) {
                                    player.sendMessage(getMessages().getChatMessage(RED + "Please choose a level between 1 and 3."));
                                    return true;
                                }

                                Race race = RacesPlugin.getRaceManager().getRace(raceType, level);

                                player.sendMessage(getMessages().getChatMessage(GRAY + "You have set " + GREEN + offlinePlayer.getName() + "'s" + GRAY
                                        + " race to " + raceType.getRaceColor() + race.getName() + GRAY + "."));
                                if (offlinePlayer.isOnline()) {
                                    Bukkit.getPluginManager().callEvent(new RaceChangeEvent(offlinePlayer.getPlayer(), race));
                                } else {
                                    target.clearAbilities();
                                    target.setPerkPoints(0);
                                    target.setRace(race);
                                }
                                return true;
                            } else {
                                Race race = RacesPlugin.getRaceManager().getRace(args[3]);
                                if (race == null) {
                                    player.sendMessage(getMessages().getChatMessage(RED + "That race does not exist."));
                                    return true;
                                }

                                if (race.getRaceType() != raceType) {
                                    player.sendMessage(getMessages().getChatMessage(RED + "That race is not a sub-race of that racetype."));
                                    return true;
                                }

                                player.sendMessage(getMessages().getChatMessage(GRAY + "You have set " + GREEN + offlinePlayer.getName() + "'s" + GRAY
                                        + " race to " + raceType.getRaceColor() + race.getName() + GRAY + "."));
                                if (offlinePlayer.isOnline()) {
                                    Bukkit.getPluginManager().callEvent(new RaceChangeEvent(offlinePlayer.getPlayer(), race));
                                } else {
                                    target.clearAbilities();
                                    target.setPerkPoints(0);
                                    target.setRace(race);
                                }
                                return true;
                            }
                        default:
                            player.sendMessage(getMessages().getChatTag(Messages.INVALID_SYNTAX));
                            break;
                    }
            }
            return true;
        } else {
            //TODO: Shit here
            return true;
        }
    }
}
