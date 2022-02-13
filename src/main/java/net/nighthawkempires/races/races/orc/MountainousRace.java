package net.nighthawkempires.races.races.orc;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class MountainousRace {

    public String getName() {
        return "Mountainous";
    }

    /*public RaceType getRaceType() {
        return RaceType.ORC;
    }*/

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] { "Mountainous Orcs maintain the magical prowess of Hill Giants, and use it to enhance their defensive" +
                        " and offensive combat abilities. They are typically the oldest and most experienced Orcs;" +
                        " they are both finely attuned to the nature and source of their magic, and are among" +
                        " the best fighters in the world."
        };
    }
}
