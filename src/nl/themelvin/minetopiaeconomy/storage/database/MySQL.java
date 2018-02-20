package nl.themelvin.minetopiaeconomy.storage.database;

import nl.themelvin.minetopiaeconomy.Main;
import nl.themelvin.minetopiaeconomy.utils.Logger;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class to control SQL database connections
 */
public class MySQL {

    private static final String hostname = Main.getPlugin().getConfig().getString("database.host");
    private static final String database = Main.getPlugin().getConfig().getString("database.database");
    private static final String username = Main.getPlugin().getConfig().getString("database.username");
    private static final String password = Main.getPlugin().getConfig().getString("database.password");

    private static Connection connection = null;

    /**
     * Creates a MySQL database and returns the connection
     * @return The database connection instance
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:mysql://" + hostname + "/" + database, username, password);
            }
            return connection;
        } catch (ClassNotFoundException exception) {
            Logger.consoleOutput(Logger.InfoLevel.ERROR, "JDBC MySQL driver niet gevonden, plugin wordt uitgeschakeld...");
            Bukkit.getPluginManager().disablePlugin(Main.getPlugin());
            return null;
        } catch(SQLException e) {
            Logger.consoleOutput(Logger.InfoLevel.ERROR, "Fout tijdens het verbinden met de database, plugin wordt uitgeschakeld...");
            Bukkit.getPluginManager().disablePlugin(Main.getPlugin());
            return null;
        }
    }

    /**
     * Closes the connection with the MySQL database
     */
    public static void close() {
        try {
            connection.close();
            connection = null;
        } catch (Exception e) {
            Logger.consoleOutput(Logger.InfoLevel.ERROR, "Fout tijdens het sluiten van de MySQL connectie, contacteer TheMelvinNL");

        }
    }

}
