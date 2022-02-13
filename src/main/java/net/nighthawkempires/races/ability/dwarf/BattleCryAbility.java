package net.nighthawkempires.races.ability.dwarf;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class BattleCryAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return (level == 5 ? 60 : 90) + getDuration(level);
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCost(int level) {
        return switch (level) {
            case 4, 5 -> 2;
            default -> 1;
        };
    }

    public Material getDisplayItem() {
        return Material.BELL;
    }

    public RaceType getRaceType() {
        return RaceType.DWARF;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DWARF, 1);
    }

    public String getName() {
        return "Battle Cry";
    }

    public String[] getDescription(int level) {
        return new String[0];
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof PlayerInteractEvent event) {
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                int level = userModel.getLevel(this);
                if (CorePlugin.getCooldowns().hasActiveCooldown(player.getUniqueId(),
                        this.getClass().getSimpleName().toLowerCase())) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "There is another "
                            + CorePlugin.getCooldowns().getActive(player.getUniqueId(), this.getClass().getSimpleName().toLowerCase()).timeLeft()
                            + " before you can use this ability again."));
                    return;
                }

                switch (level) {
                    case 2 -> player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
                    case 3 -> {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 600, 0));
                    }
                    default -> player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 0));
                }

                for (Entity entity : player.getNearbyEntities(3, 3, 3)) {
                    if (entity instanceof LivingEntity livingEntity) {
                        if (livingEntity instanceof Player target) {
                            // Check if target player is immune to magic, if so return
                            // return;
                        }

                        Vector center = player.getLocation().toVector();
                        center.subtract(livingEntity.getLocation().toVector());
                        livingEntity.setVelocity(center.normalize().multiply(2.5).setY(0.8));

                        if (level >= 4) {
                            int chance = level == 4 ? 15 : 30;

                            int random = Double.valueOf(Math.random() * 100).intValue();
                            if (random <= chance) {
                                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
                                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 1));
                                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                            }
                        }
                    }
                }

                CorePlugin.getCooldowns().addCooldown(new Cooldown(player.getUniqueId(),
                        this.getClass().getSimpleName().toLowerCase(),
                        (System.currentTimeMillis() + (getCooldown(level) * 1000L))));
            }
        }
    }

    public int getId() {
        return 13;
    }

    public int getDuration(int level) {
        return 0;
    }
}
