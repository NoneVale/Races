package net.nighthawkempires.races.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.nighthawkempires.races.inventory.PerksInventory;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class InventoryData {

    public List<Inventory> inventoryList;
    public List<Inventory> perksInventoryList;

    public List<UUID> perkResetList;

    public InventoryData() {
        this.inventoryList = Lists.newArrayList();
        this.perksInventoryList = Lists.newArrayList();

        this.perkResetList = Lists.newArrayList();
    }
}
