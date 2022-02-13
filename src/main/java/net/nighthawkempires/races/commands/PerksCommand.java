package net.nighthawkempires.races.commands;

import net.nighthawkempires.core.lang.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.getCommandManager;
import static net.nighthawkempires.core.CorePlugin.getMessages;
import static org.bukkit.ChatColor.GRAY;

public class PerksCommand implements CommandExecutor {

    public PerksCommand() {
        getCommandManager().registerCommands("perks", new String[] {
                "ne.races"
        });
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("ne.races")) {
                player.sendMessage(getMessages().getChatTag(Messages.NO_PERMS));
                return true;
            }

            Bukkit.dispatchCommand(player, "races perks");
            return true;
        } else {
            sender.sendMessage(getMessages().getChatMessage(GRAY + "This command is not available from the console."));
            return true;
        }
    }
}
