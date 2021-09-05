package net.nighthawkempires.races.races;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.nighthawkempires.races.races.celestial.CherubimRace;
import net.nighthawkempires.races.races.celestial.SeraphimRace;
import net.nighthawkempires.races.races.aurora.AbyssalAuroraRace;
import net.nighthawkempires.races.races.aurora.GlacialAuroraRace;
import net.nighthawkempires.races.races.aurora.NewbornAuroraRace;
import net.nighthawkempires.races.races.celestial.ThroneRace;
import net.nighthawkempires.races.races.infernal.DevilRace;
import net.nighthawkempires.races.races.infernal.LemureRace;
import net.nighthawkempires.races.races.dwarf.DerroRace;
import net.nighthawkempires.races.races.dwarf.DuergarRace;
import net.nighthawkempires.races.races.dwarf.UrdunnirRace;
import net.nighthawkempires.races.races.elf.NoldorRace;
import net.nighthawkempires.races.races.elf.TeleriRace;
import net.nighthawkempires.races.races.elf.VanyarRace;
import net.nighthawkempires.races.races.human.HunterRace;
import net.nighthawkempires.races.races.human.PriestRace;
import net.nighthawkempires.races.races.human.WitcherRace;
import net.nighthawkempires.races.races.infernal.TieflingRace;
import net.nighthawkempires.races.races.orc.GruntRace;
import net.nighthawkempires.races.races.orc.WarbossRace;
import net.nighthawkempires.races.races.orc.WarchiefRace;
import net.nighthawkempires.races.races.vampire.VampireFledglingRace;
import net.nighthawkempires.races.races.vampire.VampireLordRace;
import net.nighthawkempires.races.races.vampire.VampireNightstalkerRace;
import net.nighthawkempires.races.races.voidwalker.ElytronRace;
import net.nighthawkempires.races.races.voidwalker.ShadeRace;
import net.nighthawkempires.races.races.voidwalker.WraithRace;
import net.nighthawkempires.races.races.lycan.AlphaRace;
import net.nighthawkempires.races.races.lycan.BetaRace;
import net.nighthawkempires.races.races.lycan.OmegaRace;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static net.nighthawkempires.races.races.RaceType.*;

public class RaceManager {

    private HashMap<RaceType, List<Race>> raceMap;

    public RaceManager() {
        this.raceMap = Maps.newHashMap();

        raceMap.put(AURORA, newArrayList(new NewbornAuroraRace(), new GlacialAuroraRace(), new AbyssalAuroraRace()));
        raceMap.put(CELESTIAL, newArrayList(new ThroneRace(), new CherubimRace(), new SeraphimRace()));
        raceMap.put(DWARF, newArrayList(new DerroRace(), new UrdunnirRace(), new DuergarRace()));
        raceMap.put(ELF, newArrayList(new VanyarRace(), new NoldorRace(), new TeleriRace()));
        raceMap.put(HUMAN, newArrayList(new HunterRace(), new PriestRace(), new WitcherRace()));
        raceMap.put(INFERNAL, newArrayList(new TieflingRace(), new LemureRace(), new DevilRace()));
        raceMap.put(LYCAN, newArrayList(new OmegaRace(), new BetaRace(), new AlphaRace()));
        raceMap.put(ORC, newArrayList(new GruntRace(), new WarchiefRace(), new WarbossRace()));
        raceMap.put(VAMPIRE, newArrayList(new VampireFledglingRace(), new VampireNightstalkerRace(), new VampireLordRace()));
        raceMap.put(VOIDWALKER, newArrayList(new ShadeRace(), new WraithRace(), new ElytronRace()));
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