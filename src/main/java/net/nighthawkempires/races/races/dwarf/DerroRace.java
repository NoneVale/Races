package net.nighthawkempires.races.races.dwarf;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class DerroRace implements Race {

    public String getName() {
        return "Derro";
    }

    public RaceType getRaceType() {
        return RaceType.DWARF;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] { "Derro are Dwarven youth or initiates. They are often faster and more spry than their elders," +
                        " but are nowhere near as strong; while older Dwarves are known for their fine metalwork and defensive" +
                        " resilience, Derro are often more skilled at offensive combat, something looked down on as the" +
                        " harrows of youth by older Dwarves."
        };
    }
}