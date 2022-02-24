package net.nighthawkempires.races.ability;

import com.google.common.collect.Lists;
import net.nighthawkempires.races.ability.angel.*;
import net.nighthawkempires.races.ability.dwarf.*;
import net.nighthawkempires.races.ability.human.*;
import net.nighthawkempires.races.ability.demon.*;
import net.nighthawkempires.races.ability.voidwalker.*;

import java.util.List;

public class AbilityManager {

    private List<Ability> abilities;

    public AbilityManager() {
        abilities = Lists.newArrayList();

        abilities.add(new BlankAbility());

        // Angel
        abilities.add(new AngelicFaithAbility());
        abilities.add(new ConcentratedGroundAbility());
        abilities.add(new ShelteringWingsAbility());
        abilities.add(new HeavensGraceAbility());
        abilities.add(new LanceOfLonginusAbility());
        abilities.add(new HeavenlyBarrierAbility());
        abilities.add(new WhirlwindAbility());
        abilities.add(new AerialDashAbility());
        abilities.add(new PurgeAbility());

        // Demon
        abilities.add(new HellishBeingAbility());
        abilities.add(new FireCloakAbility());
        abilities.add(new FireballAbility());
        abilities.add(new HellishGuardiansAbility());
        abilities.add(new NoLongerAGuestAbility());
        abilities.add(new PossessionAbility());
        abilities.add(new HellRiftAbility());
        abilities.add(new SyphonAbility());
        abilities.add(new CorruptionAbility());

        // Dwarf
        abilities.add(new RockNStoneAbility());
        abilities.add(new AxeMasteryAbility());
        abilities.add(new BattleCryAbility());
        abilities.add(new DwarvenFuryAbility());
        abilities.add(new GoldenHeritageAbility());
        abilities.add(new StoneHeartAbility());
        abilities.add(new ShatteringForceAbility());
        abilities.add(new StonewallAbility());
        abilities.add(new MyPreciousAbility());

        // Human
        abilities.add(new ShieldBashAbility());
        abilities.add(new NoPlaceLikeHomeAbility());
        abilities.add(new PiercingEdgeAbility());
        abilities.add(new MarkedForDeathAbility());
        abilities.add(new BlackForgedArrowsAbility());
        abilities.add(new CripplingShotAbility());
        abilities.add(new MasterAlchemistAbility());
        abilities.add(new AardAbility());
        abilities.add(new QuenAbility());

        // Vampire

        // Voidwalker
        abilities.add(new ShadowAbility());
        abilities.add(new VoidTouchedAbility());
        abilities.add(new KnowledgeOfThePearlsAbility());
        abilities.add(new CallOfTheVoidAbility());
        abilities.add(new AbyssalDisplacementAbility());
        abilities.add(new EnderRightsAbility());
        abilities.add(new VoidBlastAbility());
        abilities.add(new EnderShieldAbility());
        abilities.add(new ReturnOfTheVoidAbility());
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
