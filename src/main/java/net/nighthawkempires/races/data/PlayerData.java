package net.nighthawkempires.races.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.Hash;
import net.nighthawkempires.races.inventory.PerksInventory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    public Voidwalker voidwalker;

    public PlayerData() {
        voidwalker = new Voidwalker();
    }

    public class Voidwalker {

        public List<UUID> activePhaseAbility;

        public HashMap<UUID, List<UUID>> endermanMap;
        public HashMap<World, Integer> rainMap;
        public HashMap<UUID, Integer> voidTouchedMap;
        public HashMap<UUID, Integer> waterMap;

        public Voidwalker() {
            activePhaseAbility = Lists.newArrayList();

            endermanMap = Maps.newHashMap();
            rainMap = Maps.newHashMap();
            voidTouchedMap = Maps.newHashMap();
            waterMap = Maps.newHashMap();
        }
    }
}