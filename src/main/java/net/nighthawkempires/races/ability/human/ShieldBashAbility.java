package net.nighthawkempires.races.ability.human;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.guilds.util.AllyUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.ChatColor.RED;

public class ShieldBashAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.ACTIVE;
    }

    public int getCooldown(int level) {
        switch (level) {
            case 2: return 25;
            default: return 30;
        }
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        switch (level) {
            case 3: return 2;
            default: return 1;
        }
    }

    public Material getDisplayItem() {
        return Material.SHIELD;
    }

    public RaceType getRaceType() {
        return RaceType.HUMAN;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.HUMAN, 1);
    }

    public String getName() {
        return "Shield Bash";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Increase slowness to Slowness IV, and", "reduce cooldown to " + getCooldown(level) + "s."};
            case 3 -> new String[] {"Increase blindness duration to 4s, and", "deals 2 damage."};
            default -> new String[] {"Humans are capable of attacking foes", "with their shield in order to", "stun them.", "", "Deals Blindness 2s, Slowness II 1s", "and Nausea 5s"};
        };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof PlayerInteractEntityEvent event) {
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (event.getRightClicked() instanceof Player target) {
                if (AllyUtil.isAlly(player, target)) return;

                if (userModel.hasAbility(this)) {
                    if (checkCooldown(this, player, false)) return;

                    if (!canUseRaceAbility(player)) {
                        return;
                    } else if (isSyphoned(player)) {
                        return;
                    }

                    int level = userModel.getLevel(this);

                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have used Shield Bash on "
                            + ChatColor.GREEN + target.getName() + ChatColor.GRAY + "."));
                    player.getWorld().playSound(target.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, 0.4f);

                    switch (level) {
                        case 2 -> {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 3));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                        }
                        case 3 -> {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 3));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                            target.damage(2.0, player);
                        }
                        default -> {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 1));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                        }
                    }

                    addCooldown(this, player, level);
                }
            }
        }
    }

    public int getId() {
        return 31;
    }

    public int getDuration(int level) {
        return 0;
    }
}
