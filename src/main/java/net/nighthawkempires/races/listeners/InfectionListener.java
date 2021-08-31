package net.nighthawkempires.races.listeners;

import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.event.RaceChangeEvent;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class InfectionListener implements Listener {

    @EventHandler
    public void onChange(RaceChangeEvent event) {
        Player player = event.getPlayer();
        UserModel user = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        user.setPerkPoints(0);
    }
}