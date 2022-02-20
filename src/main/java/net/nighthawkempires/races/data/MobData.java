package net.nighthawkempires.races.data;

import com.google.common.collect.Lists;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;

import java.util.List;
import java.util.UUID;

public class MobData {

    public List<UUID> pet;

    public MobData() {
        this.pet = Lists.newArrayList();
    }

    public void addPet(Entity entity) {
        this.pet.add(entity.getUniqueId());
    }

    public void removePet(Entity entity) {
        this.pet.remove(entity.getUniqueId());
    }

    public boolean isPet(Entity entity) {
        return this.pet.contains(entity.getUniqueId());
    }
}