package net.nighthawkempires.races.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.nighthawkempires.races.inventory.RaceListInventory;
import net.nighthawkempires.races.races.RaceType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class InventoryData {

    public List<Inventory> perksInventoryList;
    public List<Inventory> raceGUIInventoryList;
    public List<Inventory> recipeList;

    public HashMap<Inventory, RaceListInventory.RaceListType> raceListTypeMap;
    public HashMap<Inventory, RaceType> recipeListMap;
    public HashMap<Inventory, RaceType> recipeMap;

    public List<UUID> perkResetList;

    public InventoryData() {
        this.perksInventoryList = Lists.newArrayList();
        this.raceGUIInventoryList = Lists.newArrayList();
        this.recipeList = Lists.newArrayList();

        this.raceListTypeMap = Maps.newHashMap();
        this.recipeListMap = Maps.newHashMap();
        this.recipeMap = Maps.newHashMap();

        this.perkResetList = Lists.newArrayList();
    }
}