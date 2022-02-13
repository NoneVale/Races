package net.nighthawkempires.races.races.elf;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;


public class TeleriRace {

    public String getName() {
        return "Teleri";
    }

    /*public RaceType getRaceType() {
        return RaceType.ELF;
    }*/

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] { "Teleri Elves are what many would consider “High Elves.” They are the oldest caste of Elf," +
                        " and are known for their regality, fluid movements, silent steps, and hunting skill. "
        };
    }
}
