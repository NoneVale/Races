package net.nighthawkempires.races.tabcompleters;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.lang.Messages;
import net.nighthawkempires.races.races.RaceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

import static net.nighthawkempires.core.CorePlugin.getMessages;

public class RaceTabCompleter implements TabCompleter {

    private final String[] help = new String[] {
            getMessages().getMessage(Messages.CHAT_HEADER),
            ChatColor.translateAlternateColorCodes('&', "&8Command&7: Races   &8-   [Optional], <Required>"),
            getMessages().getMessage(Messages.CHAT_FOOTER),
            getMessages().getCommand("races", "help", "Show this help menu."),
            getMessages().getCommand("races", "gui", "Open the races GUI."),
            getMessages().getCommand("races", "list", "Show a list of all races."),
            getMessages().getCommand("races", "perks", "Open the perk menu."),
            getMessages().getCommand("races", "abilities", "Open the perk menu."),
            getMessages().getCommand("races", "info [race]", "Show info about a race."),
            getMessages().getCommand("races", "infection <race>", "Show infections for a race."),
            getMessages().getCommand("races", "recipes <race>", "Show recipes for a race."),
            getMessages().getMessage(Messages.CHAT_FOOTER)
    };

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = Lists.newArrayList();
        if (sender instanceof Player player) {
            if (!player.hasPermission("ne.races")) return completions;

            switch (args.length) {
                case 1 -> {
                    List<String> options = Lists.newArrayList("help", "gui", "list", "perks", "abilities", "info", "infection", "recipes");
                    StringUtil.copyPartialMatches(args[0], options, completions);
                    Collections.sort(completions);
                    return completions;
                }
                case 2 -> {
                    List<String> options = Lists.newArrayList();
                    for (RaceType raceType : RaceType.values()) {
                        options.add(raceType.getName());
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

        return completions;
    }
}
