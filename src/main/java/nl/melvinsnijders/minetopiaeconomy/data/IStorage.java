package nl.melvinsnijders.minetopiaeconomy.data;

import nl.melvinsnijders.minetopiaeconomy.profile.Profile;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IStorage {

    /**
     * Initiate a database (create tables, etc.)
     *
     * @param configuration HashMap with configuration items.
     * @return Whether database initiation was complete.
     */
    boolean init(Plugin plugin, HashMap<String, Object> configuration);


    /**
     * Close a database connection.
     * @return Whether the data connection is successfully closed.
     */
    boolean close();

    /**
     * Retrieve a user {@link Profile} from the data storage.
     *
     * @param uuid The UUID of the user to retrieve the data from.
     * @return The stored {@link Profile} of the user.
     */
    CompletableFuture<Profile> retrieveProfile(UUID uuid);

    /**
     * Update an existing {@link Profile} to the data storage.
     *
     * @param profile The {@link Profile} to update in the storage.
     * @return Whether the action has been successfully executed or not.
     */
    CompletableFuture<Boolean> updateProfile(Profile profile);

    /**
     * Insert a new {@link Profile} in the data storage.
     *
     * @param profile The {@link Profile} to insert in the data storage.
     * @return Whether the action has been successfully executed or not.
     */
    CompletableFuture<Boolean> insertProfile(Profile profile);

    /**
     * Check if a {@link Profile} exists for a user.
     *
     * @param uuid The UUID of the user to check if {@link Profile} exists.
     * @return Whether a {@link Profile} exists for the user.
     */
    CompletableFuture<Boolean> existsProfile(UUID uuid);

}
