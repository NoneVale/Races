package net.nighthawkempires.races.ability.demon;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.util.List;
import java.util.UUID;

import static org.bukkit.ChatColor.RED;

public class HellishGuardiansAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.ACTIVE;
    }

    public int getCooldown(int level) {
        return 300 + getDuration(level);
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCost(int level) {
        return switch (level) {
            case 4, 5 -> 2;
            default -> 1;
        };
    }

    public Material getDisplayItem() {
        return Material.WITHER_SKELETON_SKULL;
    }

    public RaceType getRaceType() {
        return RaceType.DEMON;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DEMON, 2);
    }

    public String getName() {
        return "Hellish Guardians";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[]{"A Hoglin now comes to your", "aid."};
            case 3 -> new String[]{"2 Blazes now come to your", "aid."};
            case 4 -> new String[]{"2 Ghast now come to your", "aid."};
            case 5 -> new String[]{"2 Piglin Brutes now come to", "your aid."};
            default ->  new String[] {"When a demon is low on health", "guardians from hell come to your", "aid", "", "3 Piglins come to your aid."};
        };
    }
    public void run(Player player) {

    }

    public void run(Event e) {
        PlayerData.DemonData demonData = RacesPlugin.getPlayerData().demon;
        if (e instanceof EntityDamageByEntityEvent event) {
            if (event.getEntity() instanceof Player player) {
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

                    if (player.getHealth() - event.getFinalDamage() < 5) {
                        int piglin = 2;
                        int hoglin = switch (level) {
                            case 2, 3, 4, 5 -> 1;
                            default -> 0;
                        };
                        int blaze = switch (level) {
                            case 3, 4, 5 -> 2;
                            default -> 0;
                        };
                        int ghast = switch (level) {
                            case 4, 5 -> 2;
                            default -> 0;
                        };
                        int brute = switch (level) {
                            case 5 -> 2;
                            default -> 0;
                        };

                        List<UUID> guardians = Lists.newArrayList();

                        for (int i = 0; i < piglin; i++) {
                            Mob mob = (Mob) player.getWorld().spawnEntity(player.getLocation(), EntityType.PIGLIN, true);
                            mob.setTarget((LivingEntity) event.getDamager());
                            RacesPlugin.getMobData().addPet(mob);
                            guardians.add(mob.getUniqueId());
                        }
                        for (int i = 0; i < hoglin; i++) {
                            Mob mob = (Mob) player.getWorld().spawnEntity(player.getLocation(), EntityType.HOGLIN, true);
                            mob.setTarget((LivingEntity) event.getDamager());
                            RacesPlugin.getMobData().addPet(mob);
                            guardians.add(mob.getUniqueId());
                        }
                        for (int i = 0; i < blaze; i++) {

                            Mob mob = (Mob) player.getWorld().spawnEntity(player.getLocation(), EntityType.BLAZE, true);
                            mob.setTarget((LivingEntity) event.getDamager());
                            RacesPlugin.getMobData().addPet(mob);
                            guardians.add(mob.getUniqueId());
                        }
                        for (int i = 0; i < ghast; i++) {
                            Mob mob = (Mob) player.getWorld().spawnEntity(player.getLocation(), EntityType.GHAST, true);
                            mob.setTarget((LivingEntity) event.getDamager());
                            RacesPlugin.getMobData().addPet(mob);
                            guardians.add(mob.getUniqueId());
                        }
                        for (int i = 0; i < brute; i++) {
                            Mob mob = (Mob) player.getWorld().spawnEntity(player.getLocation(), EntityType.PIGLIN_BRUTE, true);
                            mob.setTarget((LivingEntity) event.getDamager());
                            RacesPlugin.getMobData().addPet(mob);
                            guardians.add(mob.getUniqueId());
                        }

                        demonData.guardians.put(player.getUniqueId(), guardians);

                        Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                            if (demonData.guardians.containsKey(player.getUniqueId())) {
                                for (UUID uuid : demonData.guardians.get(player.getUniqueId())) {
                                    if (Bukkit.getEntity(uuid) != null && !Bukkit.getEntity(uuid).isDead()) {
                                        if (Bukkit.getEntity(uuid) instanceof Mob mob) {
                                            mob.damage(100);
                                            RacesPlugin.getMobData().removePet(mob);
                                        }
                                    }
                                }
                            }
                            demonData.guardians.remove(player.getUniqueId());
                        }, getDuration(level) * 20L);

                        addCooldown(this, player, level);
                    }
                }
            }

            if (event.getDamager() instanceof Player player) {
                if (demonData.guardians.keySet().contains(player.getUniqueId())) {
                    List<UUID> guardians = demonData.guardians.get(player.getUniqueId());

                    for (UUID uuid : guardians) {
                        if (Bukkit.getEntity(uuid) != null) {
                            Entity entity = Bukkit.getEntity(uuid);
                            if (entity instanceof Mob mob) {
                                if (mob.getTarget() == null) {
                                    if (event.getEntity() instanceof LivingEntity livingEntity) {
                                        if (!guardians.contains(livingEntity.getUniqueId())) {
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
                    if (demonData.guardians.keySet().contains(player.getUniqueId())) {
                        List<UUID> guardians = demonData.guardians.get(player.getUniqueId());

                        for (UUID uuid : guardians) {
                            if (Bukkit.getEntity(uuid) != null) {
                                Entity entity = Bukkit.getEntity(uuid);
                                if (entity instanceof Mob mob) {
                                    if (mob.getTarget() == null) {
                                        if (event.getEntity() instanceof LivingEntity livingEntity) {
                                            if (!guardians.contains(livingEntity.getUniqueId())) {
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
                if (demonData.guardians.keySet().contains(player.getUniqueId())) {
                    List<UUID> guardians = demonData.guardians.get(player.getUniqueId());

                    for (UUID uuid : guardians) {
                        if (Bukkit.getEntity(uuid) != null) {
                            Entity entity = Bukkit.getEntity(uuid);
                            if (entity instanceof Mob mob) {
                                if (mob.getTarget() == null) {
                                    if (event.getDamager() instanceof LivingEntity livingEntity) {
                                        if (!guardians.contains(livingEntity.getUniqueId())) {
                                            mob.setTarget(livingEntity);
                                        }
                                    } else if (event.getDamager() instanceof Projectile projectile) {
                                        if (projectile.getShooter() instanceof LivingEntity livingEntity) {
                                            if (!guardians.contains(livingEntity.getUniqueId())) {
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
        } else if (e instanceof EntityTargetLivingEntityEvent event) {
            if (event.getTarget() instanceof Player player) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this) && demonData.guardians.containsKey(player.getUniqueId())) {
                    List<UUID> guardians = demonData.guardians.get(player.getUniqueId());

                    if (guardians.contains(event.getEntity().getUniqueId())) {
                        event.setCancelled(true);
                    }
                }
            }
        } else if (e instanceof EntityDeathEvent event) {
            for (UUID uuid : demonData.guardians.keySet()) {
                List<UUID> guardians = Lists.newArrayList(demonData.guardians.get(uuid));

                if (guardians.contains(event.getEntity().getUniqueId())) {
                    guardians.remove(event.getEntity().getUniqueId());
                    RacesPlugin.getMobData().removePet(event.getEntity());

                    demonData.guardians.put(uuid, guardians);
                    break;
                }
            }
        }
    }

    public int getId() {
        return 14;
    }

    public int getDuration(int level) {
        return 60;
    }
}