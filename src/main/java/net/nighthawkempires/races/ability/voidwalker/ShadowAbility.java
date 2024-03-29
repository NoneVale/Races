package net.nighthawkempires.races.ability.voidwalker;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.ItemStack;
import net.nighthawkempires.core.CorePlugin;
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

public class ShadowAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.BOUND;
    }

    public int getCooldown(int level) {
        return 60 + getDuration(level);
    }

    public int getMaxLevel() {
        return 4;
    }

    public int getCost(int level) {
        return switch (level) {
            case 3, 4 -> 2;
            default -> 1;
        };
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
        return "Shadow";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Gain Jump + Speed boost while", "active."};
            case 3 -> new String[] {"Increase duration to 10s."};
            case 4 -> new String[] {"Mobs no longer target you while", "in shadow form."};
            default -> new String[] {"Voidwalkers can completely vanish from", "the sight of others", "", "Duration: 5s"};
        };
    }

    public void run(Player player) {}

    public void run(Event e) {
        if (e instanceof PlayerInteractEvent event) {
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

                int duration = getDuration(level);

                player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.GRAY + "You have activated ability "
                        + getRaceType().getRaceColor() + this.getName() + ChatColor.GRAY + "."));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration * 20, 1, false, true, true));
                RacesPlugin.getPlayerData().voidwalker.phase.add(player.getUniqueId());

                if (level > 1) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, duration * 20, 1, false, false, true));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration * 20, 1, false, false, true));
                }

                if (userModel.getLevel(this) == getMaxLevel()) {
                    if (RacesPlugin.getPlayerData().voidwalker.phase.contains(player.getUniqueId())) {
                        for (Entity entity : player.getNearbyEntities(100, 100, 100)) {
                            if (entity instanceof Mob mob) {
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
                    equipment.add(new Pair<>(EnumItemSlot.b, new ItemStack(null)));
                    equipment.add(new Pair<>(EnumItemSlot.a, new ItemStack(null)));

                    PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(player.getEntityId(), equipment);

                    for (Player online : player.getWorld().getPlayers()) {
                        if (!online.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                            PlayerConnection conn = ((CraftPlayer) online).getHandle().b;
                            conn.a(packet);
                        }
                    }
                }, 5, 5);
                Bukkit.getScheduler().scheduleSyncDelayedTask(RacesPlugin.getPlugin(), () -> {
                    Bukkit.getScheduler().cancelTask(id);

                    RacesPlugin.getPlayerData().voidwalker.phase.remove(player.getUniqueId());

                    List<Pair<EnumItemSlot, ItemStack>> equipment = Lists.newArrayList();
                    equipment.add(new Pair<>(EnumItemSlot.f, CraftItemStack.asNMSCopy(player.getEquipment().getHelmet())));
                    equipment.add(new Pair<>(EnumItemSlot.e, CraftItemStack.asNMSCopy(player.getEquipment().getChestplate())));
                    equipment.add(new Pair<>(EnumItemSlot.d, CraftItemStack.asNMSCopy(player.getEquipment().getLeggings())));
                    equipment.add(new Pair<>(EnumItemSlot.c, CraftItemStack.asNMSCopy(player.getEquipment().getBoots())));
                    equipment.add(new Pair<>(EnumItemSlot.b, CraftItemStack.asNMSCopy(player.getEquipment().getItemInOffHand())));
                    equipment.add(new Pair<>(EnumItemSlot.a, CraftItemStack.asNMSCopy(player.getEquipment().getItemInMainHand())));

                    PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(player.getEntityId(), equipment);

                    for (Player otherPlayer : player.getWorld().getPlayers()) {
                        PlayerConnection conn = ((CraftPlayer) otherPlayer).getHandle().b;
                        conn.a(packet);
                    }
                }, duration * 20L);

                addCooldown(this, player, level);
            }
        } else if (e instanceof EntityTargetLivingEntityEvent) {
            EntityTargetLivingEntityEvent event = (EntityTargetLivingEntityEvent) e;

            if (event.getTarget() instanceof Player player) {
                UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

                if (userModel.hasAbility(this) && userModel.getLevel(this) == getMaxLevel()) {
                    if (RacesPlugin.getPlayerData().voidwalker.phase.contains(player.getUniqueId())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public int getId() {
        return 42;
    }

    public int getDuration(int level) {
        return switch (level) {
            case 3, 4 -> 10;
            default -> 5;
        };
    }
}