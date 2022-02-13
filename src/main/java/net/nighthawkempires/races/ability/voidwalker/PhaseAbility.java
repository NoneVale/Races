package net.nighthawkempires.races.ability.voidwalker;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.ItemStack;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PhaseAbility implements Ability {

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
        int cost = 0;
        switch (level) {
            case 3: cost = 2; break;
            default: cost = 1; break;
        }
        return cost;
    }

    public Material getDisplayItem() {
        return Material.GRAY_STAINED_GLASS;
    }

    public RaceType getRaceType() {
        return RaceType.VOIDWALKER;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(getRaceType(), 1);
    }

    public String getName() {
        return "Phase";
    }

    public String[] getDescription(int level) {
        return new String[] { "" };
    }

    public void run(Player player) {}

    public void run(Event e) {
        if (e instanceof PlayerInteractEvent) {
            PlayerInteractEvent event = (PlayerInteractEvent) e;
            Player player = event.getPlayer();
            UserModel user = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (user.hasAbility(this)) {
                if (RacesPlugin.getPlayerData().demon.syphoned.contains(player.getUniqueId())) return;

                if (CorePlugin.getCooldowns().hasActiveCooldown(player.getUniqueId(),
                        this.getClass().getSimpleName().toLowerCase())) {
                    player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "There is another "
                            + CorePlugin.getCooldowns().getActive(player.getUniqueId(), this.getClass().getSimpleName().toLowerCase()).timeLeft()
                            + " before you can use this ability again."));
                    return;
                }

                int level = user.getLevel(this);
                int duration = getDuration(level);

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have activated ability "
                        + getRaceType().getRaceColor() + this.getName() + ChatColor.GRAY + "."));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration * 20, 1, false, true, true));
                RacesPlugin.getPlayerData().voidwalker.activePhaseAbility.add(player.getUniqueId());

                if (user.getLevel(this) == getMaxLevel()) {
                    if (RacesPlugin.getPlayerData().voidwalker.activePhaseAbility.contains(player.getUniqueId())) {
                        for (Entity entity : player.getNearbyEntities(100, 100, 100)) {
                            if (entity instanceof Mob) {
                                Mob mob = (Mob) entity;
                                if (mob.getTarget() != null && mob.getTarget().getEntityId() == player.getEntityId()) {
                                    mob.setTarget(null);
                                }
                            }
                        }
                    }
                }

                int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(RacesPlugin.getPlugin(), () -> {
                    List<Pair<EnumItemSlot, ItemStack>> equipment = Lists.newArrayList();
                    equipment.add(new Pair<>(EnumItemSlot.f, new ItemStack(null)));
                    equipment.add(new Pair<>(EnumItemSlot.e, new ItemStack(null)));
                    equipment.add(new Pair<>(EnumItemSlot.d, new ItemStack(null)));
                    equipment.add(new Pair<>(EnumItemSlot.c, new ItemStack(null)));

                    PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(player.getEntityId(), equipment);

                    for (Player otherPlayer : player.getWorld().getPlayers()) {
                        PlayerConnection conn = ((CraftPlayer) otherPlayer).getHandle().b;
                        conn.a(packet);
                    }
                }, 5, 5);
                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    Bukkit.getScheduler().cancelTask(id);

                    RacesPlugin.getPlayerData().voidwalker.activePhaseAbility.remove(player.getUniqueId());

                    List<Pair<EnumItemSlot, ItemStack>> equipment = Lists.newArrayList();
                    equipment.add(new Pair<>(EnumItemSlot.f, CraftItemStack.asNMSCopy(player.getEquipment().getHelmet())));
                    equipment.add(new Pair<>(EnumItemSlot.e, CraftItemStack.asNMSCopy(player.getEquipment().getChestplate())));
                    equipment.add(new Pair<>(EnumItemSlot.d, CraftItemStack.asNMSCopy(player.getEquipment().getLeggings())));
                    equipment.add(new Pair<>(EnumItemSlot.c, CraftItemStack.asNMSCopy(player.getEquipment().getBoots())));

                    PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(player.getEntityId(), equipment);

                    for (Player otherPlayer : player.getWorld().getPlayers()) {
                        PlayerConnection conn = ((CraftPlayer) otherPlayer).getHandle().b;
                        conn.a(packet);
                    }
                }, duration * 20L);

                CorePlugin.getCooldowns().addCooldown(new Cooldown(player.getUniqueId(),
                        this.getClass().getSimpleName().toLowerCase(),
                        (System.currentTimeMillis() + (getCooldown(user.getLevel(this)) * 1000L))));
            }
        } else if (e instanceof EntityTargetLivingEntityEvent) {
            EntityTargetLivingEntityEvent event = (EntityTargetLivingEntityEvent) e;

            if (event.getTarget() instanceof Player) {
                Player player = (Player) event.getTarget();
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this) && userModel.getLevel(this) == getMaxLevel()) {
                    if (RacesPlugin.getPlayerData().voidwalker.activePhaseAbility.contains(player.getUniqueId())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public int getId() {
        return 91;
    }

    public int getDuration(int level) {
        int duration = 0;
        switch (level) {
            case 2: duration = 15; break;
            case 3: duration = 25; break;
            default: duration = 8; break;
        }
        return duration;
    }
}