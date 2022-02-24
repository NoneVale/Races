package net.nighthawkempires.races.enchantment;

import com.google.common.collect.Sets;
import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import net.nighthawkempires.core.enchantment.CustomEnchantmentWrapper;
import net.nighthawkempires.races.RacesPlugin;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BlankPotionEnchantment extends CustomEnchantmentWrapper {

    public BlankPotionEnchantment() {
        super(RacesPlugin.getPlugin(), "blank_potion");
    }

    @NotNull
    public String getName() {
        return "Blank Potion";
    }

    public int getMaxLevel() {
        return 1;
    }

    public int getStartLevel() {
        return 1;
    }

    @NotNull
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    public boolean isTreasure() {
        return false;
    }

    public boolean isCursed() {
        return false;
    }

    public boolean conflictsWith(@NotNull Enchantment enchantment) {
        return false;
    }

    public boolean canEnchantItem(@NotNull ItemStack itemStack) {
        return false;
    }

    public @NotNull Component displayName(int level) {
        return Component.text("");
    }

    public boolean isTradeable() {
        return false;
    }

    public boolean isDiscoverable() {
        return false;
    }

    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.VERY_RARE;
    }

    public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory) {
        return 0;
    }

    public Set<EquipmentSlot> getActiveSlots() {
        return Sets.newHashSet(EquipmentSlot.values());
    }

    public String translationKey() {
        return "";
    }
}
