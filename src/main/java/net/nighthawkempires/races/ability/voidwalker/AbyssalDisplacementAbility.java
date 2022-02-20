package net.nighthawkempires.races.ability.voidwalker;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.util.RandomUtil;
import net.nighthawkempires.guilds.util.AllyUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import net.nighthawkempires.regions.region.RegionModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.regions.RegionsPlugin.getRegionRegistry;
import static net.nighthawkempires.regions.region.RegionFlag.ENDERPEARL;
import static net.nighthawkempires.regions.region.RegionFlag.Result.DENY;
import static org.bukkit.ChatColor.RED;

public class AbyssalDisplacementAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.ACTIVE;
    }

    public int getCooldown(int level) {
        return 90 + getDuration(level);
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.CHORUS_FRUIT;
    }

    public RaceType getRaceType() {
        return RaceType.VOIDWALKER;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(getRaceType(), 2);
    }

    public String getName() {
        return "Abyssal Displacement";
    }

    public String[] getDescription(int level) {
        return new String[0];
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        PlayerData.VoidwalkerData voidwalkerData = RacesPlugin.getPlayerData().voidwalker;
        if (e instanceof PlayerInteractEvent event) {
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());;

            if (userModel.hasAbility(this)) {
                if (checkCooldown(this, player)) return;

                if (!canUseRaceAbility(player)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "You can not use Race Abilities here."));
                    return;
                } else if (isSyphoned(player)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "Your powers are being syphoned by a demon."));
                    return;
                }

                int level = userModel.getLevel(this);

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Abyssal Displacement has been activated."));
                voidwalkerData.displacement.add(player.getUniqueId());

                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    if (voidwalkerData.displacement.contains(player.getUniqueId())) {
                        player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "Abyssal Displacement has worn off."));
                        voidwalkerData.displacement.remove(player.getUniqueId());
                    }
                }, getDuration(level) * 20L);
            }
        } else if (e instanceof EntityDamageByEntityEvent event) {
            if (event.getEntity() instanceof Player player) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this) && voidwalkerData.displacement.contains(player.getUniqueId())) {
                    int level = userModel.getLevel(this);

                    int radius = level > 2 ? 8 : 5;
                    int chance = level > 1 ? 20 : 0;
                    Location location = player.getLocation();

                    if (RandomUtil.chance(chance)) {
                        List<LivingEntity> entities = Lists.newArrayList();
                        Endermite endermite = (Endermite) location.getWorld().spawnEntity(location, EntityType.ENDERMITE);
                        endermite.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 0));
                        endermite.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 1));
                        endermite.setCustomName(ChatColor.DARK_GRAY + "Abyssal Guardian");
                        endermite.setCustomNameVisible(true);
                        entities.add(endermite);

                        for (Entity entity : location.getWorld().getNearbyEntities(location, 4, 4, 4)) {
                            if (entity instanceof LivingEntity livingEntity && !(entity instanceof Player target && AllyUtil.isAlly(player, target))) {
                                endermite.setTarget(livingEntity);
                            }
                        }

                        Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                            for (LivingEntity entity : entities) {
                                if (!entity.isDead()) {
                                    entity.damage(99999);
                                }
                            }

                            entities.clear();
                        }, 1200L);
                    }

                    double x = location.getX() + (double)(RandomUtil.randomNumber(radius * 2) - radius);
                    double z = location.getZ() + (double)(RandomUtil.randomNumber(radius * 2) - radius);

                    double y = location.getY();

                    Block block = location.getWorld().getBlockAt((int) x, (int) y, (int) z);
                    do {
                        x = location.getX() + (double)(RandomUtil.randomNumber(radius * 2) - radius);
                        z = location.getZ() + (double)(RandomUtil.randomNumber(radius * 2) - radius);
                        y = location.getY() + (double)(RandomUtil.randomNumber(radius * 2) - radius);

                        block = location.getWorld().getBlockAt((int) x, (int) y, (int) z);
                    } while (!block.getType().isAir() && !block.getRelative(BlockFace.DOWN).getType().isSolid());

                    Location to = block.getLocation();
                    RegionModel regionTo = getRegionRegistry().getObeyRegion(location);

                    if (regionTo != null) {
                        if (!regionTo.getBypassRegion().contains(player.getUniqueId())) {
                            if (regionTo.getFlagResult(ENDERPEARL) == DENY) {
                                player.sendMessage(getMessages().getChatMessage(RED + "I'm sorry, but Abyssal Displacement couldn't carry through due to surrounding region restrictions."));
                                voidwalkerData.displacement.remove(player.getUniqueId());
                                return;
                            }
                        }
                    }

                    location.setPitch(player.getLocation().getPitch());
                    location.setYaw(player.getLocation().getYaw());
                    player.teleport(location);
                    voidwalkerData.displacement.remove(player.getUniqueId());
                }
            }
        }
    }

    public int getId() {
        return 45;
    }

    public int getDuration(int level) {
        return 30;
    }
}