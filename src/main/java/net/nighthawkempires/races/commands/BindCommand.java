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
import org.bukkit.inventory.ItemStack;

import static net.nighthawkempires.core.CorePlugin.getMessages;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class BindCommand implements CommandExecutor {

    private final String[] help = new String[] {
            getMessages().getMessage(Messages.CHAT_HEADER),
            ChatColor.translateAlternateColorCodes('&', "&8Command&7: Races   &8-   [Optional], <Required>"),
            getMessages().getMessage(Messages.CHAT_FOOTER),
            getMessages().getCommand("bind", "help", "Show this help menu."),
            getMessages().getCommand("bind", "list", "Show a list of bindable abilities."),
            getMessages().getCommand("bind", "<id>", "Bind an ability to the item in hand."),
            getMessages().getMessage(Messages.CHAT_FOOTER)
    };

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
                            break;
                        case "list":
                            String[] info = new String[] {
                                    getMessages().getMessage(Messages.CHAT_HEADER),
                                    translateAlternateColorCodes('&', "&8Ability List"),
                                    getMessages().getMessage(Messages.CHAT_FOOTER),
                                    translateAlternateColorCodes('&', "&8Ability IDs&7:")
                            };
                            player.sendMessage(info);

                            for (Ability ability : userModel.getAbilities()) {
                                if (ability.getAbilityType() == Ability.AbilityType.ACTIVE) {
                                    player.sendMessage(DARK_GRAY + " - [" + GOLD + ability.getId() + DARK_GRAY
                                            + "][" + ability.getRaceType().getRaceColor() + ability.getName() + DARK_GRAY + "]");
                                }
                            }

                            player.sendMessage(getMessages().getMessage(Messages.CHAT_FOOTER));
                            break;
                        default:
                            if (!NumberUtils.isDigits(args[0])) {
                                player.sendMessage(getMessages().getChatMessage(RED + "That is an invalid ability id."));
                                return true;
                            }

                            int id = Integer.parseInt(args[0]);

                            Ability ability = RacesPlugin.getAbilityManager().getAbility(id);
                            if (ability == null) {
                                player.sendMessage(getMessages().getChatMessage(RED + "The provided ID does not belong to any abilities."));
                                return true;
                            }

                            if (!userModel.hasAbility(ability)) {
                                player.sendMessage(getMessages().getChatMessage(RED + "You currently do not have that ability unlocked."));
                                return true;
                            }

                            if (ability.getAbilityType() == Ability.AbilityType.PASSIVE) {
                                player.sendMessage(getMessages().getChatMessage(RED + "You can not bind passive abilities."));
                                return true;
                            }

                            ItemStack itemStack = player.getEquipment().getItemInMainHand();
                            if (itemStack != null && !itemStack.getType().isAir()) {
                                BindingManager bindingManager = RacesPlugin.getBindingManager();

                                if (itemStack.getType().isBlock() || itemStack.getType().isEdible()) {
                                    player.sendMessage(getMessages().getChatMessage(RED + "You can not bind abilities to placeable or edible items."));
                                    return true;
                                }

                                if (bindingManager.getBindings(itemStack).size() == 0) {
                                    player.getEquipment().setItemInMainHand(bindingManager.setBinder(
                                            bindingManager.addBinding(itemStack, RacesPlugin.getAbilityManager().getAbility(0)),
                                            player.getUniqueId()));
                                    player.saveData();

                                    itemStack = player.getEquipment().getItemInMainHand();
                                }

                                if (bindingManager.getBindings(itemStack).contains(ability)) {
                                    player.sendMessage(getMessages().getChatMessage(RED + "This ability is already bound to the item in hand."));
                                    return true;
                                }

                                player.getEquipment().setItemInMainHand(RacesPlugin.getBindingManager().addBinding(itemStack, ability));
                                player.saveData();

                                player.sendMessage(getMessages().getChatMessage(GRAY + "You have successfully bound " + userModel.getRace().getRaceType().getRaceColor()
                                        + ability.getName() + GRAY + " to the item in your hand."));
                            } else {
                                player.sendMessage(getMessages().getChatMessage(RED + "You do not currently have anything in your hand."));
                                return true;
                            }
                    }
                    return true;
                default:
                    player.sendMessage(getMessages().getChatTag(Messages.INVALID_SYNTAX));
                    return true;
            }
        } else {

        }
        return false;
    }
}
