package net.nighthawkempires.races.races.voidwalker;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

public class ShadeRace implements Race {

  public String getName() {
    return "Shade";
  }

  public RaceType getRaceType() {
    return RaceType.VOIDWALKER;
  }

  public int getTier() {
    return 1;
  }

  public String[] getDescription() {
    return new String[] { "These Voidwalkers are young, inexperienced, and oftentimes brash or foolish -- contrary to what typically makes a Voidwalker."
            + "  They have little control over the abilities given to them, so while these abilities may “activate” during moments of intense emotion or high"
            + " stress, the Voidwalker likely has little or no control over them beyond basic, intrinsic abilities." };
  }
}