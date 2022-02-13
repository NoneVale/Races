package net.nighthawkempires.races;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import net.nighthawkempires.core.CorePlugin;
import net.nighthawkempires.core.server.ServerType;
import net.nighthawkempires.races.ability.AbilityManager;
import net.nighthawkempires.races.binding.BindingManager;
import net.nighthawkempires.races.commands.BindCommand;
import net.nighthawkempires.races.commands.PerksCommand;
import net.nighthawkempires.races.commands.RacesAdminCommand;
import net.nighthawkempires.races.commands.RacesCommand;
import net.nighthawkempires.races.data.InventoryData;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.enchantment.BlankPotionEnchantment;
import net.nighthawkempires.races.listeners.BindListener;
import net.nighthawkempires.races.listeners.InfectionListener;
import net.nighthawkempires.races.listeners.InventoryListener;
import net.nighthawkempires.races.listeners.PlayerListener;
import net.nighthawkempires.races.listeners.races.*;
import net.nighthawkempires.races.races.RaceManager;
import net.nighthawkempires.races.races.RaceTag;
import net.nighthawkempires.races.recipes.*;
import net.nighthawkempires.races.scoreboard.RaceScoreboard;
import net.nighthawkempires.races.user.registry.MUserRegistry;
import net.nighthawkempires.races.user.registry.UserRegistry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static net.nighthawkempires.core.CorePlugin.getConfigg;
import static net.nighthawkempires.core.CorePlugin.getEnchantmentManager;

public class RacesPlugin extends JavaPlugin {

    private static UserRegistry userRegistry;

    private static AbilityManager abilityManager;
    private static BindingManager bindingManager;
    private static RaceManager raceManager;

    private static InventoryData inventoryData;
    private static PlayerData playerData;

    private static Plugin plugin;

    private static MongoDatabase mongoDatabase;

    public static NamespacedKey BINDER_KEY;
    public static NamespacedKey BINDINGS_KEY;
    public static NamespacedKey CURRENT_BINDING_KEY;

    public static NamespacedKey PERK_INVENTORY_ABILITY;

    // Celestial
    public static NamespacedKey TEAR_OF_GOD;
    public static NamespacedKey TEAR_OF_GOD_RECIPE;

    // DWARF
    public static NamespacedKey GOLDEN_HERITAGE;
    public static NamespacedKey MINERS_TROPHY;
    public static NamespacedKey MINERS_TROPHY_RECIPE;

    // HUMAN
    public static NamespacedKey ELIXIR_OF_LIFE;
    public static NamespacedKey ELIXIR_OF_LIFE_RECIPE;

    // INFERNAL
    public static NamespacedKey INFERNAL_HEART;
    public static NamespacedKey INFERNAL_HEART_RECIPE;

    // Triton
    public static NamespacedKey SEA_PEARL;
    public static NamespacedKey SEA_PEARL_RECIPE;

    // VAMPIRE
    public static NamespacedKey ELIXIR_OF_LIFE_VAMPIRE;
    public static NamespacedKey ELIXIR_OF_LIFE_VAMPIRE_RECIPE;

    // Voidwalker
    public static NamespacedKey VOID_FORGED_PENDANT;
    public static NamespacedKey VOID_FORGED_PENDANT_RECIPE;

    public static Enchantment BLANK_POTION_ENCHANTMENT;

    public void onEnable() {
        plugin = this;
        if (getConfigg().getServerType() != ServerType.SETUP) {
            String pluginName = getPlugin().getName();
            try {
                String hostname = getConfigg().getMongoHostname();
                String database = getConfigg().getMongoDatabase().replaceAll("%PLUGIN%", pluginName);
                String username = getConfigg().getMongoUsername().replaceAll("%PLUGIN%", pluginName);
                String password = getConfigg().getMongoPassword();

                ServerAddress serverAddress = new ServerAddress(hostname);
                MongoCredential mongoCredential = MongoCredential.createCredential(username, database, password.toCharArray());
                mongoDatabase = new MongoClient(serverAddress, mongoCredential, new MongoClientOptions.Builder().build()).getDatabase(database);

                userRegistry = new MUserRegistry(mongoDatabase);

                abilityManager = new AbilityManager();
                bindingManager = new BindingManager();
                raceManager = new RaceManager();

                inventoryData = new InventoryData();
                playerData = new PlayerData();

                getLogger().info("Successfully connected to MongoDB.");

                registerCommands();
                registerEnchantments();
                registerKeys();
                registerListeners();
                registerRecipes();

                CorePlugin.getScoreboardManager().addScoreboard(new RaceScoreboard());
                CorePlugin.getChatFormat().add(new RaceTag());
            } catch (Exception exception) {
                exception.printStackTrace();
                getLogger().warning("Could not connect to MongoDB, shutting plugin down...");
                getServer().getPluginManager().disablePlugin(this);
            }
        }
    }

    private void registerCommands() {
        this.getCommand("bind").setExecutor(new BindCommand());
        this.getCommand("perks").setExecutor(new PerksCommand());
        this.getCommand("racesadmin").setExecutor(new RacesAdminCommand());
        this.getCommand("races").setExecutor(new RacesCommand());
    }

    private void registerEnchantments() {
        getEnchantmentManager().registerEnchantment(new BlankPotionEnchantment());

        BLANK_POTION_ENCHANTMENT = getEnchantmentManager().getEnchantment(plugin, "blank_potion");
    }

    private void registerKeys() {
        BINDER_KEY = new NamespacedKey(this, "bound_to");
        BINDINGS_KEY = new NamespacedKey(this, "binding");
        CURRENT_BINDING_KEY = new NamespacedKey(this, "current_binding");

        PERK_INVENTORY_ABILITY = new NamespacedKey(this, "perk_inventory_ability");

        TEAR_OF_GOD = new NamespacedKey(this, "tear_of_god");
        TEAR_OF_GOD_RECIPE = new NamespacedKey(this, "tear_of_god_recipe");

        GOLDEN_HERITAGE = new NamespacedKey(this, "golden_heritage");
        MINERS_TROPHY = new NamespacedKey(this, "miners_trophy");
        MINERS_TROPHY_RECIPE = new NamespacedKey(this, "miners_trophy_recipe");

        ELIXIR_OF_LIFE = new NamespacedKey(this, "elixir_of_life");
        ELIXIR_OF_LIFE_RECIPE = new NamespacedKey(this, "elixir_of_life_recipe");

        INFERNAL_HEART = new NamespacedKey(this, "infernal_heart");
        INFERNAL_HEART_RECIPE = new NamespacedKey(this, "infernal_heart_recipe");

        SEA_PEARL = new NamespacedKey(this, "sea_pearl");
        SEA_PEARL_RECIPE = new NamespacedKey(this, "sea_pearl_recipe");

        //ELIXIR_OF_LIFE_VAMPIRE = new NamespacedKey(this, "elixir_of_life_vampire");
        //ELIXIR_OF_LIFE_VAMPIRE_RECIPE = new NamespacedKey(this, "elixir_of_life_vampire_recipe");

        VOID_FORGED_PENDANT = new NamespacedKey(this, "void_forged_pendant");
        VOID_FORGED_PENDANT_RECIPE = new NamespacedKey(this, "void_forged_pendant_recipe");
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new BindListener(), this);
        pm.registerEvents(new InfectionListener(), this);
        pm.registerEvents(new InventoryListener(), this);
        pm.registerEvents(new PlayerListener(), this);

        pm.registerEvents(new AngelListener(), this);
        pm.registerEvents(new DwarfListener(), this);
        pm.registerEvents(new HumanListener(), this);
        pm.registerEvents(new InfectionListener(), this);
        //pm.registerEvents(new VampireListener(), this);
        pm.registerEvents(new VoidwalkerListener(), this);
    }

    public void registerRecipes() {
        //Bukkit.addRecipe(new HellForgedDiamond().recipeHellForgedDiamond());
        //Bukkit.addRecipe(new HellForgedDiamond().recipeBeef());

        Bukkit.addRecipe(new CelestialRecipes().recipeTearOfGod());
        Bukkit.addRecipe(new HumanRecipes().recipeElixirOfLife());
        Bukkit.addRecipe(new DwarfRecipes().recipeMinersTrophy());
        Bukkit.addRecipe(new InfernalRecipes().recipeInfernalHeart());
        //Bukkit.addRecipe(new VampireRecipes().recipeElixirOfLifeVampire());
        Bukkit.addRecipe(new VoidwalkerRecipes().recipeVoidForgedPendant());
    }

    public static UserRegistry getUserRegistry() {
        return userRegistry;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static AbilityManager getAbilityManager() {
        return abilityManager;
    }

    public static BindingManager getBindingManager() {
        return bindingManager;
    }

    public static RaceManager getRaceManager() {
        return raceManager;
    }

    public static InventoryData getInventoryData() {
        return inventoryData;
    }

    public static PlayerData getPlayerData() {
        return playerData;
    }
}
