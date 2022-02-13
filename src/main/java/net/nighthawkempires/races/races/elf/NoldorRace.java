package net.nighthawkempires.races.races.elf;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class NoldorRace /*implements Race*/ {

    public String getName() {
        return "Noldor";
    }

    /*public RaceType getRaceType() {
        return RaceType.ELF;
    }*/

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] { "Noldor Elves are a middle-ground between regality and calloused hands." +
                        " They retain the combat-focused abilities of the Vanyar, but gain others more tailored to their specific" +
                        " type of magic, and are typically inclined towards woodworking and similar work." +
                        " The primary skillset that they gain over Vanyar Elves is hunting."
        };
    }
}
