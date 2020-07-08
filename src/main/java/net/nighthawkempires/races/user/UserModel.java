package net.nighthawkempires.races.user;

import com.google.common.collect.Maps;
import net.nighthawkempires.core.datasection.DataSection;
import net.nighthawkempires.core.datasection.Model;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.races.Race;

import java.util.Map;
import java.util.UUID;

public class UserModel implements Model {

    private String key;
    private Race race;
    private int perkPoints;

    public UserModel(UUID uuid) {
        this.key = uuid.toString();
        this.race = RacesPlugin.getRaceManager().getDefaultRace();
        this.perkPoints = 0;
    }

    public UserModel(String key, DataSection data) {
        this.key = key;
        this.race = RacesPlugin.getRaceManager().getRace(data.getString("race"));
        this.perkPoints = data.getInt("perk_points");
    }

    public Race getRace() {
        return this.race;
    }

    public void setRace(Race race) {
        this.race = race;
        // RacesPlugin.getUserRegistry().register(this);
    }

    public int getPerkPoints() {
        return this.perkPoints;
    }

    public void setPerkPoints(int perkPoints) {
        this.perkPoints = perkPoints;
        // RacesPlugin.getUserRegistry().register(this);
    }

    public void addPerkPoints(int perkPoints) {
        this.setPerkPoints(this.getPerkPoints() + perkPoints);
    }

    public void removePerkPoints(int perkPoints) {
        this.setPerkPoints(this.getPerkPoints() - perkPoints);
    }

    public String getKey() {
        return this.key;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = Maps.newHashMap();

        map.put("race", this.race.getName());
        map.put("perk_points", this.perkPoints);
        return null;
    }
}
