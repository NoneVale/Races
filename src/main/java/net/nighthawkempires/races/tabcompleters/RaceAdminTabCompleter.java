package net.nighthawkempires.races.tabcompleters;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.lang.Messages;
import net.nighthawkempires.races.races.RaceType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

import static net.nighthawkempires.core.CorePlugin.getMessages;

public class RaceAdminTabCompleter implements TabCompleter {

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

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = Lists.newArrayList();
        if (sender instanceof Player player) {
            if (!player.hasPermission("ne.racesadmin")) return completions;

            switch (args.length) {
                case 1 -> {
                    List<String> options = Lists.newArrayList("help", "raceitem", "setpoints", "setrace");
                    StringUtil.copyPartialMatches(args[0], options, completions);
                    Collections.sort(completions);
                    return completions;
                }
                case 2 -> {
                    switch (args[0].toLowerCase()) {
                        case "raceitem" -> {
                            List<String> options = Lists.newArrayList();
                            for (RaceType raceType : RaceType.values()) {
                                options.add(raceType.getName());
                            }

                            StringUtil.copyPartialMatches(args[1], options, completions);
                            Collections.sort(completions);
                            return completions;
                        }
                        case "setpoints", "setrace" -> {
                            List<String> options = Lists.newArrayList();
                            for (Player online : Bukkit.getOnlinePlayers()) {
                                options.add(online.getName());
                            }

                            StringUtil.copyPartialMatches(args[1], options, completions);
                            Collections.sort(completions);
                            return completions;
                        }
                        default -> {
                            return completions;
                        }
                    }
                }
                case 3 -> {
                    switch (args[0].toLowerCase()) {
                        case "raceitem" -> {
                            List<String> options = Lists.newArrayList();
                            for (Player online : Bukkit.getOnlinePlayers()) {
                                options.add(online.getName());
                            }

                            StringUtil.copyPartialMatches(args[2], options, completions);
                            Collections.sort(completions);
                            return completions;
                        }
                        case "setrace" -> {
                            List<String> options = Lists.newArrayList();
                            for (RaceType raceType : RaceType.values()) {
                                options.add(raceType.getName());
                            }

                            StringUtil.copyPartialMatches(args[2], options, completions);
                            Collections.sort(completions);
                            return completions;
                        }
                        default -> {
                            return completions;
                        }
                    }
                }
                case 4 -> {
                    switch (args[0].toLowerCase()) {
                        case "setrace" -> {
                            List<String> options = Lists.newArrayList("1", "2", "3");
                            StringUtil.copyPartialMatches(args[3], options, completions);
                            Collections.sort(completions);
                            return completions;
                        }
                    }
                }
                default -> {
                    return completions;
                }
            }
        }
        return completions;
    }
}