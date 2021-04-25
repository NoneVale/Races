package net.nighthawkempires.races.event;

import net.nighthawkempires.races.races.Race;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class RaceChangeEvent extends Event {

    private static HandlerList handlers = new HandlerList();
    private final Player player;
    private final UUID uuid;
    private final Race race;

    public RaceChangeEvent(Player player, Race race) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.race = race;
    }

    public Player getPlayer() {
        return player;
    }

    public Race getRace() {
        return race;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public UUID getUniqueId() {
        return uuid;
    }
}
