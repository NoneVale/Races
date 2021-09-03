package net.nighthawkempires.races.races.voidwalker;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

public class WraithRace implements Race {

    public String getName() {
        return "Wraith";
    }

    public RaceType getRaceType() {
        return RaceType.VOIDWALKER;
    }

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] {
                "These Voidwalkers have mastered many of their abilities, but some are still lost on them, and although the"
                        + " youthful brashness is almost never present in Wraiths, they may still retain some illogical or naive thinking patterns."
                        + "  Wraiths have full control over their phasing, and can phase in and out of existence at will;"
                        + " they have also honed and mastered other abilities, but otherwise, no physical transformations have taken place by this stage."
        };
    }
}