package net.nighthawkempires.races.races.elf;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class TeleriRace implements Race {

    public String getName() {
        return "Teleri";
    }

    public RaceType getRaceType() {
        return RaceType.ELF;
    }

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Teleri Description"
        };
    }
}
