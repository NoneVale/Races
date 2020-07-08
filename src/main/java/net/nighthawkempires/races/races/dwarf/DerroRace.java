package net.nighthawkempires.races.races.dwarf;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class DerroRace implements Race {

    public String getName() {
        return "Derro";
    }

    public RaceType getRaceType() {
        return RaceType.DWARF;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "DerroD Description"
        };
    }
}
