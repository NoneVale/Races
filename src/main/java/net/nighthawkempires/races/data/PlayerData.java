package net.nighthawkempires.races.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.nighthawkempires.races.ability.Ability;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

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

    public HashMap<UUID, HashMap<Ability, Integer>> passiveTasks;

    public PlayerData() {
        angel = new AngelData();
        demon = new DemonData();
        dwarf = new DwarfData();
        human = new HumanData();
        vampire = new VampireData();
        voidwalker = new VoidwalkerData();

        passiveTasks = Maps.newHashMap();
    }

    public int getTaskId(Player player, Ability ability) {
        if (!passiveTasks.containsKey(player.getUniqueId())) return -1;

        HashMap<Ability, Integer> tasks = passiveTasks.get(player.getUniqueId());
        if (!tasks.containsKey(ability)) return -1;

        return tasks.get(ability);
    }

    public void setTaskId(Player player, Ability ability, int taskId) {
        HashMap<Ability, Integer> tasks = passiveTasks.containsKey(player.getUniqueId()) ? passiveTasks.get(player.getUniqueId()) : Maps.newHashMap();

        if (taskId == -1) {
            tasks.remove(ability);
        } else {
            tasks.put(ability, taskId);
        }

        passiveTasks.put(player.getUniqueId(), tasks);
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

        public HashMap<UUID, List<UUID>> guardians;
        public HashMap<UUID, List<UUID>> possessed;

        // UNUSED
        public List<UUID> arcaneResistance;


        public DemonData() {
            fireball = Lists.newArrayList();
            fireCloaked = Lists.newArrayList();
            syphon = Lists.newArrayList();
            syphoned = Lists.newArrayList();

            guardians = Maps.newHashMap();
            possessed = Maps.newHashMap();

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
        public List<UUID> displacement;

        public HashMap<UUID, List<UUID>> endermen;
        public HashMap<UUID, Integer> endershield;
        public HashMap<UUID, List<UUID>> pearlminion;
        public HashMap<UUID, PearlMode> pearlmode;
        public HashMap<World, Integer> rainMap;
        public HashMap<UUID, Integer> voidTouchedMap;
        public HashMap<UUID, Integer> waterMap;

        public VoidwalkerData() {
            activePhaseAbility = Lists.newArrayList();
            displacement = Lists.newArrayList();

            endermen = Maps.newHashMap();
            endershield = Maps.newHashMap();
            pearlminion = Maps.newHashMap();
            pearlmode = Maps.newHashMap();
            rainMap = Maps.newHashMap();
            voidTouchedMap = Maps.newHashMap();
            waterMap = Maps.newHashMap();
        }

        public void addPearl(Player player) {
            int pearls = getPearls(player) + 1;
            endershield.put(player.getUniqueId(), pearls);
        }

        public int getPearls(Player player) {
            if (endershield.containsKey(player.getUniqueId())) {
                return endershield.get(player.getUniqueId());
            }

            return 0;
        }

        public void removePearl(Player player) {
            if (getPearls(player) > 1) {
                int amount = getPearls(player) - 1;
                endershield.put(player.getUniqueId(), amount);
            }

            endershield.remove(player.getUniqueId());
        }

        public enum PearlMode {
            NORMAL(0, 0), VACUUM(1, 1), MINION(2, 3), EXPLODE(3, 5);

            private int id;
            private int levelReq;

            PearlMode(int id, int levelReq) {
                this.id = id;
                this.levelReq = levelReq;
            }

            public int getId() {
                return id;
            }

            public int getLevelReq() {
                return levelReq;
            }

            public PearlMode nextMode(int playerLevel) {
                int next = id + 1;
                if (next > 3) next = 0;

                for (PearlMode pearlMode : values()) {
                    if (next == pearlMode.getId()) {
                        if (playerLevel < pearlMode.getLevelReq()) {
                            return pearlMode.nextMode(playerLevel);
                        } else {
                            return pearlMode;
                        }
                    }
                }

                return NORMAL;
            }
        }
    }
}