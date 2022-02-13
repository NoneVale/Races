package net.nighthawkempires.races.ability.angel.unused;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DeathWardAbility implements Ability {
    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return 300 + getDuration(level);
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.TOTEM_OF_UNDYING;
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.ANGEL, 2);
    }

    public String getName() {
        return "Death Ward";
    }

    public String[] getDescription(int level) {
        return new String[0];
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        PlayerData.AngelData celestialData = RacesPlugin.getPlayerData().angel;
        if (e instanceof PlayerInteractEvent) {
            PlayerInteractEvent event = (PlayerInteractEvent) e;
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                if (RacesPlugin.getPlayerData().demon.syphoned.contains(player.getUniqueId())) return;

                int level = userModel.getLevel(this);

                celestialData.deathWard.add(player.getUniqueId());
                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Death Ward has been activated."));
                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    celestialData.deathWard.remove(player.getUniqueId());
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Death Ward has worn off due to not being used."));
                }, 600L);
            }
        } else if (e instanceof EntityDamageEvent) {
            EntityDamageEvent event = (EntityDamageEvent) e;
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this)) {
                    int level = userModel.getLevel(this);

                    if (celestialData.deathWard.contains(player.getUniqueId())) {
                        if (player.getHealth() - event.getFinalDamage() >= 0) {
                            event.setCancelled(true);

                            player.setHealth(2.0 * level);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 1));
                            player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Your life has been spared by Death Ward."));

                            celestialData.deathWard.remove(player.getUniqueId());
                        }
                    }
                }
            }
        }
    }

    public int getId() {
        return 5;
    }

    public int getDuration(int level) {
        return 30;
    }
}
