package net.nighthawkempires.races.tabcompleters;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.lang.Messages;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.binding.BindingManager;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

import static net.nighthawkempires.core.CorePlugin.getMessages;

public class BindTabCompleter implements TabCompleter {

    private final String[] help = new String[] {
            getMessages().getMessage(Messages.CHAT_HEADER),
            ChatColor.translateAlternateColorCodes('&', "&8Command&7: Bind   &8-   [Optional], <Required>"),
            getMessages().getMessage(Messages.CHAT_FOOTER),
            getMessages().getCommand("bind", "help", "Show this help menu."),
            getMessages().getCommand("bind", "list", "Show a list of bindable abilities."),
            getMessages().getCommand("bind", "current", "Shows a list of abilities bound to item."),
            getMessages().getCommand("bind", "<id>", "Bind an ability to the item in hand."),
            getMessages().getCommand("bind", "remove <id>", "Unbind an ability to the item in hand."),
            getMessages().getMessage(Messages.CHAT_FOOTER)
    };

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = Lists.newArrayList();
        BindingManager bindingManager = RacesPlugin.getBindingManager();
        if (sender instanceof Player player) {
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
            if (!player.hasPermission("ne.races")) return completions;

            switch (args.length) {
                case 1 -> {
                    List<String> options = Lists.newArrayList("help", "list", "current", "remove");
                    if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                        List<Ability> bound = bindingManager.getBindings(player.getInventory().getItemInMainHand());

                        for (Ability ability : userModel.getAbilities()) {
                            if (ability.getAbilityType() == Ability.AbilityType.BOUND && !bound.contains(ability)) {
                                options.add(String.valueOf(ability.getId()));
                            }
                        }
                    }

                    StringUtil.copyPartialMatches(args[0], options, completions);
                    Collections.sort(completions);
                    return completions;
                }
                case 2 -> {
                    switch (args[0].toLowerCase()) {
                        case "remove" -> {
                            List<String> options = Lists.newArrayList();
                            if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                                List<Ability> bound = bindingManager.getBindings(player.getInventory().getItemInMainHand());

                                for (Ability ability : bound) {
                                    if (ability.getAbilityType() == Ability.AbilityType.BOUND) {
                                        options.add(String.valueOf(ability.getId()));
                                    }
                                }
                            }

                            StringUtil.copyPartialMatches(args[0], options, completions);
                            Collections.sort(completions);
                            return completions;
                        }
                        default -> {
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
