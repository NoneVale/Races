package net.nighthawkempires.races.listeners;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.event.AbilityUnlockEvent;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class PlayerListener implements Listener {

    private List<NamespacedKey> recipes = Lists.newArrayList(RacesPlugin.TEAR_OF_GOD_RECIPE, RacesPlugin.MINERS_TROPHY_RECIPE,
            RacesPlugin.ELIXIR_OF_LIFE_RECIPE, RacesPlugin.INFERNAL_HEART_RECIPE, RacesPlugin.VOID_FORGED_PENDANT_RECIPE);

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserModel user = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (user.getRace() == null || user.getRace().getRaceType() == null) {
            user.setRace(RacesPlugin.getRaceManager().getDefaultRace());
        }

        player.setHealthScaled(true);
        player.setHealthScale(user.getRace().getRaceType().getBaseHealth() + (double) user.getRace().getTier());
        player.discoverRecipes(recipes);

        AttributeInstance instance = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double health = user.getRace().getRaceType().getBaseHealth() + (double) user.getRace().getTier();

        if (instance.getBaseValue() != health) {
            instance.setBaseValue(health);
            player.saveData();
        }
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (player.getKiller() != null) {
            Player killer = player.getKiller();

            if (!CorePlugin.getCooldowns().hasActiveCooldown(killer.getUniqueId(), "perk-" + player.getUniqueId().toString())) {
                killer.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have received a perk point for killing "
                        + ChatColor.GREEN + player.getName() + ChatColor.GRAY + "."));
                CorePlugin.getCooldowns().addCooldown(new Cooldown(killer.getUniqueId(),
                        "perk-" + player.getUniqueId().toString(),
                        (System.currentTimeMillis() + (86400000L))));

                UserModel userModel = RacesPlugin.getUserRegistry().getUser(killer.getUniqueId());
                userModel.addPerkPoints(1);
            }
        }
    }
}