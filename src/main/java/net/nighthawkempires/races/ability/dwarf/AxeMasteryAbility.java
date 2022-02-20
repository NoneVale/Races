package net.nighthawkempires.races.ability.dwarf;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.cooldown.Cooldown;
import net.nighthawkempires.core.util.RandomUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

import static org.bukkit.ChatColor.RED;

public class AxeMasteryAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.ACTIVE;
    }

    public int getCooldown(int level) {
        return level < 3 ? 15 : 30;
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.IRON_AXE;
    }

    public RaceType getRaceType() {
        return RaceType.DWARF;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DWARF, 1);
    }

    public String getName() {
        return "Axe Mastery";
    }

    public String[] getDescription(int level) {
        return new String[0];
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof EntityDamageByEntityEvent event) {
            if (event.getDamager() instanceof Player player) {
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

                    List<Material> axes = Lists.newArrayList(Material.IRON_AXE, Material.DIAMOND_AXE, Material.GOLDEN_AXE,
                            Material.STONE_AXE, Material.NETHERITE_AXE, Material.WOODEN_AXE);

                    int level = userModel.getLevel(this);

                    if (axes.contains(player.getEquipment().getItemInMainHand().getType())) {
                        int chance = switch (level) {
                            case 2, 3 -> 10;
                            default -> 5;
                        };

                        if (RandomUtil.chance(chance)) {
                            event.setDamage(((level == 1 ? .10 : .15) * event.getDamage()) + event.getDamage());

                            addCooldown(this, player, level);
                        }
                    }
                }
            }
        }
    }

    public int getId() {
        return 22;
    }

    public int getDuration(int level) {
        return 0;
    }
}