package net.nighthawkempires.races.ability.human;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
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

public class ShieldBashAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.PASSIVE;
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
        return new String[0];
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof PlayerInteractEntityEvent) {
            PlayerInteractEntityEvent event = (PlayerInteractEntityEvent) e;
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (event.getRightClicked() instanceof Player) {
                Player target = (Player) event.getRightClicked();

                if (userModel.hasAbility(this)) {
                    int level = userModel.getLevel(this);
                    if (CorePlugin.getCooldowns().hasActiveCooldown(player.getUniqueId(),
                            this.getClass().getSimpleName().toLowerCase())) {
                        player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "There is another "
                                + CorePlugin.getCooldowns().getActive(player.getUniqueId(), this.getClass().getSimpleName().toLowerCase()).timeLeft()
                                + " before you can use this ability again."));
                        return;
                    }

                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have used Shield Bash on "
                            + ChatColor.GREEN + target.getName() + ChatColor.GRAY + "."));
                    player.getWorld().playSound(target.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, 0.4f);

                    switch (level) {
                        case 2:
                            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 3));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                            break;
                        case 3:
                            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 3));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                            target.damage(2.0, player);
                            break;
                        default:
                            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 1));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                            break;
                    }

                    CorePlugin.getCooldowns().addCooldown(new Cooldown(player.getUniqueId(),
                            this.getClass().getSimpleName().toLowerCase(),
                            (System.currentTimeMillis() + (getCooldown(level) * 1000L))));
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
