package net.nighthawkempires.races.ability.vampire;

import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class VampirismAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return 1800;
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        return 2;
    }

    public Material getDisplayItem() {
        return Material.RED_CANDLE;
    }

    public RaceType getRaceType() {
        return RaceType.HUMAN;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(getRaceType(), 3);
    }

    public String getName() {
        return "Vampirism";
    }

    public String[] getDescription(int level) {
        return new String[] { " " };
    }

    public void run(Player player) {

    }

    public void run(Event e) {


        // Scheduler to finalize infection
        // Scheduler to give player infection symptoms
        // Event to see if infected player goes offline to pause infection
        // Event to see if infected player kills vampire that infected them

    }

    public int getId() {
        return 81;
    }

    public int getDuration(int level) {
        return 3600;
    }
}