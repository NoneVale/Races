package net.nighthawkempires.races.races.infernal;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class TieflingRace implements Race {

    public String getName() {
        return "Tiefling";
    }

    public RaceType getRaceType() {
        return RaceType.INFERNAL;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] { "Tiefling are the least powerful caste of Infernal, and often share many human qualities" +
                " -- most can even pass as human." +
                "  But they have distinctive features that give them away as Infernals, namely their vestigial wings" +
                " (which can sometimes, but not always, be hidden if necessary)."
        };
    }
}
