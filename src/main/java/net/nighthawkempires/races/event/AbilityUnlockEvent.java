package net.nighthawkempires.races.event;

import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class AbilityUnlockEvent extends Event {

    private static HandlerList handlers = new HandlerList();
    private final Player player;
    private final UUID uuid;
    private final Ability ability;

    public AbilityUnlockEvent(Player player, Ability ability) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.ability = ability;
    }

    public Player getPlayer() {
        return player;
    }

    public Ability getAbility() {
        return ability;
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