package net.nighthawkempires.races.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    public AngelData angel;
    public DemonData demon;
    public DwarfData dwarf;
    public HumanData human;
    public VampireData vampire;
    public VoidwalkerData voidwalker;

    public PlayerData() {
        angel = new AngelData();
        demon = new DemonData();
        dwarf = new DwarfData();
        human = new HumanData();
        vampire = new VampireData();
        voidwalker = new VoidwalkerData();
    }

    public class AngelData {

        public List<UUID> celestialFaithAbility;
        public List<UUID> deathWard;
        public List<UUID> heavenlyResistance;
        public List<UUID> heavenlyBarrier;
        public List<World> rainList;

        public HashMap<UUID, List<Block>> concentratedGround;
        public HashMap<UUID, List<UUID>> vexMap;

        public AngelData() {
            celestialFaithAbility = Lists.newArrayList();
            deathWard = Lists.newArrayList();
            heavenlyResistance = Lists.newArrayList();
            heavenlyBarrier = Lists.newArrayList();
            rainList = Lists.newArrayList();

            concentratedGround = Maps.newHashMap();
            vexMap = Maps.newHashMap();
        }
    }

    public class DemonData {

        public List<Fireball> fireball;
        public List<UUID> fireCloaked;
        public List<UUID> syphon;
        public List<UUID> syphoned;

        // UNUSED
        public List<UUID> arcaneResistance;


        public DemonData() {
            fireball = Lists.newArrayList();
            fireCloaked = Lists.newArrayList();
            syphon = Lists.newArrayList();
            syphoned = Lists.newArrayList();

            // UNUSED
            arcaneResistance = Lists.newArrayList();
        }
    }

    public class DwarfData {

        public HashMap<UUID, List<Block>> stonewall;

        public DwarfData() {
            stonewall = Maps.newHashMap();
        }
    }

    public class HumanData {

        public List<UUID> blackForgedArrow;
        public List<UUID> cripplingShot;
        public List<UUID> quen;

        public HashMap<UUID, UUID> markedForDeath;

        public HumanData() {
            blackForgedArrow = Lists.newArrayList();
            cripplingShot = Lists.newArrayList();

            markedForDeath = Maps.newHashMap();
        }
    }

    public class VampireData {

        public List<UUID> vampirismAbility;

        public HashMap<UUID, UUID> infectedMap;

        public VampireData() {
            vampirismAbility = Lists.newArrayList();

            infectedMap = Maps.newHashMap();
        }
    }

    public class VoidwalkerData {

        public List<UUID> activePhaseAbility;

        public HashMap<UUID, List<UUID>> endermanMap;
        public HashMap<World, Integer> rainMap;
        public HashMap<UUID, Integer> voidTouchedMap;
        public HashMap<UUID, Integer> waterMap;

        public VoidwalkerData() {
            activePhaseAbility = Lists.newArrayList();

            endermanMap = Maps.newHashMap();
            rainMap = Maps.newHashMap();
            voidTouchedMap = Maps.newHashMap();
            waterMap = Maps.newHashMap();
        }
    }
}