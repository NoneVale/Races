package net.nighthawkempires.races.inventory;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.util.ItemUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.recipes.*;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;
import java.util.Map;

import static org.bukkit.ChatColor.RED;

public class RecipeInventory {

    private Inventory getInventory(Player player, ItemStack itemStack) {
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());
        RaceType raceType = userModel.getRace().getRaceType();
        List<Ability> abilities = Lists.newArrayList();

        Inventory inventory = Bukkit.createInventory(null, 45, "Recipe");

        // 10 11 12
        // 19 20 21
        // 28 29 30

        Recipe recipe;
        if (AngelRecipes.isTearOfGod(itemStack)) {
            recipe = Bukkit.getRecipe(RacesPlugin.TEAR_OF_GOD_RECIPE);
        } else if (DemonRecipes.isInfernalHeart(itemStack)) {
            recipe = Bukkit.getRecipe(RacesPlugin.INFERNAL_HEART_RECIPE);
        } else if (DwarfRecipes.isMinersTrophy(itemStack)) {
            recipe = Bukkit.getRecipe(RacesPlugin.MINERS_TROPHY_RECIPE);
        } else if (HumanRecipes.isElixirOfLife(itemStack)) {
            recipe = Bukkit.getRecipe(RacesPlugin.ELIXIR_OF_LIFE_RECIPE);
        } else if (VoidwalkerRecipes.isVoidForgedPendant(itemStack)) {
            recipe = Bukkit.getRecipe(RacesPlugin.VOID_FORGED_PENDANT_RECIPE);
        } else {
            recipe = null;
        }

        if (recipe != null) {
            if (recipe instanceof ShapedRecipe shapedRecipe) {
                Map<Character, RecipeChoice> choices = shapedRecipe.getChoiceMap();
                int pos = 10;
                for (String s : shapedRecipe.getShape()) {
                    for (char c : s.toCharArray()) {
                        ItemStack finalItem = choices.get(c).getItemStack().clone();

                        if (finalItem.getType() == Material.POTION) {
                            PotionMeta potionMeta = (PotionMeta) finalItem.getItemMeta();
                            potionMeta.setBasePotionData(new PotionData(PotionType.WATER));
                            finalItem.setItemMeta(potionMeta);
                        }

                        inventory.setItem(pos, finalItem);
                        pos++;
                    }
                    pos += 6;
                }
            } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                int pos = 10;
                int row = 0;
                for (ItemStack item : shapelessRecipe.getIngredientList()) {
                    if (row == 3) {
                        row = 0;
                        pos += 6;
                    }

                    ItemStack finalItem = item.clone();

                    if (finalItem.hasItemMeta() && finalItem.getItemMeta() instanceof PotionMeta potionMeta) {
                        potionMeta.setBasePotionData(new PotionData(PotionType.WATER));
                        finalItem.setItemMeta(potionMeta);
                    }

                    inventory.setItem(pos, finalItem);
                    row++;
                    pos++;
                }
            }
        }

        inventory.setItem(36, ItemUtil.createSkull(RED + "Previous Page", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RjOWU0ZGNmYTQyMjFhMWZhZGMxYjViMmIxMWQ4YmVlYjU3ODc5YWYxYzQyMzYyMTQyYmFlMWVkZDUifX19"));

        inventory.setItem(24, itemStack);

        return inventory;
    }

    public void open(Player player, ItemStack itemStack, RaceType raceType) {
        Inventory inventory = getInventory(player, itemStack);
        RacesPlugin.getInventoryData().recipeMap.put(inventory, raceType);
        player.openInventory(inventory);
    }
}
