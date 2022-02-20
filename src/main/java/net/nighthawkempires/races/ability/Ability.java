package net.nighthawkempires.races.ability;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.event.AbilityUnlockEvent;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import net.nighthawkempires.regions.region.RegionModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.checkerframework.checker.units.qual.A;

import static net.nighthawkempires.regions.RegionsPlugin.getRegionRegistry;
import static net.nighthawkempires.regions.region.RegionFlag.CROP_TRAMPLE;
import static net.nighthawkempires.regions.region.RegionFlag.RACE_ABILITIES;
import static net.nighthawkempires.regions.region.RegionFlag.Result.DENY;

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

    default void passive(Event e, Ability a) {
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

            int taskId = RacesPlugin.getPlayerData().getTaskId(player, a);
            if (taskId != -1) {
                Bukkit.getScheduler().cancelTask(taskId);
                RacesPlugin.getPlayerData().setTaskId(player, a, -1);
            }
        } else if (e instanceof AbilityUnlockEvent) {
            AbilityUnlockEvent event = (AbilityUnlockEvent) e;
            Player player = event.getPlayer();

            if (event.getAbility() == this) {
                run(player);
            }
        }
    }

    default boolean checkCooldown(Ability ability, Player player) {
        return checkCooldown(ability, player, true);
    }

    default boolean checkCooldown(Ability ability, Player player, boolean message) {
        boolean cooldown = CorePlugin.getCooldowns().hasActiveCooldown(player.getUniqueId(),
                ability.getClass().getSimpleName().toLowerCase());

        if (cooldown && message) {
            player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "There is another "
                    + CorePlugin.getCooldowns().getActive(player.getUniqueId(), ability.getClass().getSimpleName().toLowerCase()).timeLeft()
                    + " before you can use this ability again."));
        }

        return cooldown;
    }

    default void addCooldown(Ability ability, Player player, int level) {
        CorePlugin.getCooldowns().addCooldown(new Cooldown(player.getUniqueId(),
                ability.getClass().getSimpleName().toLowerCase(),
                (System.currentTimeMillis() + (ability.getCooldown(level) * 1000L))));
    }

    default boolean canUseRaceAbility(Location location) {
        RegionModel region = getRegionRegistry().getObeyRegion(location);

        if (region != null) {
            return region.getFlagResult(RACE_ABILITIES) != DENY;
        }
        return true;
    }

    default boolean canUseRaceAbility(Player player) {
        return canUseRaceAbility(player.getLocation());
    }

    default boolean isSyphoned(Player player) {
        return RacesPlugin.getPlayerData().demon.syphoned.contains(player.getUniqueId());
    }

    int getId();

    int getDuration(int level);

    public enum AbilityType {
        ACTIVE, BOUND, PASSIVE
    }
}