package net.nighthawkempires.races.races.vampire;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class VampireLordRace implements Race {

    public String getName() {
        return "Vampire Lord";
    }

    public RaceType getRaceType() {
        return RaceType.VAMPIRE;
    }

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Vampire Lord Description"
        };
    }
}
