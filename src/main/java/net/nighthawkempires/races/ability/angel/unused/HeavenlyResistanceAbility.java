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
import org.bukkit.event.player.PlayerInteractEvent;

public class HeavenlyResistanceAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return 600 + getDuration(level);
    }

    public int getMaxLevel() {
        return 4;
    }

    public int getCost(int level) {
        int cost = 0;
        switch (level) {
            case 2:
            case 3:
            case 4: cost = 1; break;
            default: cost = 2; break;
        }
        return cost;
    }

    public Material getDisplayItem() {
        return Material.ENCHANTED_BOOK;
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.ANGEL, 3);
    }

    public String getName() {
        return "Heavenly Resistance";
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
                // Magic no workey
                if (RacesPlugin.getPlayerData().demon.syphoned.contains(player.getUniqueId())) return;

                int level = userModel.getLevel(this);

                celestialData.heavenlyResistance.add(player.getUniqueId());
                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Heavenly Resistance has been activated."));
                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    celestialData.heavenlyResistance.remove(player.getUniqueId());
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Heavenly Resistance has worn off."));
                }, getDuration(level) * 20L);
            }
        }
    }

    public int getId() {
        return 47;
    }

    public int getDuration(int level) {
        int duration = 0;
        switch (level) {
            case 2: duration = 20; break;
            case 3: duration = 25; break;
            case 4: duration = 30; break;
            default: duration = 15; break;
        }

        return duration;
    }
}
