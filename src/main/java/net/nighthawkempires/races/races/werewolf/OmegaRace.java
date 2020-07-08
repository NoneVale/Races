package net.nighthawkempires.races.races.werewolf;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class OmegaRace implements Race {

    public String getName() {
        return "Omega";
    }

    public RaceType getRaceType() {
        return RaceType.WEREWOLF;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Omega Description"
        };
    }
}
