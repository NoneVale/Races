package net.nighthawkempires.races.ability.human;

import net.nighthawkempires.core.util.PotionUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;

public class MasterAlchemistAbility implements Ability {

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
        return 1;
    }

    public Material getDisplayItem() {
        return Material.POTION;
    }

    public RaceType getRaceType() {
        return RaceType.HUMAN;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.HUMAN, 3);
    }

    public String getName() {
        return "Master Alchemist";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Increase duration by 35%, and double", "potion strength."};
            case 3 -> new String[] {"Increase duration by 50%. Now works", "with splash and lingering potions."};
            default -> new String[] {"Masters of Alchemy, Witchers are able", "to increase the effects of consumed", "potions.", "", "Increase duration by 20%."};
        };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof EntityPotionEffectEvent) {
            EntityPotionEffectEvent event = (EntityPotionEffectEvent) e;
            if (event.getCause() == EntityPotionEffectEvent.Cause.POTION_DRINK ||
                    event.getCause() == EntityPotionEffectEvent.Cause.POTION_SPLASH ||
                    event.getCause() == EntityPotionEffectEvent.Cause.AREA_EFFECT_CLOUD) {
                if (event.getEntity() instanceof Player) {
                    Player player = (Player) event.getEntity();
                    UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                    if (userModel.hasAbility(this)) {
                        event.setCancelled(true);
                        int level = userModel.getLevel(this);

                        int duration = event.getModifiedType().isInstant() ? 0 : event.getNewEffect().getDuration();
                        int amplifer = event.getNewEffect().getAmplifier();

                        if (!PotionUtil.isNegativeEffect(event.getModifiedType())) {
                            switch (level) {
                                case 2:
                                    duration = Double.valueOf((duration * .35) + duration).intValue();
                                    break;
                                case 3:
                                    duration = Double.valueOf((duration * .50) + duration).intValue();
                                    amplifer++;
                                    break;
                                default:
                                    duration = Double.valueOf((duration * .20) + duration).intValue();
                                    break;
                            }
                        }

                        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                            if (potionEffect.getType() == event.getModifiedType()) player.removePotionEffect(event.getModifiedType());
                        }
                        player.addPotionEffect(new PotionEffect(event.getModifiedType(), duration, amplifer));
                    }
                }
            }
        }
    }

    public int getId() {
        return 37;
    }

    public int getDuration(int level) {
        return 0;
    }
}
