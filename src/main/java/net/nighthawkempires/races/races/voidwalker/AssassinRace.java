package net.nighthawkempires.races.races.voidwalker;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class AssassinRace implements Race {

    public String getName() {
        return "Assassin";
    }

    public RaceType getRaceType() {
        return RaceType.VOID_WALKER;
    }

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Assassin Description"
        };
    }
}
