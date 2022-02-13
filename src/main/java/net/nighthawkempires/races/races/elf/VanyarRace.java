package net.nighthawkempires.races.races.elf;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class VanyarRace {

    public String getName() {
        return "Vanyar";
    }

    /*public RaceType getRaceType() {
        return RaceType.ELF;
    }*/

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] { "Young Elves or Elven initiates, Vanyar are characterized by their almost reckless abandon and lack" +
                        " of control over their more advanced abilities. They are often quick to combat and have" +
                        " certain abilities that cater to that, but the more defensive and nature-based magic that" +
                        " characterizes older Elves has a small presence."
        };
    }
}
