package net.nighthawkempires.races.races.triton;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class MerfolkRace implements Race {

    public String getName() {
        return "Merfolk";
    }

    public RaceType getRaceType() {
        return RaceType.TRITON;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Newborn Aurora Description"
        };
    }
}
