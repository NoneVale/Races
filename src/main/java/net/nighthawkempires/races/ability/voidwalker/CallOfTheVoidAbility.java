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
import org.apache.logging.log4j.core.Core;
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
import org.checkerframework.checker.units.qual.C;

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
                if (checkCooldown(this, player)) return;

                if (!canUseRaceAbility(player)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "You can not use Race Abilities here."));
                    return;
                } else if (isSyphoned(player)) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "Your powers are being syphoned by a demon."));
                    return;
                }

                int level = userModel.getLevel(this);
                int count = switch (level) {
                    case 2 -> 2;
                    case 4 -> 2;
                    default -> 1;
                };

                List<UUID> enderman = Lists.newArrayList();
                for (int i = 0; i < count; i++) {
                    enderman.add(player.getWorld().spawnEntity(player.getLocation().add(0, 1, 0), EntityType.ENDERMAN).getUniqueId());
                }

                data.endermen.put(player.getUniqueId(), enderman);

                if (count > 1) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "The Abyss sends guardians to your aid."));
                } else {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "The Abyss sends a guardian to your aid."));
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    if (data.endermen.containsKey(player.getUniqueId())) {
                        for (UUID uuid : data.endermen.get(player.getUniqueId())) {
                            Entity entity = Bukkit.getEntity(uuid);

                            if (entity != null) entity.remove();
                        }

                        data.endermen.remove(player.getUniqueId());
                    }
                }, getDuration(level) * 20L);

                addCooldown(this, player, level);
            }
        } else if (e instanceof EntityTargetLivingEntityEvent) {
            EntityTargetLivingEntityEvent event = (EntityTargetLivingEntityEvent) e;

            if (event.getTarget() instanceof Player) {
                Player player = (Player) event.getTarget();
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this)) {
                    if (data.endermen.containsKey(player.getUniqueId())) {
                        List<UUID> enderman = data.endermen.get(player.getUniqueId());

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

                        if (data.endermen.containsKey(player.getUniqueId())) {
                            List<UUID> endermanList = data.endermen.get(player.getUniqueId());

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

                        if (data.endermen.containsKey(player.getUniqueId())) {
                            List<UUID> endermanList = data.endermen.get(player.getUniqueId());

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

                            if (data.endermen.containsKey(player.getUniqueId())) {
                                List<UUID> endermanList = data.endermen.get(player.getUniqueId());

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

                    for (UUID uuid : data.endermen.keySet()) {
                        List<UUID> endermanList = data.endermen.get(uuid);

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

                for (UUID uuid : data.endermen.keySet()) {
                    List<UUID> endermanList = data.endermen.get(uuid);

                    if (endermanList.contains(enderman.getUniqueId())) {
                        endermanList.remove(enderman.getUniqueId());
                        data.endermen.put(uuid, endermanList);
                        break;
                    }
                }
            }
        } else if (e instanceof PlayerMoveEvent) {
            PlayerMoveEvent event = (PlayerMoveEvent) e;
            Player player = event.getPlayer();

            if (data.endermen.containsKey(player.getUniqueId())) {
                List<UUID> endermanMap = data.endermen.get(player.getUniqueId());

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
        return 44;
    }

    public int getDuration(int level) {
        return 300;
    }
}
