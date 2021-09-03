package net.nighthawkempires.races.races.voidwalker;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

public class ElytronRace implements Race {

  public String getName() {
    return "Elytron";
  }

  public RaceType getRaceType() {
    return RaceType.VOIDWALKER;
  }

  public int getTier() {
    return 3;
  }

  public String[] getDescription() {
    return new String[] { "Voidwalkers have no constant governmental structure, no leaders, no army; but if they did, the Elytron would be the highest of their ranks."
            + "  They are elders, ancients in every right; each of their abilities has been developed and honed to an expert level,"
            + " and while an inherent part of Voidwalker society is that there is always more to learn, Elytron are often heralded as keepers of knowledge."
            + "  At this stage, some minor physical changes may be present (such as black or pearlescent eyes, patches of scales, and similar),"
            + " but the “rite of passage,” so to speak, that makes an Elytron from a Wraith is the development of their wings."
            + "  Elytron have a pair of otherworldly, Void-forged wings permanently attached to their back."
            + "  These wings may be bird-like and feathered; scaled like a dragon’s; or even shiny like a beetle’s." };
  }
}
