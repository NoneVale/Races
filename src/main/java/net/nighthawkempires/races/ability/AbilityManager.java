package net.nighthawkempires.races.ability;

import com.google.common.collect.Lists;

import java.util.List;

public class AbilityManager {

    private List<Ability> abilities;

    public AbilityManager() {
        abilities = Lists.newArrayList();

        abilities.add(new BlankAbility());
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public Ability getAbility(int id) {
        for (Ability ability : abilities) {
            if (ability.getId() == id) {
                return ability;
            }
        }
        return null;
    }

    public Ability getAbility(String name) {
        for (Ability ability : abilities) {
            if (ability.getName().toLowerCase().equals(name.toLowerCase())) {
                return ability;
            }
        }
        return null;
    }
}
