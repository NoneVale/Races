package net.nighthawkempires.races.listeners;

import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserModel user = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        user.setRace(RacesPlugin.getRaceManager().getRace(RaceType.VOIDWALKER, 3));
        user.addAbility(RacesPlugin.getAbilityManager().getAbility(81), 3);
        user.addAbility(RacesPlugin.getAbilityManager().getAbility(82), 3);
        user.addAbility(RacesPlugin.getAbilityManager().getAbility(84), 4);
        user.addAbility(RacesPlugin.getAbilityManager().getAbility(85), 3);
        user.addAbility(RacesPlugin.getAbilityManager().getAbility(87), 1);

        player.setHealthScaled(true);
        player.setHealthScale(user.getRace().getRaceType().getBaseHealth() + (double) user.getRace().getTier());

        AttributeInstance instance = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double health = user.getRace().getRaceType().getBaseHealth() + (double) user.getRace().getTier();

        if (instance.getBaseValue() != health) {
            instance.setBaseValue(health);
            player.saveData();
        }
    }
}