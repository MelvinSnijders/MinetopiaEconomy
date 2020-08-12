package nl.melvinsnijders.minetopiaeconomy;

import lombok.Getter;
import lombok.SneakyThrows;
import net.milkbowl.vault.economy.Economy;
import nl.melvinsnijders.minetopiaeconomy.commands.MoneyCommand;
import nl.melvinsnijders.minetopiaeconomy.data.IStorage;
import nl.melvinsnijders.minetopiaeconomy.data.StorageType;
import nl.melvinsnijders.minetopiaeconomy.data.migration.MigrationType;
import nl.melvinsnijders.minetopiaeconomy.listeners.AsyncPlayerJoinListener;
import nl.melvinsnijders.minetopiaeconomy.listeners.Listener;
import nl.melvinsnijders.minetopiaeconomy.listeners.PlayerJoinListener;
import nl.melvinsnijders.minetopiaeconomy.listeners.PlayerQuitListener;
import nl.melvinsnijders.minetopiaeconomy.profile.Profile;
import nl.melvinsnijders.minetopiaeconomy.utils.Cache;
import nl.melvinsnijders.minetopiaeconomy.utils.Logger;
import nl.melvinsnijders.minetopiaeconomy.utils.Messages;
import nl.melvinsnijders.minetopiaeconomy.utils.Statistics;
import nl.melvinsnijders.minetopiaeconomy.vault.EconomyHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class MinetopiaEconomy extends JavaPlugin {

    @Getter private IStorage dataStorage;
    @Getter private Cache<UUID, Profile> profileCache;
    @Getter private MigrationType migrationType;
    private Statistics statistics;

    private Map<String, Class<? extends nl.melvinsnijders.minetopiaeconomy.commands.Command>> commands = new HashMap<>();

    @Override
    @SneakyThrows
    public void onEnable() {

        Logger.log(Logger.Severity.INFO, "MinetopiaEconomy v" + this.getDescription().getVersion() + " wordt gestart...");

        this.saveDefaultConfig();

        Configuration configuration = this.getConfig();
        String storageString = configuration.getString("storage").toUpperCase();

        // Check if selected storage type is valid.
        if(Arrays.stream(StorageType.values()).noneMatch(e -> e.toString().equals(storageString))) {

            Logger.log(Logger.Severity.ERROR, "Opslag type " + storageString + " is niet geldig. Controleer de 'storage' waarde in het configuratiebestand. Geldige types: " + Arrays.stream(StorageType.values()).map(Enum::toString).collect(Collectors.joining(", ")));
            this.getPluginLoader().disablePlugin(this);

            return;

        }

        StorageType storageType = StorageType.valueOf(storageString);

        Logger.log(Logger.Severity.INFO, "Opslag type " + storageString + " geselecteerd, initialiseren...");

        // Retrieve storage configuration
        HashMap<String, Object> storedConfig = new HashMap<>();

        if(storageType.getConfigKey() != null) {
            storedConfig = new HashMap<>(configuration.getConfigurationSection(storageType.getConfigKey()).getValues(true));
        }

        // Initiate storage class.
        this.dataStorage = storageType.getStorageClass().newInstance();
        this.dataStorage.init(this, storedConfig);

        // Setup cache and profile class
        Profile.setPlugin(this);
        this.profileCache = new Cache<>();

        // Check and set data migration type
        String migrationString = configuration.getString("data-migration").toUpperCase();

        if(Arrays.stream(MigrationType.values()).noneMatch(e -> e.toString().equals(migrationString))) {

            Logger.log(Logger.Severity.ERROR, "Data migration type " + storageString + " is niet geldig. Controleer de 'data-migration' waarde in het configuratiebestand. Geldige types: " + Arrays.stream(MigrationType.values()).map(Enum::toString).collect(Collectors.joining(", ")));
            this.getPluginLoader().disablePlugin(this);

            return;

        }

        this.migrationType = MigrationType.valueOf(migrationString);

        // Setup vault hook
        EconomyHandler.setPlugin(this);

        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            Logger.log(Logger.Severity.WARNING, "Vault is niet in deze server geinstalleerd. Deze plugin werkt ook zonder Vault, maar dat is niet aangeraden.");
        } else {
            Bukkit.getServicesManager().register(Economy.class, new EconomyHandler(), this, ServicePriority.Highest);
        }

        // Initiate messages
        Messages.setPlugin(this);
        Messages.init();

        // Register listeners
        this.registerListener(AsyncPlayerJoinListener.class);
        this.registerListener(PlayerJoinListener.class);
        this.registerListener(PlayerQuitListener.class);

        // Register commands
        this.registerCommand("money", MoneyCommand.class);

        // Reload data (fake join) for online players, because people are still reloading servers in production...
        for(Player online : Bukkit.getOnlinePlayers()) {
            new AsyncPlayerJoinListener(this).execute(new AsyncPlayerPreLoginEvent(null, null, online.getUniqueId()));
        }

        // Setup statistics
        this.statistics = new Statistics(this);

        // Startup success.
        Logger.log(Logger.Severity.INFO, "Plugin is opgestart, bedankt voor het gebruiken!");

    }

    @Override
    public void onDisable() {

        this.statistics.shutdown();

        for(Player online : Bukkit.getOnlinePlayers()) {
            new PlayerQuitListener(this).execute(new PlayerQuitEvent(online, null));
        }

        this.dataStorage.close();
        this.profileCache.clear();

    }

    /**
     * Method for registering a listener to the plugin.
     *
     * @param clazz The class of the listener to register.
     */

    protected void registerListener(Class<? extends Listener<?>> clazz) {

        try {

            Constructor<? extends Listener<?>> constructor = clazz.getConstructor(MinetopiaEconomy.class);
            Listener<?> listener = constructor.newInstance(this);

            this.getServer().getPluginManager().registerEvents(listener, this);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if (!this.commands.containsKey(alias)) {

            return false;

        }

        try {

            Class<?> clazz = this.commands.get(alias);
            Constructor<?> constructor = clazz.getConstructor(MinetopiaEconomy.class, CommandSender.class, String.class, String[].class);
            nl.melvinsnijders.minetopiaeconomy.commands.Command object = (nl.melvinsnijders.minetopiaeconomy.commands.Command) constructor.newInstance(this, sender, alias, args);
            object.execute();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    /**
     * Register command to command register.
     *
     * @param command Alias of the command
     * @param clazz   Cclass instance of the executor
     */

    private void registerCommand(String command, Class<? extends nl.melvinsnijders.minetopiaeconomy.commands.Command> clazz) {

        this.getCommand(command).setExecutor(this);
        this.commands.put(command, clazz);

        List<String> aliases = this.getCommand(command).getAliases();

        for (String alias : aliases) {

            this.commands.put(alias, clazz);

        }

    }

}
