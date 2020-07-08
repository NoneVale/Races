package net.nighthawkempires.races.races.angel;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class ArchAngelRace implements Race {

    public String getName() {
        return "Arch Angel";
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Arch Angel Description"
        };
    }
}
