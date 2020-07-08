package net.nighthawkempires.races.races.elf;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class NoldorRace implements Race {

    public String getName() {
        return "Noldor";
    }

    public RaceType getRaceType() {
        return RaceType.ELF;
    }

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Noldor Description"
        };
    }
}
