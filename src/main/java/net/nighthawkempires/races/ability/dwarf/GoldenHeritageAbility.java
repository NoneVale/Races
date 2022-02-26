package net.nighthawkempires.races.ability.dwarf;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import net.nighthawkempires.core.util.ItemUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.event.AbilitiesResetEvent;
import net.nighthawkempires.races.event.AbilityUnlockEvent;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

public class GoldenHeritageAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.ACTIVE;
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
        return switch (level) {
            case 2 -> new String[] {"Golden armor has the durability of", "diamond armor, and the protection of", "iron armor."};
            case 3 -> new String[] {"Golden armor has the protection of", "diamond armor."};
            default -> new String[] {"Golden armor has increased durability", "for dwarves.", "", "Golden armor has the durability of", "iron armor."};
        };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof PlayerArmorChangeEvent event) {
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ARMOR);
            if (attribute == null) {
                player.registerAttribute(Attribute.GENERIC_ARMOR);
                attribute = player.getAttribute(Attribute.GENERIC_ARMOR);
            }

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
                                        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(attribute.getBaseValue() + 0);
                                        player.sendMessage(attribute.getBaseValue() + "");
                                        player.saveData();
                                        itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                                String.valueOf(Double.valueOf(itemMeta.getDamage() * 4.72).intValue()));
                                    }
                                    case 3 -> {
                                        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(attribute.getBaseValue() + 1);
                                        player.sendMessage(attribute.getBaseValue() + "");
                                        player.saveData();
                                        //itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, ArmorAttributes.GOLDEN_HELMET_DIAMOND.getModifier());
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
                                        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(attribute.getBaseValue() + 1);
                                        player.sendMessage(attribute.getBaseValue() + "");
                                        player.saveData();
                                        //itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, ArmorAttributes.GOLDEN_CHESTPLATE_IRON.getModifier());
                                        //itemMeta.getAttributeModifiers(Attribute.GENERIC_ARMOR).add(ArmorAttributes.GOLDEN_CHESTPLATE_IRON.getModifier());
                                        itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                                String.valueOf(Double.valueOf(itemMeta.getDamage() * 4.72).intValue()));
                                    }
                                    case 3 -> {
                                        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(attribute.getBaseValue() + 3);
                                        player.sendMessage(attribute.getBaseValue() + "");
                                        player.saveData();
                                        //itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, ArmorAttributes.GOLDEN_CHESTPLATE_DIAMOND.getModifier());
                                        //itemMeta.getAttributeModifiers(Attribute.GENERIC_ARMOR).add(ArmorAttributes.GOLDEN_CHESTPLATE_DIAMOND.getModifier());
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
                                        //itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, ArmorAttributes.GOLDEN_LEGGINGS_IRON.getModifier());
                                        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(attribute.getBaseValue() + 2);
                                        player.sendMessage(attribute.getBaseValue() + "");
                                        player.saveData();
                                        //itemMeta.getAttributeModifiers(Attribute.GENERIC_ARMOR).add(ArmorAttributes.GOLDEN_LEGGINGS_IRON.getModifier());
                                        itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                                String.valueOf(Double.valueOf(itemMeta.getDamage() * 4.72).intValue()));
                                    }
                                    case 3 -> {
                                        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(attribute.getBaseValue() + 3);
                                        player.sendMessage(attribute.getBaseValue() + "");
                                        player.saveData();
                                        //itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, ArmorAttributes.GOLDEN_LEGGINGS_DIAMOND.getModifier());
                                        //itemMeta.getAttributeModifiers(Attribute.GENERIC_ARMOR).add(ArmorAttributes.GOLDEN_LEGGINGS_DIAMOND.getModifier());
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
                                        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(attribute.getBaseValue() + 1);
                                        player.sendMessage(attribute.getBaseValue() + "");
                                        player.saveData();
                                        //itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, ArmorAttributes.GOLDEN_BOOTS_IRON.getModifier());
                                        //itemMeta.getAttributeModifiers(Attribute.GENERIC_ARMOR).add(ArmorAttributes.GOLDEN_BOOTS_IRON.getModifier());
                                        itemMeta.getPersistentDataContainer().set(RacesPlugin.GOLDEN_HERITAGE, PersistentDataType.STRING,
                                                String.valueOf(Double.valueOf(itemMeta.getDamage() * 4.72).intValue()));
                                    }
                                    case 3 -> {
                                        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(attribute.getBaseValue() + 2);
                                        player.sendMessage(attribute.getBaseValue() + "");
                                        player.saveData();
                                        //itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, ArmorAttributes.GOLDEN_BOOTS_DIAMOND.getModifier());
                                        //itemMeta.getAttributeModifiers(Attribute.GENERIC_ARMOR).add(ArmorAttributes.GOLDEN_BOOTS_DIAMOND.getModifier());
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

                        player.saveData();
                    }
                    //Equipping New Armor
                }

                if (event.getOldItem() != null) {
                    ItemStack equipped = event.getOldItem();
                    if (equipped.getItemMeta() instanceof Damageable itemMeta) {
                        switch (equipped.getType()) {
                            case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS -> {
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

                        switch (equipped.getType()) {
                            case GOLDEN_HELMET -> {
                                switch (level) {
                                    case 2 -> attribute.setBaseValue(attribute.getBaseValue() - 0);
                                    case 3 -> attribute.setBaseValue(attribute.getBaseValue() - 1);
                                }
                            }
                            case GOLDEN_CHESTPLATE -> {
                                switch (level) {
                                    case 2 -> attribute.setBaseValue(attribute.getBaseValue() - 1);
                                    case 3 -> attribute.setBaseValue(attribute.getBaseValue() - 3);
                                }
                            }
                            case GOLDEN_LEGGINGS -> {
                                switch (level) {
                                    case 2 -> attribute.setBaseValue(attribute.getBaseValue() - 2);
                                    case 3 -> attribute.setBaseValue(attribute.getBaseValue() - 3);
                                }
                            }
                            case GOLDEN_BOOTS -> {
                                switch (level) {
                                    case 2 -> attribute.setBaseValue(attribute.getBaseValue() - 1);
                                    case 3 -> attribute.setBaseValue(attribute.getBaseValue() - 2);
                                }
                            }
                        }

                        player.saveData();
                    }
                }
            }

            // Check if all armor is off
            PlayerInventory inventory = player.getInventory();
            boolean hasArmor = false;
            for (ItemStack itemStack : inventory.getArmorContents()) {
                if (!ItemUtil.isEmpty(itemStack)) {
                    hasArmor = true;
                    break;
                }
            }

            if (!hasArmor) {
                player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0.0);
            }

            if (attribute.getBaseValue() > 20) {
                attribute.setBaseValue(20);
            }

            if (attribute.getBaseValue() < 0) {
                attribute.setBaseValue(0);
            }

            player.saveData();
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
        } else if (e instanceof AbilityUnlockEvent event) {
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ARMOR);
            if (attribute == null) {
                player.registerAttribute(Attribute.GENERIC_ARMOR);
                attribute = player.getAttribute(Attribute.GENERIC_ARMOR);
            }

            if (event.getAbility().getId() == this.getId()) {
                if (userModel.hasAbility(this)) {
                    int level = userModel.getLevel(this);

                    player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0.0);

                    PlayerInventory inventory = player.getInventory();
                    boolean hasArmor = false;
                    for (ItemStack itemStack : inventory.getArmorContents()) {
                        if (!ItemUtil.isEmpty(itemStack)) {
                            switch (itemStack.getType()) {
                                case GOLDEN_HELMET -> {
                                    switch (level) {
                                        case 2 -> attribute.setBaseValue(attribute.getBaseValue() + 0);
                                        case 3 -> attribute.setBaseValue(attribute.getBaseValue() + 1);
                                    }
                                }
                                case GOLDEN_CHESTPLATE -> {
                                    switch (level) {
                                        case 2 -> attribute.setBaseValue(attribute.getBaseValue() + 1);
                                        case 3 -> attribute.setBaseValue(attribute.getBaseValue() + 3);
                                    }
                                }
                                case GOLDEN_LEGGINGS -> {
                                    switch (level) {
                                        case 2 -> attribute.setBaseValue(attribute.getBaseValue() + 2);
                                        case 3 -> attribute.setBaseValue(attribute.getBaseValue() + 3);
                                    }
                                }
                                case GOLDEN_BOOTS -> {
                                    switch (level) {
                                        case 2 -> attribute.setBaseValue(attribute.getBaseValue() + 1);
                                        case 3 -> attribute.setBaseValue(attribute.getBaseValue() + 2);
                                    }
                                }
                            }
                        }
                    }

                    player.saveData();
                }
            }
        } else if (e instanceof AbilitiesResetEvent event) {
            Player player = event.getPlayer();
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_ARMOR);
            if (attribute == null) {
                player.registerAttribute(Attribute.GENERIC_ARMOR);
                attribute = player.getAttribute(Attribute.GENERIC_ARMOR);
            }

            for (Ability ability : event.getAbilities()) {
                if (ability.getId() == this.getId()) {
                    player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0.0);
                    player.saveData();
                }
            }
        }
    }

    public int getId() {
        return 25;
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
