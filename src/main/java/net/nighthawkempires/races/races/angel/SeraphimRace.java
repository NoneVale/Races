package net.nighthawkempires.races.races.angel;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

public class SeraphimRace implements Race {

    public String getName() {
        return "Seraphim";
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] { "Perhaps the most powerful Celestials aside from those still serving the gods directly," +
                " Seraphim were once the only beings who could speak to the gods face-to-face." +
                "  They were the keepers and maintainers of the gods’ thrones, and delivered messages to lower castes of Celestials."
        };
    }
}
