package net.nighthawkempires.races.listeners.races;

import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.races.RacesPlugin;
import net.nighthawkempires.races.ability.Ability;
import net.nighthawkempires.races.binding.BindingManager;
import net.nighthawkempires.races.event.AbilityUnlockEvent;
import net.nighthawkempires.races.event.RaceChangeEvent;
import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;
import net.nighthawkempires.races.recipes.HumanRecipes;
import net.nighthawkempires.races.user.UserModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class HumanListener implements Listener {

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (!event.getItem().isSimilar(HumanRecipes.itemElixirOfLife())) return;

        if (userModel.getRace().getRaceType() == RaceType.HUMAN) {
            player.sendMessage(CorePlugin.getMessages().getChatMessage(ChatColor.RED + "You are already human, so there's no purpose in drinking the Elixir of Life."));
            event.setCancelled(true);
        } else {
            Race race = RacesPlugin.getRaceManager().getRace(RaceType.HUMAN, 1);
            Bukkit.getPluginManager().callEvent(new RaceChangeEvent(player, race));

            player.playSound(player, Sound.ENTITY_PLAYER_BURP, 1f, 0.5f);
            player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1f, 0.5f);
        }
    }

    @EventHandler
    public void onPrepare(PrepareItemCraftEvent event) {
        /**if (event.getRecipe() != null && event.getRecipe().getResult().isSimilar(HumanRecipes.itemElixirOfLife())) {
            event.getInventory().setResult(null);
            if (event.getInventory().getMatrix()[4].getType() == Material.POTION) {
                ItemStack itemStack = event.getInventory().getMatrix()[4];

                if (itemStack.getItemMeta() instanceof PotionMeta) {
                    PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
                    PotionData potionData = potionMeta.getBasePotionData();

                    if (potionData.getType() == PotionType.REGEN && potionData.isUpgraded()) {
                        event.getInventory().setResult(HumanRecipes.itemElixirOfLife());
                    }
                }
            }
        }*/
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        RacesPlugin.getAbilityManager().getAbility(31).run(event);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        RacesPlugin.getAbilityManager().getAbility(35).run(event);
        RacesPlugin.getAbilityManager().getAbility(36).run(event);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        RacesPlugin.getAbilityManager().getAbility(33).run(event);
        RacesPlugin.getAbilityManager().getAbility(34).run(event);
        RacesPlugin.getAbilityManager().getAbility(39).run(event);
    }

    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent event) {
        RacesPlugin.getAbilityManager().getAbility(37).run(event);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.getRace().getRaceType() == RaceType.HUMAN) {
            RacesPlugin.getAbilityManager().getAbility(32).run(event);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (userModel.getRace().getRaceType() == RaceType.HUMAN) {
            RacesPlugin.getAbilityManager().getAbility(32).run(event);
        }
    }

    @EventHandler
    public void onUnlockAbility(AbilityUnlockEvent event) {
        Player player = event.getPlayer();
        UserModel userModel = RacesPlugin.getUserRegistry().getUser(player.getUniqueId());

        if (event.getAbility().getRaceType() == RaceType.HUMAN) {
            if (event.getAbility().getAbilityType() == Ability.AbilityType.PASSIVE) {
                event.getAbility().run(event);
            }
        }
    }
}
