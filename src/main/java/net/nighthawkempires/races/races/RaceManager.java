package net.nighthawkempires.races.races;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.nighthawkempires.races.races.angel.CherubimRace;
import net.nighthawkempires.races.races.angel.SeraphimRace;
import net.nighthawkempires.races.races.human.RangerRace;
import net.nighthawkempires.races.races.angel.ThroneRace;
import net.nighthawkempires.races.races.demon.DevilRace;
import net.nighthawkempires.races.races.demon.LemureRace;
import net.nighthawkempires.races.races.dwarf.DerroRace;
import net.nighthawkempires.races.races.dwarf.DuergarRace;
import net.nighthawkempires.races.races.dwarf.UrdunnirRace;
import net.nighthawkempires.races.races.human.NomadRace;
import net.nighthawkempires.races.races.human.WitcherRace;
import net.nighthawkempires.races.races.demon.TieflingRace;
import net.nighthawkempires.races.races.voidwalker.RevenantRace;
import net.nighthawkempires.races.races.voidwalker.ShadeRace;
import net.nighthawkempires.races.races.voidwalker.WraithRace;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static net.nighthawkempires.races.races.RaceType.*;

public class RaceManager {

    private final HashMap<RaceType, List<Race>> raceMap;

    public RaceManager() {
        this.raceMap = Maps.newHashMap();

        raceMap.put(ANGEL, newArrayList(new ThroneRace(), new CherubimRace(), new SeraphimRace()));
        raceMap.put(DEMON, newArrayList(new TieflingRace(), new LemureRace(), new DevilRace()));
        raceMap.put(DWARF, newArrayList(new DerroRace(), new UrdunnirRace(), new DuergarRace()));
        // UPDATE 3 raceMap.put(ELF, newArrayList(new VanyarRace(), new NoldorRace(), new TeleriRace()));
        raceMap.put(HUMAN, newArrayList(new NomadRace(), new RangerRace(), new WitcherRace()));
        // UPDATE 2 raceMap.put(LYCAN, newArrayList(new OmegaRace(), new BetaRace(), new AlphaRace()));
        // UPDATE 3 raceMap.put(ORC, newArrayList(new UnderdarkRace(), new HillGiantRace(), new MountainousRace()));
        // UPDATE 4 raceMap.put(TRITON, newArrayList(new MerfolkRace(), new GlacialRace(), new AbyssalRace()));
        // UPDATE 2 raceMap.put(VAMPIRE, newArrayList(new FledglingRace(), new NightstalkerRace(), new AncillaeRace()));
        raceMap.put(VOIDWALKER, newArrayList(new ShadeRace(), new WraithRace(), new RevenantRace()));
    }

    public ImmutableMap<RaceType, List<Race>> getRaceMap() {
        return ImmutableMap.copyOf(this.raceMap);
    }

    public Race getRace(String name) {
        for (List<Race> lists : getRaceMap().values()) {
            for (Race race : lists) {
                if (race.getName().toLowerCase().equals(name.toLowerCase())) {
                    return race;
                }
            }
        }
        return null;
    }

    public Race getRace(RaceType type, int level) {
        switch (level) {
            case 1: level = 0; break;
            case 2: level = 1; break;
            case 3: level = 2; break;
            default: level = 0; break;
        }
        return raceMap.get(type).get(level);
    }

    public Race getDefaultRace() {
        return getRace(HUMAN, 1);
    }
}