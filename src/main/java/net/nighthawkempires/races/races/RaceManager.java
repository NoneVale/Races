package net.nighthawkempires.races.races;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.nighthawkempires.races.races.angel.ArchAngelRace;
import net.nighthawkempires.races.races.angel.CherubimRace;
import net.nighthawkempires.races.races.angel.SeraphimRace;
import net.nighthawkempires.races.races.aurora.AbyssalAuroraRace;
import net.nighthawkempires.races.races.aurora.GlacialAuroraRace;
import net.nighthawkempires.races.races.aurora.NewbornAuroraRace;
import net.nighthawkempires.races.races.demon.KingOfHellRace;
import net.nighthawkempires.races.races.demon.KnightOfHellRace;
import net.nighthawkempires.races.races.demon.PrinceOfHellRace;
import net.nighthawkempires.races.races.dwarf.DerroRace;
import net.nighthawkempires.races.races.dwarf.DuergarRace;
import net.nighthawkempires.races.races.dwarf.UrdunnirRace;
import net.nighthawkempires.races.races.elf.NoldorRace;
import net.nighthawkempires.races.races.elf.TeleriRace;
import net.nighthawkempires.races.races.elf.VanyarRace;
import net.nighthawkempires.races.races.human.HunterRace;
import net.nighthawkempires.races.races.human.PriestRace;
import net.nighthawkempires.races.races.human.WitcherRace;
import net.nighthawkempires.races.races.orc.GruntRace;
import net.nighthawkempires.races.races.orc.WarbossRace;
import net.nighthawkempires.races.races.orc.WarchiefRace;
import net.nighthawkempires.races.races.vampire.VampireFledglingRace;
import net.nighthawkempires.races.races.vampire.VampireLordRace;
import net.nighthawkempires.races.races.vampire.VampireNightstalkerRace;
import net.nighthawkempires.races.races.voidwalker.ElytronRace;
import net.nighthawkempires.races.races.voidwalker.ShadeRace;
import net.nighthawkempires.races.races.voidwalker.WraithRace;
import net.nighthawkempires.races.races.werewolf.AlphaRace;
import net.nighthawkempires.races.races.werewolf.BetaRace;
import net.nighthawkempires.races.races.werewolf.OmegaRace;

import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static net.nighthawkempires.races.races.RaceType.*;

public class RaceManager {

    private HashMap<RaceType, List<Race>> raceMap;

    public RaceManager() {
        this.raceMap = Maps.newHashMap();

        raceMap.put(ANGEL, newArrayList(new SeraphimRace(), new CherubimRace(), new ArchAngelRace()));
        raceMap.put(AURORA, newArrayList(new NewbornAuroraRace(), new GlacialAuroraRace(), new AbyssalAuroraRace()));
        raceMap.put(DEMON, newArrayList(new KnightOfHellRace(), new PrinceOfHellRace(), new KingOfHellRace()));
        raceMap.put(DWARF, newArrayList(new DerroRace(), new UrdunnirRace(), new DuergarRace()));
        raceMap.put(ELF, newArrayList(new VanyarRace(), new NoldorRace(), new TeleriRace()));
        raceMap.put(HUMAN, newArrayList(new HunterRace(), new PriestRace(), new WitcherRace()));
        raceMap.put(ORC, newArrayList(new GruntRace(), new WarchiefRace(), new WarbossRace()));
        raceMap.put(VAMPIRE, newArrayList(new VampireFledglingRace(), new VampireNightstalkerRace(), new VampireLordRace()));
        raceMap.put(VOIDWALKER, newArrayList(new ShadeRace(), new WraithRace(), new ElytronRace()));
        raceMap.put(WEREWOLF, newArrayList(new OmegaRace(), new BetaRace(), new AlphaRace()));
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