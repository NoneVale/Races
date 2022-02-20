package net.nighthawkempires.races.ability.human;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.ChatColor.RED;

public class CripplingShotAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        switch (level) {
            case 2:
            case 3:
                return 25;
            default: return 30;
        }
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.ARROW;
    }

    public RaceType getRaceType() {
        return RaceType.HUMAN;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.HUMAN, 2);
    }

    public String getName() {
        return "Crippling Shot";
    }

    public String[] getDescription(int level) {
        return new String[0];
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        PlayerData.HumanData humanData = RacesPlugin.getPlayerData().human;
        if (e instanceof ProjectileHitEvent) {
            ProjectileHitEvent event = (ProjectileHitEvent) e;
            if (event.getEntity() instanceof Arrow) {
                if (event.getEntity().getShooter() instanceof Player) {
                    Player player = (Player) event.getEntity().getShooter();
                    UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                    humanData.cripplingShot.remove(player.getUniqueId());

                    if (userModel.hasAbility(this)) {
                        int level = userModel.getLevel(this);
                        if (event.getHitEntity() != null) {
                            if (event.getHitEntity() instanceof LivingEntity) {
                                LivingEntity entity = (LivingEntity) event.getHitEntity();

                                switch (level) {
                                    case 2:
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 3));
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                                        break;
                                    case 3:
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 3));
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                                        break;
                                    default:
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0));
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 1));
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
                                        break;
                                }

                                addCooldown(this, player, level);
                            }
                        }
                    }
                }
            }
        } else if (e instanceof PlayerInteractEvent) {
            PlayerInteractEvent event = (PlayerInteractEvent) e;
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                if (checkCooldown(this, player)) return;

                if (!canUseRaceAbility(player)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(RED + "You can not use Race Abilities here."));
                    return;
                } else if (isSyphoned(player)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(RED + "Your powers are being syphoned by a demon."));
                    return;
                }

                int level = userModel.getLevel(this);

                if (humanData.blackForgedArrow.contains(player.getUniqueId())) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Black Forged Arrow and Crippling Shot can not be" +
                            " enabled at the same time.  Disabling Black Forged Arrow."));
                    humanData.blackForgedArrow.remove(player.getUniqueId());
                }

                humanData.cripplingShot.add(player.getUniqueId());
                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Crippling Shot enabled for the next arrow shot."));
            }
        }
    }

    public int getId() {
        return 36;
    }

    public int getDuration(int level) {
        return 0;
    }
}
