package net.nighthawkempires.races.ability.voidwalker;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.ItemStack;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.List;

public class ElytraAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.PASSIVE;
    }

    public int getCooldown(int level) {
        return 0;
    }

    public int getMaxLevel() {
        return 1;
    }

    public int getCost(int level) {
        return 2;
    }

    public Material getDisplayItem() {
        return Material.ELYTRA;
    }

    public RaceType getRaceType() {
        return RaceType.VOIDWALKER;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(getRaceType(), 3);
    }

    public String getName() {
        return "Elytra";
    }

    public String[] getDescription(int level) {
        return new String[] { "" };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof PlayerToggleFlightEvent) {
            PlayerToggleFlightEvent event = (PlayerToggleFlightEvent) e;
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

            if (userModel.hasAbility(this)) {
                player.setAllowFlight(false);
                player.setGliding(true);
                event.setCancelled(true);
            }

        } else if (e instanceof PlayerMoveEvent) {
            PlayerMoveEvent event = (PlayerMoveEvent) e;
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                Block below = player.getLocation().subtract(0, 1, 0).getBlock();

                if (below.getType().isSolid()) {
                    if (player.isGliding()) player.setGliding(false);
                }
            }
        } else if (e instanceof EntityToggleGlideEvent) {
            EntityToggleGlideEvent event = (EntityToggleGlideEvent) e;
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this)) {
                    Block below = player.getLocation().subtract(0, 1, 0).getBlock();
                    if (!below.getType().isSolid()) {
                        event.setCancelled(true);
                    }
                }
            }
        } else if (e instanceof PlayerJumpEvent) {
            PlayerJumpEvent event = (PlayerJumpEvent) e;
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

            if (userModel.hasAbility(this)) {
                if (!player.getAllowFlight()) player.setAllowFlight(true);

                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    if (player.getAllowFlight()) player.setAllowFlight(false);
                }, 20);
            }
        }
    }

    public int getId() {
        return 97;
    }

    public int getDuration(int level) {
        return 0;
    }
}