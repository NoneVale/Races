package net.nighthawkempires.races.races.human;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

public class RangerRace implements Race {

    public String getName() {
        return "Ranger";
    }

    public RaceType getRaceType() {
        return RaceType.HUMAN;
    }

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] { "Humans who are particularly experienced eventually become Rangers. They have more control over their magic," +
                        " and may actually be sought out by Testificates for their perceived divine power and priestly knowledge."
        };
    }
}
