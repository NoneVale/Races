package net.nighthawkempires.races.ability.voidwalker;

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
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VoidFogAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.ACTIVE;
    }

    public int getCooldown(int level) {
        return 120 + getDuration(level);
    }

    public int getMaxLevel() {
        return 4;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return null;
    }

    public RaceType getRaceType() {
        return RaceType.VOIDWALKER;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(getRaceType(), 2);
    }

    public String getName() {
        return "Void Fog";
    }

    public String[] getDescription(int level) {
        return new String[] { "" };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof PlayerInteractEvent) {
            PlayerInteractEvent event = (PlayerInteractEvent) e;
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                if (CorePlugin.getCooldowns().hasActiveCooldown(player.getUniqueId(),
                        this.getClass().getSimpleName().toLowerCase())) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "There is another "
                            + CorePlugin.getCooldowns().getActive(player.getUniqueId(), this.getClass().getSimpleName().toLowerCase()).timeLeft()
                            + " before you can use this ability again."));
                    return;
                }

                int level = userModel.getLevel(this);
                double radius = getRadius(level);
                int duration = getDuration(level);

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have activated ability "
                        + getRaceType().getRaceColor() + this.getName() + ChatColor.GRAY + "."));
                for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
                    if (entity instanceof Player) {
                        Player target = (Player) entity;

                        if (target.getUniqueId() != player.getUniqueId()) {
                            target.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have been surrounded by a dense "
                                    + getRaceType().getRaceColor() + getName() + ChatColor.GRAY + "."));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration * 20, 0, false, false, false));
                        }
                    }
                }

                CorePlugin.getCooldowns().addCooldown(new Cooldown(player.getUniqueId(),
                        this.getClass().getSimpleName().toLowerCase(),
                        (System.currentTimeMillis() + (getCooldown(userModel.getLevel(this)) * 1000L))));
            }
        }
    }

    public int getId() {
        return 84;
    }

    public int getDuration(int level) {
        int duration = 0;
        switch (level) {
            case 1: duration = 5; break;
            case 2: duration = 6; break;
            case 3: duration = 7; break;
            case 4: duration = 8; break;
        }
        return duration;
    }

    private double getRadius(int level) {
        double radius = 0;
        switch (level) {
            case 1: radius = 5; break;
            case 2: radius = 7.5; break;
            case 3: radius = 10; break;
            case 4: radius = 12.5; break;
        }
        return radius;
    }
}