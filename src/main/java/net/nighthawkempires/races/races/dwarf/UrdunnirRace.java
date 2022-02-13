package net.nighthawkempires.races.races.dwarf;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class UrdunnirRace implements Race {

    public String getName() {
        return "Urdunnir";
    }

    public RaceType getRaceType() {
        return RaceType.DWARF;
    }

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] { "Strong and extremely skilled in defensive combat, Urdunnirs make up the majority of Dwarven defense forces." +
                        " While they are excellent in combat, they have not yet gained the mastery of creation and metalwork" +
                        " that characterizes the Duergar. "
        };
    }
}
