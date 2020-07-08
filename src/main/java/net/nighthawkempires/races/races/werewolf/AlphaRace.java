package net.nighthawkempires.races.races.werewolf;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class AlphaRace implements Race {

    public String getName() {
        return "Alpha";
    }

    public RaceType getRaceType() {
        return RaceType.WEREWOLF;
    }

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Alpha Description"
        };
    }
}
