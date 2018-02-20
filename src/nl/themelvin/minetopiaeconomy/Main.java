package nl.themelvin.minetopiaeconomy;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import nl.themelvin.minetopiaeconomy.economy.EconomyHandler;
import nl.themelvin.minetopiaeconomy.storage.StorageType;
import nl.themelvin.minetopiaeconomy.storage.database.MySQL;
import nl.themelvin.minetopiaeconomy.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin {

    private static Plugin plugin;

    public static StorageType storageType;

    @Override
    public void onEnable() {

        plugin = this;

        saveDefaultConfig();

        if(Bukkit.getPluginManager().getPlugin("Vault") == null) {
            Logger.consoleOutput(Logger.InfoLevel.ERROR, "De Vault plugin staat niet in je server, plugin uitgeschakeld!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

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

        if(storageType == StorageType.MYSQL) {
            // Trying to connect to database and creating table if not exists
            if(MySQL.getConnection() != null) {
                try {
                    MySQL.getConnection().createStatement().executeQuery("CREATE TABLE IF NOT EXISTS `UserData` (" +
                            "  `userID` INT NOT NULL AUTO_INCREMENT," +
                            "  `UUID` VARCHAR(36) NOT NULL," +
                            "  `username` VARCHAR(16) NOT NULL," +
                            "  `money` DOUBLE NOT NULL," +
                            "  PRIMARY KEY (`userID`));");
                } catch (SQLException e) {
                    Logger.consoleOutput(Logger.InfoLevel.WARNING, "Er ging iets fout tijdens het aanmaken van de table");
                    e.printStackTrace();
                }
                MySQL.close();
            }
        }

        // Overriding Essentials Economy and registering this plugin as Economy plugin
        Bukkit.getServicesManager().register(Economy.class, new EconomyHandler(this), new Vault(), ServicePriority.Highest);

    }

    @Override
    public void onDisable() {

        plugin = null;

    }

    /**
     * Method to get Plugin
     * @return The plugin
     */
    public static Plugin getPlugin() {
        return plugin;
    }

}
