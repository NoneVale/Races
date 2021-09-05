package net.nighthawkempires.races.races.infernal;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class DevilRace implements Race {

    public String getName() {
        return "Devil";
    }

    public RaceType getRaceType() {
        return RaceType.INFERNAL;
    }

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "King of Hell Description"
        };
    }
}
