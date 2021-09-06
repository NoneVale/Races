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
import net.nighthawkempires.races.commands.RacesCommand;
import net.nighthawkempires.races.data.PlayerData;
import net.nighthawkempires.races.listeners.BindListener;
import net.nighthawkempires.races.listeners.InfectionListener;
import net.nighthawkempires.races.listeners.PlayerListener;
import net.nighthawkempires.races.listeners.races.VoidwalkerListener;
import net.nighthawkempires.races.races.RaceManager;
import net.nighthawkempires.races.races.RaceTag;
import net.nighthawkempires.races.recipes.HellForgedDiamond;
import net.nighthawkempires.races.recipes.VoidwalkerRecipes;
import net.nighthawkempires.races.scoreboard.RaceScoreboard;
import net.nighthawkempires.races.user.registry.MUserRegistry;
import net.nighthawkempires.races.user.registry.UserRegistry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static net.nighthawkempires.core.CorePlugin.getConfigg;

public class RacesPlugin extends JavaPlugin {

    private static UserRegistry userRegistry;

    private static AbilityManager abilityManager;
    private static BindingManager bindingManager;
    private static RaceManager raceManager;

    private static PlayerData playerData;

    private static Plugin plugin;

    private static MongoDatabase mongoDatabase;

    public static NamespacedKey BINDER_KEY;
    public static NamespacedKey BINDINGS_KEY;
    public static NamespacedKey CURRENT_BINDING_KEY;

    public static NamespacedKey VOID_FORGED_PENDANT;
    public static NamespacedKey VOID_FORGED_PENDANT_RECIPE;

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

                playerData = new PlayerData();

                getLogger().info("Successfully connected to MongoDB.");

                registerCommands();
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
        this.getCommand("races").setExecutor(new RacesCommand());
    }

    private void registerKeys() {
        BINDER_KEY = new NamespacedKey(this, "bound_to");
        BINDINGS_KEY = new NamespacedKey(this, "binding");
        CURRENT_BINDING_KEY = new NamespacedKey(this, "current_binding");

        VOID_FORGED_PENDANT = new NamespacedKey(this, "void_forged_pendant");
        VOID_FORGED_PENDANT_RECIPE = new NamespacedKey(this, "void_forged_pendant_recipe");
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new BindListener(), this);
        pm.registerEvents(new InfectionListener(), this);
        pm.registerEvents(new PlayerListener(), this);

        pm.registerEvents(new VoidwalkerListener(), this);
    }

    public void registerRecipes() {
        Bukkit.addRecipe(new HellForgedDiamond().recipeHellForgedDiamond());
        Bukkit.addRecipe(new HellForgedDiamond().recipeBeef());

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

    public static PlayerData getPlayerData() {
        return playerData;
    }
}
