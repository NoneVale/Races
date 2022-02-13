package net.nighthawkempires.races.races.demon;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

public class DevilRace implements Race {

    public String getName() {
        return "Devil";
    }

    public RaceType getRaceType() {
        return RaceType.DEMON;
    }

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] { "Devils are the descendents of Demons who were direct prophets, scribes, or messengers to the gods." +
                        " Like Seraphim Angels, they used to be the only beings who could speak to their gods face-to-face," +
                        " and are perhaps the most powerful of Demons besides those who still serve the gods directly."
        };
    }
}
