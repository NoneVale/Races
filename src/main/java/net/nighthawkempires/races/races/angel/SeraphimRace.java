package net.nighthawkempires.races.races.angel;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import org.bukkit.ChatColor;

import static org.bukkit.ChatColor.*;

public class SeraphimRace implements Race {

    public String getName() {
        return "Seraphim";
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Seraphim Description"
        };
    }
}
