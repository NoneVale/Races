package net.nighthawkempires.races.races;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.nighthawkempires.core.chat.tag.PlayerTag;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.entity.Player;

public class RaceTag extends PlayerTag {

    public String getName() {
        return "race";
    }

    public int getPriority() {
        return 5;
    }

    public TextComponent getFor(Player player) {
        UserModel user = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
        TextComponent tag = new TextComponent("[");
        tag.setColor(ChatColor.DARK_GRAY);
        TextComponent mid = new TextComponent(user.getRace().getRaceType().getInitial());
        mid.setColor(ChatColor.valueOf(user.getRace().getRaceType().getRaceColor().name()));
        tag.addExtra(mid);
        tag.addExtra("]");
        tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                "&7Race: " + ChatColor.valueOf(user.getRace().getRaceType().getRaceColor().name()) + user.getRace().getRaceType().name().substring(0, 1)
                        + user.getRace().getRaceType().name().substring(1).toLowerCase() + "\n" + "&7Class: " + ChatColor.valueOf(user.getRace().getRaceType().getRaceColor().name()) + user.getRace().getName()))));
        return tag;
    }
}