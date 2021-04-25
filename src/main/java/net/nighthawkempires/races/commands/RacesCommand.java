package net.nighthawkempires.races.commands;

import net.nighthawkempires.core.lang.Messages;
import net.nighthawkempires.core.lang.ServerMessage;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static org.bukkit.ChatColor.*;

public class RacesCommand implements CommandExecutor {

    public RacesCommand() {
        getCommandManager().registerCommands("races", new String[] {
                "ne.races"
        });
    }

    private String[] help = new String[] {
            getMessages().getMessage(Messages.CHAT_HEADER),
            ChatColor.translateAlternateColorCodes('&', "&8Command&7: Races   &8-   [Optional], <Required>"),
            getMessages().getMessage(Messages.CHAT_FOOTER),
            getMessages().getCommand("races", "help", "Show this help menu."),
            getMessages().getCommand("races", "gui", "Open the races GUI."),
            getMessages().getCommand("races", "list", "Show a list of all races."),
            getMessages().getCommand("races", "perks", "Open the perk menu."),
            getMessages().getCommand("races", "info [race]", "Show info about a race."),
            getMessages().getCommand("races", "help <race>", "Show help for a race."),
            getMessages().getCommand("races", "recipes <race>", "Show recipes for a race."),
            getMessages().getMessage(Messages.CHAT_FOOTER)
    };

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (!player.hasPermission("ne.races")) {
                player.sendMessage(getMessages().getChatTag(Messages.NO_PERMS));
                return true;
            }

            switch (args.length) {
                case 0:
                    player.sendMessage(help);
                    return true;
                case 1:
                    switch (args[0].toLowerCase()) {
                        case "help":
                            player.sendMessage(help);
                            return true;
                        case "gui":
                        case "list":
                            StringBuilder raceBuilder = new StringBuilder();
                            for (int i = 0; i < RaceType.values().length; i++) {
                                raceBuilder.append(DARK_GRAY).append(" - ").append(RaceType.values()[i].getRaceColor())
                                        .append(enumName(RaceType.values()[i].name())).append(GRAY).append(": ");
                                for (int j = 1; j <= 3; j++) {
                                    raceBuilder.append(RaceType.values()[i].getRaceColor()).append(RacesPlugin.getRaceManager().getRace(RaceType.values()[i], j).getName());
                                    if (j < 3) {
                                        raceBuilder.append(DARK_GRAY).append(", ");
                                    }
                                }
                                if (i < RaceType.values().length - 1) {
                                    raceBuilder.append("\n");
                                }
                            }

                            String[] list = new String[] {
                                    getMessages().getMessage(Messages.CHAT_HEADER),
                                    translateAlternateColorCodes('&', "&8List&7: Races"),
                                    getMessages().getMessage(Messages.CHAT_FOOTER),
                                    translateAlternateColorCodes('&', "&8Races&7: "),
                                    raceBuilder.toString(),
                                    getMessages().getMessage(Messages.CHAT_FOOTER)
                            };
                            player.sendMessage(list);
                            return true;
                        case "perks":
                        case "info":
                            String[] info = new String[] {
                                    getMessages().getMessage(Messages.CHAT_HEADER),
                                    translateAlternateColorCodes('&', "&8Race Info&7: "
                                            + userModel.getRace().getRaceType().getRaceColor() + userModel.getRace().getName()),
                                    getMessages().getMessage(Messages.CHAT_FOOTER),
                                    translateAlternateColorCodes('&', "&8Race Type&7: "
                                            + userModel.getRace().getRaceType().getRaceColor() + enumName(userModel.getRace().getRaceType().name())),
                                    translateAlternateColorCodes('&', "&8Perk Points&7: &6" + userModel.getPerkPoints()),
                                    translateAlternateColorCodes('&', "&8Race Type Description&7: "),
                                    translateAlternateColorCodes('&', "&7" + userModel.getRace().getRaceType().getRaceDescriptionString()),
                                    translateAlternateColorCodes('&', "&8Race Description&7: "),
                                    translateAlternateColorCodes('&', "&7" + userModel.getRace().getDescriptionString()),
                                    getMessages().getMessage(Messages.CHAT_FOOTER),
                            };

                            player.sendMessage(info);
                            return true;
                        default:
                            player.sendMessage(getMessages().getChatTag(Messages.INVALID_SYNTAX));
                            return true;
                    }
                case 2:
                    switch (args[0]) {
                        case "info":
                        case "help":
                        case "recipes":
                        default:
                            player.sendMessage(getMessages().getChatTag(Messages.INVALID_SYNTAX));
                            return true;
                    }
                default:
                    player.sendMessage(getMessages().getChatTag(Messages.INVALID_SYNTAX));
                    return true;
            }
        } else if (sender instanceof ConsoleCommandSender) {

        }
        return false;
    }

    private String enumName(String s) {
        if (s.contains("_")) {
            String[] split = s.split("_");

            StringBuilder matName = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                matName.append(enumName(split[i]));

                if (i < split.length - 1) {
                    matName.append(" ");
                }
            }

            return matName.toString();
        }

        return s.toUpperCase().substring(0, 1) + s.substring(1).toLowerCase();
    }
}
