package net.nighthawkempires.races.ability.dwarf;

import net.nighthawkempires.core.util.RandomUtil;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MyPreciousAbility implements Ability {

    public AbilityType getAbilityType() {
        return AbilityType.ACTIVE;
    }

    public int getCooldown(int level) {
        return 0;
    }

    public int getMaxLevel() {
        return 3;
    }

    public int getCost(int level) {
        return 1;
    }

    public Material getDisplayItem() {
        return Material.DIAMOND;
    }

    public RaceType getRaceType() {
        return RaceType.DWARF;
    }

    public Race getRace() {
        return RacesPlugin.getRaceManager().getRace(RaceType.DWARF, 3);
    }

    public String getName() {
        return "My Precious!";
    }

    public String[] getDescription(int level) {
        return switch (level) {
            case 2 -> new String[] {"Increase chance to 30%."};
            case 3 -> new String[] {"Increase chance to 45%."};
            default -> new String[] {"Masters of mining, dwarves are able", "to manipulate the stone around",
                    "deepslate ores to yield more profit.", "", "15% for an extra drop on", "deepslate ores."};
        };
    }

    public void run(Player player) {

    }

    public void run(Event e) {
        if (e instanceof BlockBreakEvent event) {
            Player player = event.getPlayer();
            UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

            if (userModel.hasAbility(this)) {
                int level = userModel.getLevel(this);

                int chance = switch (level) {
                    case 2 -> 30;
                    case 3 -> 45;
                    default -> 15;
                };

                if (RandomUtil.chance(chance)) {
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    if (!itemStack.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) {
                        switch (itemStack.getType()) {
                            case DIAMOND_PICKAXE, GOLDEN_PICKAXE, IRON_PICKAXE, NETHERITE_PICKAXE, STONE_PICKAXE, WOODEN_PICKAXE -> {
                                switch (event.getBlock().getType()) {
                                    case DEEPSLATE_COAL_ORE -> {
                                        if (event.isDropItems()) {
                                            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.COAL));
                                        }
                                    }
                                    case DEEPSLATE_COPPER_ORE -> {
                                        if (event.isDropItems()) {
                                            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.RAW_COPPER));
                                        }
                                    }
                                    case DEEPSLATE_IRON_ORE -> {
                                        if (event.isDropItems()) {
                                            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.RAW_IRON));
                                        }
                                    }
                                    case DEEPSLATE_LAPIS_ORE -> {
                                        if (event.isDropItems()) {
                                            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.LAPIS_LAZULI));
                                        }
                                    }
                                    case DEEPSLATE_REDSTONE_ORE -> {
                                        if (event.isDropItems()) {
                                            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.REDSTONE));
                                        }
                                    }
                                    case DEEPSLATE_GOLD_ORE -> {
                                        if (event.isDropItems()) {
                                            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.RAW_GOLD));
                                        }
                                    }
                                    case DEEPSLATE_EMERALD_ORE -> {
                                        if (event.isDropItems()) {
                                            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.EMERALD));
                                        }
                                    }
                                    case DEEPSLATE_DIAMOND_ORE -> {
                                        if (event.isDropItems()) {
                                            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.DIAMOND));
                                        }
                                    }
                                    default -> {
                                    }
                                }
                            }
                            default -> {}
                        }
                    }
                }
            }
        }
    }

    public int getId() {
        return 29;
    }

    public int getDuration(int level) {
        return 0;
    }
}
