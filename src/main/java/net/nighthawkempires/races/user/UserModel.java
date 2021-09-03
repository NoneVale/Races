package net.nighthawkempires.races.user;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.nighthawkempires.core.datasection.DataSection;
import net.nighthawkempires.core.datasection.Model;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.ability.AbilityManager;
import net.nighthawkempires.races.races.Race;
import org.apache.commons.lang.math.NumberUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class UserModel implements Model {

    private String key;
    private Race race;
    private int perkPoints;
    private HashMap<Ability, Integer> abilities;

    public UserModel(UUID uuid) {
        this.key = uuid.toString();
        this.race = RacesPlugin.getRaceManager().getDefaultRace();
        this.perkPoints = 0;
        this.abilities = Maps.newHashMap();
    }

    public UserModel(String key, DataSection data) {
        this.key = key;
        this.race = RacesPlugin.getRaceManager().getRace(data.getString("race"));
        this.perkPoints = data.getInt("perk_points");

        this.abilities = Maps.newHashMap();
        if (data.isSet("abilities")) {
            DataSection abilitySection = data.getSectionNullable("abilities");
            for (String s : abilitySection.keySet()) {
                if (NumberUtils.isNumber(s)) {
                    int id = Integer.parseInt(s);
                    int level = abilitySection.getInt(s);
                    this.abilities.put(RacesPlugin.getAbilityManager().getAbility(id), level);
                }
            }
        }
    }

    public Race getRace() {
        return this.race;
    }

    public void setRace(Race race) {
        if (race.getRaceType() != this.race.getRaceType()) {
            clearAbilities();
        }

        this.race = race;
        RacesPlugin.getUserRegistry().register(this);
    }

    public int getPerkPoints() {
        return this.perkPoints;
    }

    public void setPerkPoints(int perkPoints) {
        this.perkPoints = perkPoints;
        RacesPlugin.getUserRegistry().register(this);
    }

    public void addPerkPoints(int perkPoints) {
        this.setPerkPoints(this.getPerkPoints() + perkPoints);
    }

    public void removePerkPoints(int perkPoints) {
        this.setPerkPoints(this.getPerkPoints() - perkPoints);
    }

    public ImmutableList<Ability> getAbilities() {
        List<Ability> abilities = Lists.newArrayList();
        abilities.addAll(this.abilities.keySet());
        return ImmutableList.copyOf(abilities);
    }

    public void setAbilities(HashMap<Ability, Integer> abilities) {
        this.abilities = abilities;
        RacesPlugin.getUserRegistry().register(this);
    }

    public void addAbility(Ability ability, int level) {
        this.abilities.put(ability, level);
        RacesPlugin.getUserRegistry().register(this);
    }

    public boolean hasAbility(Ability ability) {
        return getAbilities().contains(ability);
    }

    public int getLevel(Ability ability) {
        if (!hasAbility(ability)) return 0;

        return this.abilities.get(ability);
    }

    public void clearAbilities() {
        this.abilities = Maps.newHashMap();
        RacesPlugin.getUserRegistry().register(this);
    }

    public String getKey() {
        return this.key;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = Maps.newHashMap();

        map.put("race", this.race.getName());
        map.put("perk_points", this.perkPoints);

        Map<String, Object> abilityMap = Maps.newHashMap();
        for (Ability ability : this.abilities.keySet()) {
            abilityMap.put(String.valueOf(ability.getId()), this.abilities.get(ability));
        }
        map.put("abilities", abilityMap);

        return map;
    }
}
