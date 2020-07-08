package net.nighthawkempires.races.races.elf;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class VanyarRace implements Race {

    public String getName() {
        return "Vanyar";
    }

    public RaceType getRaceType() {
        return RaceType.ELF;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Vanyar Description"
        };
    }
}
