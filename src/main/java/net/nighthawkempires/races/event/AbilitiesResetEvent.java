package net.nighthawkempires.races.event;

import net.nighthawkempires.races.ability.Ability;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;
import java.util.UUID;

public class AbilitiesResetEvent extends Event {

    private static HandlerList handlers = new HandlerList();
    private final Player player;
    private final UUID uuid;
    private final List<Ability> abilities;

    public AbilitiesResetEvent(Player player, List<Ability> abilities) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.abilities = abilities;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Ability> getAbilities() {
        return abilities;
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
