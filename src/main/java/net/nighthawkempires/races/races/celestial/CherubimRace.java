package net.nighthawkempires.races.races.celestial;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.*;

public class CherubimRace implements Race {

    public String getName() {
        return "Cherubim";
    }

    public RaceType getRaceType() {
        return RaceType.CELESTIAL;
    }

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] { "The next step up from Thrones, Cherubim were the guardians and keepers of the" +
                " Worldspawn and the entrance to the gods’ thrones." +
                "  They would watch over the prophets of the gods and had a hand in performing miracles."
        };
    }
}
