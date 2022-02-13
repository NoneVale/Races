package net.nighthawkempires.races.ability.voidwalker;

import com.google.common.collect.Lists;
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
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.UUID;

public class CallOfTheVoidAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return 180 + getDuration(level);
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.ENDERMAN_SPAWN_EGG;
    }

    public RaceType getRaceType() {
        return RaceType.VOIDWALKER;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(getRaceType(), 2);
    }

    public String getName() {
        return "Call of the Void";
    }

    public String[] getDescription(int level) {
        return new String[] { "" };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        PlayerData.VoidwalkerData data = RacesPlugin.getPlayerData().voidwalker;
        if (e instanceof PlayerInteractEvent) {
            PlayerInteractEvent event = (PlayerInteractEvent) e;
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                if (RacesPlugin.getPlayerData().demon.syphoned.contains(player.getUniqueId())) return;

                if (CorePlugin.getCooldowns().hasActiveCooldown(player.getUniqueId(),
                        this.getClass().getSimpleName().toLowerCase())) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "There is another "
                            + CorePlugin.getCooldowns().getActive(player.getUniqueId(), this.getClass().getSimpleName().toLowerCase()).timeLeft()
                            + " before you can use this ability again."));
                    return;
                }

                int level = userModel.getLevel(this);

                List<UUID> enderman = Lists.newArrayList();
                for (int i = 0; i < level; i++) {
                    enderman.add(player.getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.ENDERMAN).getUniqueId());
                }

                data.endermanMap.put(player.getUniqueId(), enderman);

                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    if (data.endermanMap.containsKey(player.getUniqueId())) {
                        for (UUID uuid : data.endermanMap.get(player.getUniqueId())) {
                            Entity entity = Bukkit.getEntity(uuid);

                            if (entity != null) entity.remove();
                        }

                        data.endermanMap.remove(player.getUniqueId());
                    }
                }, getDuration(level) * 20L);

                CorePlugin.getCooldowns().addCooldown(new Cooldown(player.getUniqueId(),
                        this.getClass().getSimpleName().toLowerCase(),
                        (System.currentTimeMillis() + (getCooldown(userModel.getLevel(this)) * 1000L))));
            }
        } else if (e instanceof EntityTargetLivingEntityEvent) {
            EntityTargetLivingEntityEvent event = (EntityTargetLivingEntityEvent) e;

            if (event.getTarget() instanceof Player) {
                Player player = (Player) event.getTarget();
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this)) {
                    if (data.endermanMap.containsKey(player.getUniqueId())) {
                        List<UUID> enderman = data.endermanMap.get(player.getUniqueId());

                        for (UUID uuid : enderman) {
                            if (event.getEntity().getUniqueId().equals(uuid)) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        } else if (e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;

            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this)) {
                    if (event.getEntity() instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) event.getEntity();

                        if (data.endermanMap.containsKey(player.getUniqueId())) {
                            List<UUID> endermanList = data.endermanMap.get(player.getUniqueId());

                            for (UUID uuid : endermanList) {
                                Entity entity = Bukkit.getEntity(uuid);

                                if (entity != null) {
                                    if (entity instanceof Enderman) {
                                        Enderman enderman = (Enderman) entity;

                                        if (!endermanList.contains(livingEntity.getUniqueId())) {
                                            enderman.setTarget(livingEntity);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this)) {
                    if (event.getDamager() instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) event.getDamager();

                        if (data.endermanMap.containsKey(player.getUniqueId())) {
                            List<UUID> endermanList = data.endermanMap.get(player.getUniqueId());

                            for (UUID uuid : endermanList) {
                                Entity entity = Bukkit.getEntity(uuid);

                                if (entity != null) {
                                    if (entity instanceof Enderman) {
                                        Enderman enderman = (Enderman) entity;

                                        if (!endermanList.contains(livingEntity.getUniqueId())) {
                                            enderman.setTarget(livingEntity);
                                        }
                                    }
                                }
                            }
                        }
                    } else if (event.getDamager() instanceof Projectile) {
                        Projectile projectile = (Projectile) event.getDamager();
                        if (projectile.getShooter() instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity) projectile.getShooter();

                            if (data.endermanMap.containsKey(player.getUniqueId())) {
                                List<UUID> endermanList = data.endermanMap.get(player.getUniqueId());

                                for (UUID uuid : endermanList) {
                                    Entity entity = Bukkit.getEntity(uuid);

                                    if (entity != null) {
                                        if (entity instanceof Enderman) {
                                            Enderman enderman = (Enderman) entity;

                                            if (!endermanList.contains(livingEntity.getUniqueId())) {
                                                enderman.setTarget(livingEntity);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (event.getEntity() instanceof Enderman) {
                Enderman enderman = (Enderman) event.getEntity();

                if (event.getDamager() instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) event.getDamager();

                    for (UUID uuid : data.endermanMap.keySet()) {
                        List<UUID> endermanList = data.endermanMap.get(uuid);

                        if (endermanList.contains(enderman.getUniqueId())) {

                            for (UUID endermanUuid : endermanList) {
                                Entity entity = Bukkit.getEntity(endermanUuid);

                                if (entity != null) {
                                    if (entity instanceof Enderman) {
                                        Enderman endyman = (Enderman) entity;
                                        endyman.setTarget(livingEntity);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } else if (e instanceof EntityDeathEvent) {
            EntityDeathEvent event = (EntityDeathEvent) e;

            if (event.getEntity() instanceof Enderman) {
                Enderman enderman = (Enderman) event.getEntity();

                for (UUID uuid : data.endermanMap.keySet()) {
                    List<UUID> endermanList = data.endermanMap.get(uuid);

                    if (endermanList.contains(enderman.getUniqueId())) {
                        endermanList.remove(enderman.getUniqueId());
                        data.endermanMap.put(uuid, endermanList);
                        break;
                    }
                }
            }
        } else if (e instanceof PlayerMoveEvent) {
            PlayerMoveEvent event = (PlayerMoveEvent) e;
            Player player = event.getPlayer();

            if (data.endermanMap.containsKey(player.getUniqueId())) {
                List<UUID> endermanMap = data.endermanMap.get(player.getUniqueId());

                for (UUID uuid : endermanMap) {
                    Entity entity = Bukkit.getEntity(uuid);

                    if (entity != null) {
                        if (entity instanceof Enderman) {
                            Enderman enderman = (Enderman) entity;

                            if (player.getLocation().distance(enderman.getLocation()) >= 10) {
                                enderman.teleport(player);
                            }
                        }
                    }
                }
            }
        }
    }

    public int getId() {
        return 95;
    }

    public int getDuration(int level) {
        return 300;
    }
}
