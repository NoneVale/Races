package net.nighthawkempires.races.ability.voidwalker;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.util.RandomUtil;
import net.nighthawkempires.core.util.StringUtil;
import net.nighthawkempires.guilds.util.AllyUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import net.nighthawkempires.regions.region.RegionModel;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

import static net.nighthawkempires.core.CorePlugin.getMessages;
import static net.nighthawkempires.regions.RegionsPlugin.getRegionRegistry;
import static net.nighthawkempires.regions.region.RegionFlag.ENDERPEARL;
import static net.nighthawkempires.regions.region.RegionFlag.Result.DENY;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.RED;

public class KnowledgeOfThePearlsAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.ACTIVE;
    }

    public int getCooldown(int level) {
        return 30 + getDuration(level);
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.ENDER_PEARL;
    }

    public RaceType getRaceType() {
        return RaceType.VOIDWALKER;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(getRaceType(), 1);
    }

    public String getName() {
        return "Knowledge of the Pearls";
    }

    public String[] getDescription(int level) {
        return new String[0];
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        PlayerData.VoidwalkerData voidwalkerData = RacesPlugin.getPlayerData().voidwalker;
        if (e instanceof ProjectileHitEvent event) {
            if (event.getEntity() instanceof EnderPearl enderPearl) {
                if (enderPearl.getShooter() instanceof Player player) {
                    UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                    if (userModel.hasAbility(this)) {
                        event.setCancelled(true);
                        int level = userModel.getLevel(this);

                        Location location = event.getEntity().getLocation();

                        boolean keep = level > 3;
                        boolean fallDamage = level > 1;
                        PlayerData.VoidwalkerData.PearlMode pearlMode = voidwalkerData.pearlmode.getOrDefault(player.getUniqueId(), PlayerData.VoidwalkerData.PearlMode.NORMAL);

                        if (checkCooldown(this, player)) pearlMode = PlayerData.VoidwalkerData.PearlMode.NORMAL;
                        if (!canUseRaceAbility(player)) pearlMode = PlayerData.VoidwalkerData.PearlMode.NORMAL;
                        if (isSyphoned(player)) pearlMode = PlayerData.VoidwalkerData.PearlMode.NORMAL;

                        if (pearlMode != PlayerData.VoidwalkerData.PearlMode.NORMAL && !canUseRaceAbility(location)) {
                            player.sendMessage(CorePlugin.getMessages().getChatMessage(RED + "Race Abilities can not be used at that location."));
                            player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                            return;
                        }

                        switch (pearlMode) {
                            case NORMAL -> {
                                RegionModel regionTo = getRegionRegistry().getObeyRegion(location);

                                if (regionTo != null) {
                                    if (!regionTo.getBypassRegion().contains(player.getUniqueId())) {
                                        if (regionTo.getFlagResult(ENDERPEARL) == DENY) {
                                            player.sendMessage(getMessages().getChatMessage(RED + "I'm sorry, but ender pearls have been disabled in this region."));
                                            player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                                            return;
                                        }
                                    }
                                }

                                if (!fallDamage) player.damage(5.0);

                                location.setPitch(player.getLocation().getPitch());
                                location.setYaw(player.getLocation().getYaw());
                                player.teleport(location);
                            }
                            case VACUUM -> {
                                location.getWorld().getEntitiesByClasses(new Class[] { LivingEntity.class, Item.class, Projectile.class }).stream().filter((entity) -> {
                                    return entity.getLocation().distance(location) <= 8D;
                                }).forEach((entity) -> {
                                    if ((!(entity instanceof Player) || AllyUtil.isAlly((Player) entity, player))) {
                                        double x = player.getLocation().getX() - location.getX();
                                        double z = player.getLocation().getZ() - location.getZ();

                                        Vector vector = new Vector(x, 0.14, z);
                                        vector.normalize();

                                        entity.setVelocity(entity.getVelocity().add(vector).multiply(-1.7));
                                    }
                                });
                            }
                            case MINION -> {
                                List<LivingEntity> entities = Lists.newArrayList();
                                Enderman enderman = (Enderman) location.getWorld().spawnEntity(location, EntityType.ENDERMAN);
                                enderman.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 0));
                                enderman.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 1));
                                enderman.setCustomName(ChatColor.DARK_GRAY + "Abyssal Guardian");
                                enderman.setCustomNameVisible(true);
                                entities.add(enderman);

                                for (Entity entity : location.getWorld().getNearbyEntities(location, 7, 7, 7)) {
                                    if (entity instanceof LivingEntity livingEntity && !(entity instanceof Player target && AllyUtil.isAlly(player, target))) {
                                        enderman.setTarget(livingEntity);
                                    }
                                }

                                for (int i = 0; i < 2; i++) {
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
                            case EXPLODE -> {
                                location.getWorld().createExplosion(location, 3, false, false);
                            }
                        }

                        if (keep && RandomUtil.fiftyfifty()) {
                            player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                        }
                    }
                }
            }
        } else if (e instanceof ProjectileLaunchEvent event) {

        } else if (e instanceof PlayerInteractEvent event) {
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                int level = userModel.getLevel(this);
                if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (player.isSneaking()) {
                        PlayerData.VoidwalkerData.PearlMode pearlMode = voidwalkerData.pearlmode.getOrDefault(player.getUniqueId(), PlayerData.VoidwalkerData.PearlMode.NORMAL);

                        voidwalkerData.pearlmode.put(player.getUniqueId(), pearlMode.nextMode(level));
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GRAY.asBungee() + "Pearl Mode "
                                + getRaceType().getRaceColor().asBungee() + "" + ChatColor.BOLD.asBungee() + "" + ChatColor.ITALIC.asBungee()
                                + StringUtil.beautify(pearlMode.nextMode(level).toString())));
                    }
                }
            }
        }
    }

    public int getId() {
        return 43;
    }

    public int getDuration(int level) {
        return 0;
    }
}
