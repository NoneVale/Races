package net.nighthawkempires.races.binding;

import com.google.common.collect.Lists;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class BindingManager {

    public List<Ability> getBindings(ItemStack itemStack) {
        List<Ability> abilities = Lists.newArrayList();

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.getPersistentDataContainer().has(RacesPlugin.BINDINGS_KEY, PersistentDataType.STRING)) {
            String data = itemMeta.getPersistentDataContainer().get(RacesPlugin.BINDINGS_KEY, PersistentDataType.STRING);

            String[] list = data.split(",");
            for (int i = 0; i < list.length; i++) {
                if (NumberUtils.isNumber(list[i])) {
                    int id = Integer.parseInt(list[i]);

                    Ability ability = RacesPlugin.getAbilityManager().getAbility(id);

                    if (ability != null) abilities.add(ability);
                }
            }
        }

        return abilities;
    }

    public ItemStack addBinding(ItemStack itemStack, Ability ability) {
        List<Ability> abilities = getBindings(itemStack);

        abilities.add(ability);

        ItemMeta itemMeta = itemStack.getItemMeta();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < abilities.size(); i++) {
            stringBuilder.append(abilities.get(i).getId());

            if (i < abilities.size() - 1) {
                stringBuilder.append(",");
            }
        }

        itemMeta.getPersistentDataContainer().set(RacesPlugin.BINDINGS_KEY, PersistentDataType.STRING, stringBuilder.toString());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack removeBinding(ItemStack itemStack, Ability ability) {
        List<Ability> abilities = getBindings(itemStack);

        abilities.remove(ability);

        ItemMeta itemMeta = itemStack.getItemMeta();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < abilities.size(); i++) {
            stringBuilder.append(abilities.get(i).getId());

            if (i < abilities.size() - 1) {
                stringBuilder.append(",");
            }
        }

        itemMeta.getPersistentDataContainer().set(RacesPlugin.BINDINGS_KEY, PersistentDataType.STRING, stringBuilder.toString());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack clearBindings(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.getPersistentDataContainer().has(RacesPlugin.BINDINGS_KEY, PersistentDataType.STRING)) {
            itemMeta.getPersistentDataContainer().remove(RacesPlugin.BINDINGS_KEY);

            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        return itemStack;
    }

    public Ability getCurrentAbility(ItemStack itemStack) {
        List<Ability> abilities = getBindings(itemStack);

        if (abilities.isEmpty()) {
            RacesPlugin.getAbilityManager().getAbility(0);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        int index = 0;
        if (itemMeta.getPersistentDataContainer().has(RacesPlugin.CURRENT_BINDING_KEY, PersistentDataType.INTEGER)) {
            index = itemMeta.getPersistentDataContainer().get(RacesPlugin.CURRENT_BINDING_KEY, PersistentDataType.INTEGER);
        }

        return abilities.get(index);
    }

    public ItemStack scrollNextAbility(ItemStack itemStack) {
        List<Ability> abilities = getBindings(itemStack);

        if (abilities.isEmpty()) {
            RacesPlugin.getAbilityManager().getAbility(0);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        int index = 0;
        if (itemMeta.getPersistentDataContainer().has(RacesPlugin.CURRENT_BINDING_KEY, PersistentDataType.INTEGER)) {
            index = itemMeta.getPersistentDataContainer().get(RacesPlugin.CURRENT_BINDING_KEY, PersistentDataType.INTEGER);
        }

        index++;
        if (index == abilities.size()) {
            index = 0;
        }

        itemMeta.getPersistentDataContainer().set(RacesPlugin.CURRENT_BINDING_KEY, PersistentDataType.INTEGER, index);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public ItemStack setBinder(ItemStack itemStack, UUID uuid) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.getPersistentDataContainer().set(RacesPlugin.BINDER_KEY, PersistentDataType.STRING, uuid.toString());
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public UUID getBinder(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.getPersistentDataContainer().has(RacesPlugin.BINDER_KEY, PersistentDataType.STRING)) {
            String data = itemMeta.getPersistentDataContainer().get(RacesPlugin.BINDER_KEY, PersistentDataType.STRING);

            return UUID.fromString(data);
        }

        return null;
    }

    public ItemStack clearBinder(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.getPersistentDataContainer().has(RacesPlugin.BINDER_KEY, PersistentDataType.STRING)) {
            itemMeta.getPersistentDataContainer().remove(RacesPlugin.BINDER_KEY);

            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        return itemStack;
    }
}
