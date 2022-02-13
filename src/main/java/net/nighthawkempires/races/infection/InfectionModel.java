package net.nighthawkempires.races.infection;

import com.google.common.collect.Maps;
import net.nighthawkempires.core.datasection.DataSection;
import net.nighthawkempires.core.datasection.Model;
import net.nighthawkempires.races.RacesPlugin;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InfectionModel implements Model {

    private HashMap<UUID, ActiveInfection> activeInfections;
    private HashMap<UUID, InactiveInfection> inactiveInfections;

    public InfectionModel() {
        this.activeInfections = Maps.newHashMap();
        this.inactiveInfections = Maps.newHashMap();
    }

    public InfectionModel(DataSection data) {

    }

    public boolean hasActiveInfection(UUID uuid) {
        if (!activeInfections.containsKey(uuid) && !inactiveInfections.containsKey(uuid)) return false;
        if (inactiveInfections.containsKey(uuid)) {
            if (Bukkit.getPlayer(uuid) != null) {
                ActiveInfection activeInfection = inactiveInfections.get(uuid).toActive();

                activeInfection.setTaskId(Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {

                }, activeInfection.secondsLeft() * 20L));
            }
        }

        return false;
    }

    public void deactivateInfection(UUID uuid) {

    }

    public String getKey() {
        return "infections";
    }

    public Map<String, Object> serialize() {
        return null;
    }
}
