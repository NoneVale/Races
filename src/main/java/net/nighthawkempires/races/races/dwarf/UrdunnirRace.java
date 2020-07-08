package net.nighthawkempires.races.races.dwarf;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class UrdunnirRace implements Race {

    public String getName() {
        return "Urdunnir";
    }

    public RaceType getRaceType() {
        return RaceType.DWARF;
    }

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Urdunnir Description"
        };
    }
}
