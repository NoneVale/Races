package net.nighthawkempires.races.races.lycan;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class OmegaRace {

    public String getName() {
        return "Omega";
    }

    /*public RaceType getRaceType() {
        return RaceType.LYCAN;
    }*/

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] {"The weakest of the three Lycan development stages, Omegas are typically young," +
                        " newly-infected, or otherwise inexperienced." +
                        "  Their wolfish forms tend to be more human than those of higher stages," +
                        " and that form doesn’t come with some of the tell-tale physical" +
                        " changes present in Betas or (especially) Alphas (such as wolfish ears and eyes, patches" +
                        " of fur, claws, and enlarged canine teeth)."
        };
    }
}