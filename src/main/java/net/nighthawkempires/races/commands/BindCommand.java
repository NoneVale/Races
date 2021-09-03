package net.nighthawkempires.races.commands;

import net.nighthawkempires.core.lang.Messages;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.binding.BindingManager;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.nighthawkempires.core.CorePlugin.getMessages;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class BindCommand implements CommandExecutor {

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
                    return true;
                case 1:
                    String arg1 = args[0];

                    if (!NumberUtils.isDigits(arg1)) {
                        player.sendMessage(getMessages().getChatMessage(RED + "That is an invalid ability id."));
                        return true;
                    }

                    int id = Integer.parseInt(arg1);

                    Ability ability = RacesPlugin.getAbilityManager().getAbility(id);
                    if (ability == null) {
                        player.sendMessage(getMessages().getChatMessage(RED + "The provided ID does not belong to any abilities."));
                        return true;
                    }

                    if (!userModel.hasAbility(ability)) {
                        player.sendMessage(getMessages().getChatMessage(RED + "You currently do not have that ability unlocked."));
                        return true;
                    }

                    if (player.getEquipment().getItemInMainHand() != null) {
                        BindingManager bindingManager = RacesPlugin.getBindingManager();

                        if (bindingManager.getBindings(player.getEquipment().getItemInMainHand()).size() == 0) {
                            player.getEquipment().setItemInMainHand(bindingManager.setBinder(
                                    bindingManager.addBinding(player.getEquipment().getItemInMainHand(), RacesPlugin.getAbilityManager().getAbility(0)),
                                    player.getUniqueId()));
                            player.saveData();
                        }

                        if (bindingManager.getBindings(player.getEquipment().getItemInMainHand()).contains(ability)) {
                            player.sendMessage(getMessages().getChatMessage(RED + "This ability is already bound to the item in hand."));
                            return true;
                        }

                        player.getEquipment().setItemInMainHand(RacesPlugin.getBindingManager().addBinding(player.getEquipment().getItemInMainHand(), ability));
                        player.saveData();

                        player.sendMessage(getMessages().getChatMessage(GRAY + "You have successfully bound " + userModel.getRace().getRaceType().getRaceColor()
                                + ability.getName() + GRAY + " to the item in your hand."));
                    } else {
                        player.sendMessage(getMessages().getChatMessage(RED + "You do not currently have anything in your hand."));
                        return true;
                    }


                    return true;
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
        } else {

        }
        return false;
    }
}
