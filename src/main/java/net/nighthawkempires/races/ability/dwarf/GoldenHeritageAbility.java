package net.nighthawkempires.races.ability.dwarf;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

public class GoldenHeritageAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.PASSIVE;
    }

    public int getCooldown(int level) {
        return 0;
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        return 2;
    }

    public Material getDisplayItem() {
        return Material.GOLDEN_CHESTPLATE;
    }

    public RaceType getRaceType() {
        return RaceType.DWARF;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DWARF, 2);
    }

    public String getName() {
        return "Golden Heritage";
    }

    public String[] getDescription(int level) {
        return new String[0];
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof PlayerArmorChangeEvent event) {
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                int level = userModel.getLevel(this);
                if (event.getNewItem() != null) {
                    ItemStack equipped = event.getNewItem();
                    ItemMeta meta = equipped.getItemMeta();
                    if (meta instanceof Damageable itemMeta) {

                        switch (equipped.getType()) {
                            case GOLDEN_HELMET -> {
                                switch (level) {
                                    case 2 -> {
                                        itemMeta.getAttributeModifiers(Attribute.GENERIC_ARMOR).add(ArmorAttributes.GOLDEN_HELMET_IRON.getModifier());
                                        itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                                String.valueOf(Double.valueOf(itemMeta.getDamage() * 4.72).intValue()));
                                    }
                                    case 3 -> {
                                        itemMeta.getAttributeModifiers(Attribute.GENERIC_ARMOR).add(ArmorAttributes.GOLDEN_HELMET_DIAMOND.getModifier());
                                        itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                                String.valueOf(Double.valueOf(itemMeta.getDamage() * 4.72).intValue()));
                                    }
                                    default -> itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                            String.valueOf(Double.valueOf(itemMeta.getDamage() * 2.15).intValue()));
                                }
                                equipped.setItemMeta(itemMeta);
                            }
                            case GOLDEN_CHESTPLATE -> {
                                switch (level) {
                                    case 2 -> {
                                        itemMeta.getAttributeModifiers(Attribute.GENERIC_ARMOR).add(ArmorAttributes.GOLDEN_CHESTPLATE_IRON.getModifier());
                                        itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                                String.valueOf(Double.valueOf(itemMeta.getDamage() * 4.72).intValue()));
                                    }
                                    case 3 -> {
                                        itemMeta.getAttributeModifiers(Attribute.GENERIC_ARMOR).add(ArmorAttributes.GOLDEN_CHESTPLATE_DIAMOND.getModifier());
                                        itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                                String.valueOf(Double.valueOf(itemMeta.getDamage() * 4.72).intValue()));
                                    }
                                    default -> itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                            String.valueOf(Double.valueOf(itemMeta.getDamage() * 2.15).intValue()));
                                }
                                equipped.setItemMeta(itemMeta);
                            }
                            case GOLDEN_LEGGINGS -> {
                                switch (level) {
                                    case 2 -> {
                                        itemMeta.getAttributeModifiers(Attribute.GENERIC_ARMOR).add(ArmorAttributes.GOLDEN_LEGGINGS_IRON.getModifier());
                                        itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                                String.valueOf(Double.valueOf(itemMeta.getDamage() * 4.72).intValue()));
                                    }
                                    case 3 -> {
                                        itemMeta.getAttributeModifiers(Attribute.GENERIC_ARMOR).add(ArmorAttributes.GOLDEN_LEGGINGS_DIAMOND.getModifier());
                                        itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                                String.valueOf(Double.valueOf(itemMeta.getDamage() * 4.72).intValue()));
                                    }
                                    default -> itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                            String.valueOf(Double.valueOf(itemMeta.getDamage() * 2.15).intValue()));
                                }
                                equipped.setItemMeta(itemMeta);
                            }
                            case GOLDEN_BOOTS -> {
                                switch (level) {
                                    case 2 -> {
                                        itemMeta.getAttributeModifiers(Attribute.GENERIC_ARMOR).add(ArmorAttributes.GOLDEN_BOOTS_IRON.getModifier());
                                        itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                                String.valueOf(Double.valueOf(itemMeta.getDamage() * 4.72).intValue()));
                                    }
                                    case 3 -> {
                                        itemMeta.getAttributeModifiers(Attribute.GENERIC_ARMOR).add(ArmorAttributes.GOLDEN_BOOTS_DIAMOND.getModifier());
                                        itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                                String.valueOf(Double.valueOf(itemMeta.getDamage() * 4.72).intValue()));
                                    }
                                    default -> itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                            String.valueOf(Double.valueOf(itemMeta.getDamage() * 2.15).intValue()));
                                }
                                equipped.setItemMeta(itemMeta);
                            }
                            default -> {
                            }
                        }
                    }
                    //Equipping New Armor
                }
                if (event.getOldItem() != null) {
                    ItemStack equipped = event.getNewItem();
                    if (equipped.getItemMeta() instanceof Damageable itemMeta) {

                        switch (equipped.getType()) {
                            case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS -> {
                                itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR);
                                if (itemMeta.getPersistentDataContainer().has(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING)) {
                                    String string = itemMeta.getPersistentDataContainer().get(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING);
                                    if (NumberUtils.isDigits(string)) {
                                        int damage = Integer.parseInt(string);

                                        if (level == 1) {
                                            itemMeta.setDamage(Double.valueOf(damage / 2.15).intValue());
                                        } else {
                                            itemMeta.setDamage(Double.valueOf(damage / 4.72).intValue());
                                        }
                                    }

                                    itemMeta.getPersistentDataContainer().remove(RacesPlugin.GOLDEN_HERITAGE);
                                }
                                equipped.setItemMeta(itemMeta);
                            }
                            default -> {}
                        }
                    }
                }
            }
        } else if (e instanceof PlayerItemDamageEvent event) {
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                int level = userModel.getLevel(this);
                ItemStack itemStack = event.getItem();

                if (itemStack.getItemMeta() instanceof Damageable itemMeta) {
                    switch (itemStack.getType()) {
                        case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS -> {
                            if (itemMeta.getDamage() >= itemStack.getType().getMaxDurability()) return;

                            event.setCancelled(true);

                            if (itemMeta.getPersistentDataContainer().has(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING)) {
                                String string = itemMeta.getPersistentDataContainer().get(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING);
                                if (NumberUtils.isDigits(string)) {
                                    int damage = Integer.parseInt(string);
                                    damage += event.getDamage();
                                    itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                            String.valueOf(damage));

                                    if (level == 1) {
                                        itemMeta.setDamage(Double.valueOf(damage / 2.15).intValue());
                                    } else {
                                        itemMeta.setDamage(Double.valueOf(damage / 4.72).intValue());
                                    }
                                }
                            }
                            itemStack.setItemMeta(itemMeta);
                        }
                        default -> {}
                    }
                }
            }
        }
    }

    public int getId() {
        return 15;
    }

    public int getDuration(int level) {
        return 0;
    }

    private enum ArmorAttributes {
        GOLDEN_HELMET_IRON(new AttributeModifier("golden-helmet-iron", 0, AttributeModifier.Operation.ADD_NUMBER)),
        GOLDEN_CHESTPLATE_IRON(new AttributeModifier("golden-chestplate-iron", 1, AttributeModifier.Operation.ADD_NUMBER)),
        GOLDEN_LEGGINGS_IRON(new AttributeModifier("golden-leggings-iron", 2, AttributeModifier.Operation.ADD_NUMBER)),
        GOLDEN_BOOTS_IRON(new AttributeModifier("golden-boots-iron", 1, AttributeModifier.Operation.ADD_NUMBER)),
        GOLDEN_HELMET_DIAMOND(new AttributeModifier("golden-helmet-diamond", 1, AttributeModifier.Operation.ADD_NUMBER)),
        GOLDEN_CHESTPLATE_DIAMOND(new AttributeModifier("golden-chestplate-diamond", 3, AttributeModifier.Operation.ADD_NUMBER)),
        GOLDEN_LEGGINGS_DIAMOND(new AttributeModifier("golden-leggings-diamond", 3, AttributeModifier.Operation.ADD_NUMBER)),
        GOLDEN_BOOTS_DIAMOND(new AttributeModifier("golden-boots-diamond", 2, AttributeModifier.Operation.ADD_NUMBER));

        private AttributeModifier modifier;

        ArmorAttributes(AttributeModifier modifier) {
            this.modifier = modifier;
        }

        public AttributeModifier getModifier() {
            return modifier;
        }
    }
}
