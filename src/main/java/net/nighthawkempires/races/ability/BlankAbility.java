package net.nighthawkempires.races.ability;

import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class BlankAbility implements Ability {

    public Ability.AbilityType getAbilityType() {
        return AbilityType.ACTIVE;
    }

    public int getCooldown(int level) {
        return 0;
    }

    public int getMaxLevel() {
        return 0;
    }

    public int getCost(int level) {
        return 0;
    }

    public Material getDisplayItem() {
        return Material.AIR;
    }

    public RaceType getRaceType() {
        return RaceType.VOIDWALKER;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(getRaceType(), 1);
    }

    public String getName() {
        return "Blank";
    }

    public String[] getDescription(int level) {
        return new String[] {
                ""
        };
    }

    public void run(Player player) {

    }

    public void run(Event event) {

    }

    public int getId() {
        return 0;
    }

    public int getDuration(int level) {
        return 0;
    }

}
