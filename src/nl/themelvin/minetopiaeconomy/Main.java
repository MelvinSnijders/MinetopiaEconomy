package nl.themelvin.minetopiaeconomy;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import nl.themelvin.minetopiaeconomy.economy.EconomyHandler;
import nl.themelvin.minetopiaeconomy.listeners.DefaultPlayerListener;
import nl.themelvin.minetopiaeconomy.storage.DataManager;
import nl.themelvin.minetopiaeconomy.storage.StorageType;
import nl.themelvin.minetopiaeconomy.storage.database.MySQL;
import nl.themelvin.minetopiaeconomy.storage.file.FileManager;
import nl.themelvin.minetopiaeconomy.user.UserData;
import nl.themelvin.minetopiaeconomy.user.UserDataCache;
import nl.themelvin.minetopiaeconomy.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    private static Plugin plugin;

    public static StorageType storageType;

    public static FileConfiguration dataFile;
    public static FileConfiguration messageFile;

    @Override
    public void onEnable() {

        long startMillis = System.currentTimeMillis();

        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("com.microsoft.sqlserver.jdbc");
        logger.setLevel(Level.OFF);

        plugin = this;

        saveDefaultConfig();

        Logger.consoleOutput(Logger.InfoLevel.INFO, "MinetopiaEconomy inschakelen...");

        if(Bukkit.getPluginManager().getPlugin("Vault") == null) {
            Logger.consoleOutput(Logger.InfoLevel.ERROR, "De Vault plugin staat niet in je server, plugin uitgeschakeld!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getPluginManager().registerEvents(new DefaultPlayerListener(), this);

        String foundStorage = getConfig().getString("storage");

        // Checking what storage type is configured
        if(foundStorage.equalsIgnoreCase("mysql")) {
            storageType = StorageType.MYSQL;
        } else if(foundStorage.equalsIgnoreCase("file")) {
            storageType = StorageType.FILE;
        } else {
            storageType = StorageType.FILE;
            Logger.consoleOutput(Logger.InfoLevel.WARNING, "Geen geldig opslagtype gevonden, file opslag geselecteerd!");
        }

        Logger.consoleOutput(Logger.InfoLevel.INFO, "Gekozen opslagtype is " + storageType.toString() + ".");

        if(storageType == StorageType.MYSQL) {
            // Trying to connect to database and creating table if not exists
            if(MySQL.getConnection() != null) {
                try {
                    MySQL.getConnection().createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `UserData` (" +
                            "  `userID` INT NOT NULL AUTO_INCREMENT," +
                            "  `UUID` VARCHAR(36) NOT NULL," +
                            "  `username` VARCHAR(16)," +
                            "  `money` DOUBLE NOT NULL," +
                            "  PRIMARY KEY (`userID`));");
                    MySQL.close();
                } catch (SQLException e) {
                    Logger.consoleOutput(Logger.InfoLevel.ERROR, "Er ging iets fout tijdens het aanmaken van de table, plugin uitgeschakeld.");
                    e.printStackTrace();
                    MySQL.close();
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
            } else {
                return;
            }

            if(Bukkit.getOnlinePlayers().size() > 0) {
                Logger.consoleOutput(Logger.InfoLevel.WARNING, "Reload gedetecteerd, probeer je server zo min mogelijk te reloaden!");
            }

        }

        messageFile = FileManager.loadFile("", "messages.yml");

        if(storageType == StorageType.FILE) {
            dataFile = FileManager.loadFile("data", "userdata.yml");
            if(dataFile == null) {
                return;
            }
            Logger.consoleOutput(Logger.InfoLevel.INFO, "Het data bestand is aangemaakt.");
        }

        if(messageFile == null) {
            return;
        }

        // Loading data for all online players (when server is reloaded)
        for(Player on : Bukkit.getOnlinePlayers()) {
            DataManager.loadData(on.getUniqueId(), true);
            UserDataCache.getInstance().get(on.getUniqueId()).name = on.getName();
            Logger.consoleOutput(Logger.InfoLevel.INFO, "Data geladen voor " + on.getName() + " (" + on.getUniqueId() + ")");
        }

        Logger.consoleOutput(Logger.InfoLevel.INFO, "In Vault hooken...");

        // Overriding Essentials Economy and registering this plugin as Economy plugin
        Bukkit.getServicesManager().register(Economy.class, new EconomyHandler(this), Bukkit.getPluginManager().getPlugin("Vault"), ServicePriority.Highest);

        Logger.consoleOutput(Logger.InfoLevel.INFO, "Succesvol geregistreerd in Vault plugin.");

        Logger.consoleOutput(Logger.InfoLevel.INFO, "De MinetopiaEconomy plugin is ingeschakeld! Het duurde " + (System.currentTimeMillis() - startMillis) + "ms. Gemaakt door TheMelvinNL");

    }

    @Override
    public void onDisable() {

        plugin = null;

        if(storageType == StorageType.MYSQL) {
            // Saving data for all online players (abnormal server behaviour)
            for(Player on : Bukkit.getOnlinePlayers()) {
                DataManager.saveCachedData(on.getUniqueId());
            }
        }

    }

    /**
     * Method to get Plugin
     * @return The plugin
     */
    public static Plugin getPlugin() {
        return plugin;
    }

}
