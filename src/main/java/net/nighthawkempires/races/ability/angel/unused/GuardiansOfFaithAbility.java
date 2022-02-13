package net.nighthawkempires.races.ability.angel.unused;

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

public class GuardiansOfFaithAbility implements Ability {

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
        return Material.IRON_SWORD;
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.ANGEL, 2);
    }

    public String getName() {
        return "Guardians of Faith";
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

                if (CorePlugin.getCooldowns().hasActiveCooldown(player.getUniqueId(),
                        this.getClass().getSimpleName().toLowerCase())) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "There is another "
                            + CorePlugin.getCooldowns().getActive(player.getUniqueId(), this.getClass().getSimpleName().toLowerCase()).timeLeft()
                            + " before you can use this ability again."));
                    return;
                }

                int level = userModel.getLevel(this);

                List<UUID> vexes = Lists.newArrayList();
                for (int i = 0; i < level; i++) {
                    vexes.add(player.getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.VEX).getUniqueId());
                }

                celestialData.vexMap.put(player.getUniqueId(), vexes);

                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    if (celestialData.vexMap.containsKey(player.getUniqueId())) {
                        for (UUID uuid : celestialData.vexMap.get(player.getUniqueId())) {
                            Entity entity = Bukkit.getEntity(uuid);

                            if (entity != null) entity.remove();
                        }

                        celestialData.vexMap.remove(player.getUniqueId());
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
                    if (celestialData.vexMap.containsKey(player.getUniqueId())) {
                        List<UUID> vex = celestialData.vexMap.get(player.getUniqueId());

                        for (UUID uuid : vex) {
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

                        if (celestialData.vexMap.containsKey(player.getUniqueId())) {
                            List<UUID> vexList = celestialData.vexMap.get(player.getUniqueId());

                            for (UUID uuid : vexList) {
                                Entity entity = Bukkit.getEntity(uuid);

                                if (entity != null) {
                                    if (entity instanceof Vex) {
                                        Vex vex = (Vex) entity;

                                        if (!vexList.contains(livingEntity.getUniqueId())) {
                                            vex.setTarget(livingEntity);
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

                        if (celestialData.vexMap.containsKey(player.getUniqueId())) {
                            List<UUID> vexList = celestialData.vexMap.get(player.getUniqueId());

                            for (UUID uuid : vexList) {
                                Entity entity = Bukkit.getEntity(uuid);

                                if (entity != null) {
                                    if (entity instanceof Vex) {
                                        Vex vex = (Vex) entity;

                                        if (!vexList.contains(livingEntity.getUniqueId())) {
                                            vex.setTarget(livingEntity);
                                        }
                                    }
                                }
                            }
                        }
                    } else if (event.getDamager() instanceof Projectile) {
                        Projectile projectile = (Projectile) event.getDamager();
                        if (projectile.getShooter() instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity) projectile.getShooter();

                            if (celestialData.vexMap.containsKey(player.getUniqueId())) {
                                List<UUID> vexList = celestialData.vexMap.get(player.getUniqueId());

                                for (UUID uuid : vexList) {
                                    Entity entity = Bukkit.getEntity(uuid);

                                    if (entity != null) {
                                        if (entity instanceof Vex) {
                                            Vex vex = (Vex) entity;

                                            if (!vexList.contains(livingEntity.getUniqueId())) {
                                                vex.setTarget(livingEntity);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (event.getEntity() instanceof Vex) {
                Vex vex = (Vex) event.getEntity();

                if (event.getDamager() instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) event.getDamager();

                    for (UUID uuid : celestialData.vexMap.keySet()) {
                        List<UUID> vexList = celestialData.vexMap.get(uuid);

                        if (vexList.contains(vex.getUniqueId())) {

                            for (UUID endermanUuid : vexList) {
                                Entity entity = Bukkit.getEntity(endermanUuid);

                                if (entity != null) {
                                    if (entity instanceof Vex) {
                                        Vex vexy = (Vex) entity;
                                        vexy.setTarget(livingEntity);
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

            if (event.getEntity() instanceof Vex) {
                Vex vex = (Vex) event.getEntity();

                for (UUID uuid : celestialData.vexMap.keySet()) {
                    List<UUID> vexList = celestialData.vexMap.get(uuid);

                    if (vexList.contains(vex.getUniqueId())) {
                        vexList.remove(vex.getUniqueId());
                        celestialData.vexMap.put(uuid, vexList);
                        break;
                    }
                }
            }
        } else if (e instanceof PlayerMoveEvent) {
            PlayerMoveEvent event = (PlayerMoveEvent) e;
            Player player = event.getPlayer();

            if (celestialData.vexMap.containsKey(player.getUniqueId())) {
                List<UUID> vexList = celestialData.vexMap.get(player.getUniqueId());

                for (UUID uuid : vexList) {
                    Entity entity = Bukkit.getEntity(uuid);

                    if (entity != null) {
                        if (entity instanceof Vex) {
                            Vex vex = (Vex) entity;

                            if (player.getLocation().distance(vex.getLocation()) >= 10) {
                                vex.teleport(player);
                            }
                        }
                    }
                }
            }
        }
    }

    public int getId() {
        return 4;
    }

    public int getDuration(int level) {
        return 180;
    }
}
