package net.nighthawkempires.races.ability.human;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import static org.bukkit.ChatColor.RED;

public class QuenAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return 60 + getDuration(level);
    }

    public int getMaxLevel() {
        return 2;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.CHAINMAIL_HELMET;
    }

    public RaceType getRaceType() {
        return RaceType.HUMAN;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.HUMAN, 3);
    }

    public String getName() {
        return "Quen";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Knocks attacking enemy backwards."};
            default -> new String[] {"Forms a protective shield around the", "Witcher that protects them from an", "incoming attack.", "", "Duration: 10s"};
        };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        PlayerData.HumanData humanData = RacesPlugin.getPlayerData().human;
        if (e instanceof PlayerInteractEvent) {
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

                humanData.quen.add(player.getUniqueId());
                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    humanData.quen.remove(player.getUniqueId());
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Quen has worn off."));
                }, 200);
                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Quen has been enabled."));

                addCooldown(this, player, level);
            }
        } else if (e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (humanData.quen.contains(player.getUniqueId())) {
                    if (userModel.hasAbility(this)) {
                        int level = userModel.getLevel(this);

                        event.setCancelled(true);
                        humanData.quen.remove(player.getUniqueId());
                        player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Successfully blocked attack using Quen."));
                        player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1f, 1f);

                        if (level == 2) {
                            if (event.getDamager() instanceof LivingEntity) {
                                LivingEntity entity = (LivingEntity) event.getDamager();

                                Vector center = player.getLocation().toVector();
                                Vector vector = entity.getLocation().toVector().subtract(center);
                                entity.setVelocity(vector.normalize().multiply(1.7).setY(0.14));
                            }
                        }
                    }
                }
            }
        }
    }

    public int getId() {
        return 39;
    }

    public int getDuration(int level) {
        return 10;
    }
}
