package net.nighthawkempires.races.races;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;

import java.util.List;

public interface Race {

    String getName();

    RaceType getRaceType();

    int getTier();

    String[] getDescription();

    default List<Ability> getAbilities() {
        List<Ability> abilities = Lists.newArrayList();
        for (Ability ability : RacesPlugin.getAbilityManager().getAbilities()) {
            if (ability.getRace() == this) {
                abilities.add(ability);
            }
        }
        return abilities;
    }

    default String getDescriptionString() {
        StringBuilder description = new StringBuilder();
        for (int i = 0; i < getDescription().length; i++) {
            description.append(getDescription()[i]);

            if (i < getDescription().length - 1) {
                description.append("\n");
            }
        }
        return description.toString();
    }
}