package net.nighthawkempires.races.races.voidwalker;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class SilencerRace implements Race {

    public String getName() {
        return "Silencer";
    }

    public RaceType getRaceType() {
        return RaceType.VOID_WALKER;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Silencer Description"
        };
    }
}
