package net.nighthawkempires.races.races.human;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class WitcherRace implements Race {

    public String getName() {
        return "Witcher";
    }

    public RaceType getRaceType() {
        return RaceType.HUMAN;
    }

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] { "Highly experienced with all facets of life, Witchers are the most powerful of humans thus far." +
                        " They are finely attuned to the rhythms of the Overworld, and can perform miraculous feats by channeling its magic."
        };
    }
}