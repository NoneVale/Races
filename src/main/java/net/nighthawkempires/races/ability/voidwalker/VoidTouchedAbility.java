package net.nighthawkempires.races.ability.voidwalker;

import com.google.common.collect.Lists;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class VoidTouchedAbility implements Ability {

    private final List<Material> endBlocks = Lists.newArrayList(Material.END_STONE, Material.END_STONE_BRICK_SLAB, Material.END_STONE_BRICK_STAIRS,
            Material.END_STONE_BRICK_WALL, Material.END_STONE_BRICKS, Material.END_ROD, Material.ENDER_CHEST, Material.END_PORTAL_FRAME, Material.END_PORTAL,
            Material.PURPUR_BLOCK, Material.PURPUR_PILLAR, Material.PURPUR_SLAB, Material.PURPUR_STAIRS, Material.CHORUS_FLOWER, Material.CHORUS_PLANT,
            Material.SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.GRAY_SHULKER_BOX,
            Material.GREEN_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.LIME_SHULKER_BOX, Material.MAGENTA_SHULKER_BOX,
            Material.ORANGE_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.RED_SHULKER_BOX, Material.WHITE_SHULKER_BOX,
            Material.YELLOW_SHULKER_BOX);

    public AbilityType getAbilityType() {
        return AbilityType.PASSIVE;
    }

    public int getCooldown(int level) {
        return 0;
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.END_STONE;
    }

    public RaceType getRaceType() {
        return RaceType.VOIDWALKER;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(getRaceType(), 1);
    }


    public String getName() {
        return "Void-Touched";
    }

    public String[] getDescription(int level) {
        return new String[] { "" };
    }

    public void run(Player player) {
        PlayerData.VoidwalkerData data = RacesPlugin.getPlayerData().voidwalker;
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.hasAbility(this)) {
            data.voidTouchedMap.put(player.getUniqueId(),
                    Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
                        if (!userModel.hasAbility(this)) {
                            Bukkit.getScheduler().cancelTask(data.voidTouchedMap.get(player.getUniqueId()));
                            return;
                        }

                        int level = userModel.getLevel(this);
                        int radius = getRadius(level);

                        Location location = player.getLocation();
                        boolean endBlock = false;
                        for (int x = location.getBlockX() - radius; x < location.getBlockX() + radius; x++) {
                            for (int y = location.getBlockX() - radius; y < location.getBlockX() + radius; y++) {
                                for (int z = location.getBlockX() - radius; z < location.getBlockX() + radius; z++) {
                                    Block block = location.getWorld().getBlockAt(x, y, z);

                                    if (endBlocks.contains(block.getType())) endBlock = true;
                                    if (endBlock) break;
                                }
                                if (endBlock) break;
                            }
                            if (endBlock) break;
                        }

                        if (endBlock) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 0, false, false, true));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1, false, false, true));
                        }
                    }, 0L, 100L));
        }
    }

    public void run(Event e) {
        PlayerData.VoidwalkerData data = RacesPlugin.getPlayerData().voidwalker;
        if (e instanceof PlayerJoinEvent) {
            PlayerJoinEvent event = (PlayerJoinEvent) e;
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                data.voidTouchedMap.put(player.getUniqueId(),
                        Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
                            if (!userModel.hasAbility(this)) {
                                Bukkit.getScheduler().cancelTask(data.voidTouchedMap.get(player.getUniqueId()));
                                return;
                            }

                            int level = userModel.getLevel(this);
                            int radius = getRadius(level);

                            Location location = player.getLocation();
                            boolean endBlock = false;
                            for (int x = location.getBlockX() - radius; x < location.getBlockX() + radius; x++) {
                                for (int y = location.getBlockY() - radius; y < location.getBlockY() + radius; y++) {
                                    for (int z = location.getBlockZ() - radius; z < location.getBlockZ() + radius; z++) {
                                        Block block = location.getWorld().getBlockAt(x, y, z);

                                        if (endBlocks.contains(block.getType())) endBlock = true;
                                        if (endBlock) break;
                                    }
                                    if (endBlock) break;
                                }
                                if (endBlock) break;
                            }

                            if (endBlock) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 0, false, false, true));
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0, false, false, true));
                            }
                        }, 0L, 100L));
            }
        } else if (e instanceof PlayerQuitEvent) {
            PlayerQuitEvent event = (PlayerQuitEvent) e;
            Player player = event.getPlayer();

            if (data.voidTouchedMap.containsKey(player.getUniqueId())) {
                Bukkit.getScheduler().cancelTask(data.voidTouchedMap.get(player.getUniqueId()));
            }
        }
    }

    public int getId() {
        return 92;
    }

    public int getDuration(int level) {
        return 0;
    }

    private int getRadius(int level) {
        int radius = 0;
        switch (level) {
            case 1: radius = 5; break;
            case 2: radius = 10; break;
            case 3: radius = 15; break;
            default: break;
        }
        return radius;
    }
}
