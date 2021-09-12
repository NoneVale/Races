package net.nighthawkempires.races.listeners.races;

import net.nighthawkempires.races.recipes.VampireRecipes;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class VampireListener implements Listener {

    @EventHandler
    public void onPrepare(PrepareItemCraftEvent event) {
        if (event.getRecipe() != null && event.getRecipe().getResult().isSimilar(VampireRecipes.itemElixirOfLifeVampire())) {
            //event.getInventory().setResult(null);
            if (event.getInventory().getMatrix()[4].getType() == Material.POTION) {
                ItemStack raceItem = event.getInventory().getMatrix()[1];
                ItemStack potion = event.getInventory().getMatrix()[4];

                if (potion.getItemMeta() instanceof PotionMeta) {
                    PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                    PotionData potionData = potionMeta.getBasePotionData();

                    if (potionData.getType() == PotionType.REGEN && potionData.isUpgraded()) {
                        //event.getInventory().setResult(VampireRecipes.itemElixirOfLifeVampire());
                    }
                }
            }
        }
    }
}