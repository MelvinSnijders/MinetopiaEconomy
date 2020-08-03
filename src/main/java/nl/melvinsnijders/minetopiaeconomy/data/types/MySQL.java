package nl.melvinsnijders.minetopiaeconomy.data.types;

import com.github.jasync.sql.db.Connection;
import com.github.jasync.sql.db.ConnectionPoolConfiguration;
import com.github.jasync.sql.db.ConnectionPoolConfigurationBuilder;
import com.github.jasync.sql.db.mysql.MySQLConnectionBuilder;
import lombok.SneakyThrows;
import nl.melvinsnijders.minetopiaeconomy.data.IStorage;
import nl.melvinsnijders.minetopiaeconomy.profile.Profile;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MySQL implements IStorage {

    private Plugin plugin;
    private Connection connection;

    /**
     * Initiate a database (create tables, etc.)
     *
     * @return Whether database initiation was complete.
     */
    @Override
    public boolean init(Plugin plugin, HashMap<String, Object> storedConfiguration) {

        this.plugin = plugin;

        ConnectionPoolConfigurationBuilder configurationBuilder = new ConnectionPoolConfigurationBuilder();
        configurationBuilder.setHost((String) storedConfiguration.get("host"));
        configurationBuilder.setPort((Integer) storedConfiguration.get("port"));
        configurationBuilder.setDatabase((String) storedConfiguration.get("database"));
        configurationBuilder.setUsername((String) storedConfiguration.get("username"));
        configurationBuilder.setPassword((String) storedConfiguration.get("password"));

        ConnectionPoolConfiguration configuration = configurationBuilder.build();
        this.connection = MySQLConnectionBuilder.createConnectionPool(configuration);

        return this.connection.isConnected();

    }

    /**
     * Close a database connection.
     *
     * @return Whether the data connection is succesfully closed.
     */
    @Override
    @SneakyThrows
    public boolean close() {
        return !this.connection.disconnect().get().isConnected();
    }

    /**
     * Retrieve a user {@link Profile} from the data storage.
     *
     * @param uuid The UUID of the user to retrieve the data from.
     * @return The stored {@link Profile} of the user.
     */
    @Override
    public CompletableFuture<Profile> retrieveProfile(UUID uuid) {
        return null;
    }

    /**
     * Update an existing {@link Profile} to the data storage.
     *
     * @param profile The {@link Profile} to update in the storage.
     * @return Whether the action has been successfully executed or not.
     */
    @Override
    public CompletableFuture<Boolean> updateProfile(Profile profile) {
        return null;
    }

    /**
     * Insert a new {@link Profile} in the data storage.
     *
     * @param profile The {@link Profile} to insert in the data storage.
     * @return Whether the action has been successfully executed or not.
     */
    @Override
    public CompletableFuture<Boolean> insertProfile(Profile profile) {
        return null;
    }

    /**
     * Check if a {@link Profile} exists for a user.
     *
     * @param uuid The UUID of the user to check if {@link Profile} exists.
     * @return Whether a {@link Profile} exists for the user.
     */
    @Override
    public CompletableFuture<Boolean> existsProfile(UUID uuid) {
        return null;
    }

}
