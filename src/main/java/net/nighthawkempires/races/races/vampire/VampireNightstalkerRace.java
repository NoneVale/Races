package net.nighthawkempires.races.races.vampire;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class VampireNightstalkerRace implements Race {

    public String getName() {
        return "Vampire Nightstalker";
    }

    public RaceType getRaceType() {
        return RaceType.VAMPIRE;
    }

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Vampire Nightstalker Description"
        };
    }
}
