package net.nighthawkempires.races.races.celestial;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.*;

public class ThroneRace implements Race {

    public String getName() {
        return "Throne";
    }

    public RaceType getRaceType() {
        return RaceType.CELESTIAL;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] {"Thrones were once the guardians of the gods’ temples and holy lands, and were the" +
                        " most powerful class of Celestial who travelled outside the Ether." +
                        "  While they are powerful, they are not nearly as powerful as the higher castes."
        };
    }
}
