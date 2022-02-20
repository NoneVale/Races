package net.nighthawkempires.races.ability.demon;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.util.EntityUtil;
import net.nighthawkempires.guilds.util.AllyUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.UUID;

import static org.bukkit.ChatColor.RED;

public class PossessionAbility implements Ability {

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
        return 2;
    }

    public Material getDisplayItem() {
        return Material.RABBIT_HIDE;
    }

    public RaceType getRaceType() {
        return RaceType.DEMON;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DEMON, 2);
    }

    public String getName() {
        return "Possession";
    }

    public String[] getDescription(int level) {
        return new String[0];
    }

    public void run(Player player) {}

    public void run(Event e) {
        PlayerData.DemonData demonData = RacesPlugin.getPlayerData().demon;
        PlayerData.VoidwalkerData voidwalkerData = RacesPlugin.getPlayerData().voidwalker;
        if (e instanceof PlayerInteractEvent event) {
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                if (checkCooldown(this, player)) return;

                if (!canUseRaceAbility(player)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(RED + "You can not use Race Abilities here."));
                    return;
                } else if (isSyphoned(player)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(RED + "Your powers are being syphoned by a demon."));
                    return;
                }

                int level = userModel.getLevel(this);

                int radius = switch (level) {
                    case 2, 3 -> 8;
                    default -> 5;
                };

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have started possession of nearby unholy creatures."));

                List<UUID> possessed = Lists.newArrayList();
                World world = player.getWorld();
                world.getEntitiesByClass(LivingEntity.class).stream().filter((livingEntity) ->
                        livingEntity.getLocation().distance(player.getLocation()) <= radius)
                        .forEach((entity) -> {
                            if (EntityUtil.isUnholy(entity)) {
                                if (!RacesPlugin.getMobData().isPet(entity)) {
                                    possessed.add(entity.getUniqueId());
                                    RacesPlugin.getMobData().addPet(entity);

                                    if (entity instanceof Mob mob) {
                                        if (mob.getTarget() == player) {
                                            mob.setTarget(null);
                                        }
                                    }
                                } else {
                                    if (level > 2) {
                                        if (entity instanceof Enderman enderman) {
                                            for (UUID uuid : voidwalkerData.endermen.keySet()) {
                                                if (Bukkit.getPlayer(uuid) != null) {
                                                    Player target = Bukkit.getPlayer(uuid);

                                                    if (!AllyUtil.isAlly(player, target)) {
                                                        List<UUID> endermen = Lists.newArrayList(voidwalkerData.endermen.get(uuid));

                                                        if (endermen.contains(enderman.getUniqueId())) {
                                                            endermen.remove(enderman.getUniqueId());
                                                            voidwalkerData.endermen.put(uuid, endermen);
                                                            possessed.add(entity.getUniqueId());
                                                            RacesPlugin.getMobData().addPet(entity);

                                                            if (target.getWorld().getUID() == world.getUID()) {
                                                                enderman.setTarget(target);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else if (entity instanceof Piglin || entity instanceof Hoglin || entity instanceof Blaze ||
                                                entity instanceof Ghast || entity instanceof PiglinBrute) {
                                            for (UUID uuid : demonData.guardians.keySet()) {
                                                if (Bukkit.getPlayer(uuid) != null) {
                                                    Player target = Bukkit.getPlayer(uuid);

                                                    if (!AllyUtil.isAlly(player, target)) {
                                                        List<UUID> guardians = Lists.newArrayList(demonData.guardians.get(uuid));

                                                        if (guardians.contains(entity.getUniqueId())) {
                                                            guardians.remove(entity.getUniqueId());
                                                            demonData.guardians.put(uuid, guardians);
                                                            possessed.add(entity.getUniqueId());
                                                            RacesPlugin.getMobData().addPet(entity);

                                                            Mob mob = (Mob) entity;
                                                            if (target.getWorld().getUID() == world.getUID()) {
                                                                mob.setTarget(target);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                });
                demonData.possessed.put(player.getUniqueId(), possessed);

                for (int i = 0; i < 20 * getDuration(level); i++) {
                    if (i % 8 == 0) {
                        int finalI = i;
                        Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                            for (UUID uuid : possessed) {
                                if (Bukkit.getEntity(uuid) != null && !Bukkit.getEntity(uuid).isDead()) {
                                    Entity entity = Bukkit.getEntity(uuid);

                                    world.spawnParticle(Particle.LAVA, entity.getLocation(), finalI / 2, 1, 1, 1);
                                    world.spawnParticle(Particle.SMOKE_NORMAL, entity.getLocation(), finalI / 4, 1, 1, 1);

                                    if (entity instanceof Mob mob) {
                                        if (mob.getTarget() == null) {
                                            if (mob.getLocation().distance(player.getLocation()) >= 5) {
                                                mob.getPathfinder().moveTo(player);
                                            }
                                        }
                                    }
                                }
                            }
                        }, i);
                    }
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    if (demonData.possessed.containsKey(player.getUniqueId())) {
                        for (UUID uuid : demonData.possessed.get(player.getUniqueId())) {
                            if (Bukkit.getEntity(uuid) != null && !Bukkit.getEntity(uuid).isDead()) {
                                RacesPlugin.getMobData().removePet(Bukkit.getEntity(uuid));
                            }
                        }
                    }
                    demonData.possessed.remove(player.getUniqueId());
                }, getDuration(level) * 20L);

                addCooldown(this, player, level);
            }
        } else if (e instanceof EntityTargetLivingEntityEvent event) {
            if (event.getTarget() instanceof Player player) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this) && demonData.possessed.containsKey(player.getUniqueId())) {
                    List<UUID> possessed = demonData.possessed.get(player.getUniqueId());

                    if (possessed.contains(event.getEntity().getUniqueId())) {
                        event.setCancelled(true);
                    }
                }
            }
        } else if (e instanceof EntityDamageByEntityEvent event) {
            if (event.getDamager() instanceof Player player) {
                if (demonData.possessed.keySet().contains(player.getUniqueId())) {
                    List<UUID> possessed = demonData.possessed.get(player.getUniqueId());

                    for (UUID uuid : possessed) {
                        if (player.getWorld().getEntity(uuid) != null) {
                            Entity entity = player.getWorld().getEntity(uuid);
                            if (entity instanceof Mob mob) {
                                if (mob.getTarget() == null) {
                                    if (event.getEntity() instanceof LivingEntity livingEntity) {
                                        if (!possessed.contains(livingEntity.getUniqueId())) {
                                            mob.setTarget(livingEntity);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (event.getDamager() instanceof Projectile projectile) {
                if (projectile.getShooter() instanceof Player player) {
                    if (demonData.possessed.keySet().contains(player.getUniqueId())) {
                        List<UUID> possessed = demonData.possessed.get(player.getUniqueId());

                        for (UUID uuid : possessed) {
                            if (player.getWorld().getEntity(uuid) != null) {
                                Entity entity = player.getWorld().getEntity(uuid);
                                if (entity instanceof Mob mob) {
                                    if (mob.getTarget() == null) {
                                        if (event.getEntity() instanceof LivingEntity livingEntity) {
                                            if (!possessed.contains(livingEntity.getUniqueId())) {
                                                mob.setTarget(livingEntity);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (event.getEntity() instanceof Player player) {
                if (demonData.possessed.keySet().contains(player.getUniqueId())) {
                    List<UUID> possessed = demonData.possessed.get(player.getUniqueId());

                    for (UUID uuid : possessed) {
                        if (player.getWorld().getEntity(uuid) != null) {
                            Entity entity = player.getWorld().getEntity(uuid);
                            if (entity instanceof Mob mob) {
                                if (mob.getTarget() == null) {
                                    if (event.getDamager() instanceof LivingEntity livingEntity) {
                                        if (!possessed.contains(livingEntity.getUniqueId())) {
                                            mob.setTarget(livingEntity);
                                        }
                                    } else if (event.getDamager() instanceof Projectile projectile) {
                                        if (projectile.getShooter() instanceof LivingEntity livingEntity) {
                                            if (!possessed.contains(livingEntity.getUniqueId())) {
                                                mob.setTarget(livingEntity);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (e instanceof EntityDeathEvent event) {
            for (UUID uuid : demonData.possessed.keySet()) {
                List<UUID> possessed = Lists.newArrayList(demonData.possessed.get(uuid));

                if (possessed.contains(event.getEntity().getUniqueId())) {
                    possessed.remove(event.getEntity().getUniqueId());

                    demonData.possessed.put(uuid, possessed);
                    break;
                }
            }
        }
    }

    public int getId() {
        return 16;
    }

    public int getDuration(int level) {
        return switch (level) {
            case 2, 3 -> 15;
            default -> 10;
        };
    }
}