package net.nighthawkempires.races.ability;

import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.event.AbilityUnlockEvent;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public interface Ability {

    AbilityType getAbilityType();

    int getCooldown(int level);

    int getMaxLevel();

    int getCost(int level);

    Material getDisplayItem();

    RaceType getRaceType();

    Race getRace();

    String getName();

    String[] getDescription(int level);

    void run(Player player);

    void run(Event e);

    default int taskId() {
        return 0;
    }

    default void clearTaskId() {}

    default void passive(Event e) {
        if (e instanceof PlayerJoinEvent) {
            PlayerJoinEvent event = (PlayerJoinEvent) e;
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                run(player);
            }
        } else if (e instanceof PlayerQuitEvent) {
            PlayerQuitEvent event = (PlayerQuitEvent) e;
            Player player = event.getPlayer();

            if (taskId() != 0) {
                Bukkit.getScheduler().cancelTask(taskId());
                clearTaskId();
            }
        } else if (e instanceof AbilityUnlockEvent) {
            AbilityUnlockEvent event = (AbilityUnlockEvent) e;
            Player player = event.getPlayer();

            if (event.getAbility() == this) {
                run(player);
            }
        }
    }

    int getId();

    int getDuration(int level);

    public enum AbilityType {
        ACTIVE, BOUND, PASSIVE
    }
}