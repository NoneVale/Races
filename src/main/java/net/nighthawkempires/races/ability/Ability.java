package net.nighthawkempires.races.ability;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public interface Ability {

    AbilityType getAbilityType();

    int getCooldown(int level);

    int getMaxLevel();

    int getCost(int level);

    Material getDisplayItem();

    RaceType getRaceType();

    Race getRace();

    String getName();

    String[] getDescription(int level);

    void run(Player player);

    void run(Event e);

    int getId();

    int getDuration(int level);

    public enum AbilityType {
        ACTIVE, PASSIVE
    }
}
