package net.nighthawkempires.races.listeners;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.event.RaceChangeEvent;
import net.nighthawkempires.races.event.RaceUpgradeEvent;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InfectionListener implements Listener {

    @EventHandler
    public void onChange(RaceChangeEvent event) {
        Player player = event.getPlayer();
        UserModel user = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        user.clearAbilities();
        user.setPerkPoints(0);
        user.setRace(event.getRace());

        player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You are now a "
                + event.getRace().getRaceType().getRaceColor() + event.getRace().getName() + ChatColor.GRAY + "."));
    }

    @EventHandler
    public void onUpgrade(RaceUpgradeEvent event) {
        Player player = event.getPlayer();
        UserModel user = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        user.setRace(event.getRace());

        player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You are now a "
                + event.getRace().getRaceType().getRaceColor() + event.getRace().getName() + ChatColor.GRAY + "."));
    }
}