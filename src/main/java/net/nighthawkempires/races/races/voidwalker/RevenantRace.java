package net.nighthawkempires.races.races.voidwalker;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

public class RevenantRace implements Race {

  public String getName() {
    return "Revenant";
  }

  public RaceType getRaceType() {
    return RaceType.VOIDWALKER;
  }

  public int getTier() {
    return 3;
  }

  public String[] getDescription() {
    return new String[] { "Voidwalkers have no constant governmental structure, no leaders, no army; but if they did, the Revenant would be the highest of their ranks."
            + "  They are elders, ancients in every right; each of their abilities has been developed and honed to an expert level,"
            + " and while an inherent part of Voidwalker society is that there is always more to learn, Revenant are often heralded as keepers of knowledge."
    };
  }
}