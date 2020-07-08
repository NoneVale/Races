package net.nighthawkempires.races.races.vampire;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class VampireFledglingRace implements Race {

    public String getName() {
        return "Vampire Fledgling";
    }

    public RaceType getRaceType() {
        return RaceType.VAMPIRE;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Vampire Fledgling Description"
        };
    }
}
