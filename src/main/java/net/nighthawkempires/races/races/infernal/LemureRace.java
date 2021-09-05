package net.nighthawkempires.races.races.infernal;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class LemureRace implements Race {

    public String getName() {
        return "Lemure";
    }

    public RaceType getRaceType() {
        return RaceType.INFERNAL;
    }

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] { "Lemures are the descendants of Infernals who dealt directly with souls and the mortal world." +
                "  Their work caused them to partly fuse with the souls they managed, giving them a distinct" +
                " ghostly tinge -- though they are not invisible and cannot become invisible like Voidwalkers can." +
                "  Lemures have a much more difficult time passing as human due to this slight transparency of their skin."
        };
    }
}
