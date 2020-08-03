package nl.melvinsnijders.minetopiaeconomy.data.types;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import nl.melvinsnijders.minetopiaeconomy.data.IStorage;
import nl.melvinsnijders.minetopiaeconomy.profile.Profile;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class JSON implements IStorage {

    private Plugin plugin;
    private Gson gson = new Gson();

    /**
     * Initiate a database (create tables, etc.)
     *
     * @param configuration HashMap with configuration items.
     * @return Whether database initiation was complete.
     */
    @Override
    public boolean init(Plugin plugin, HashMap<String, Object> configuration) {

        this.plugin = plugin;

        File file = new File(this.plugin.getDataFolder() + "/data");
        return file.mkdir();

    }

    /**
     * Close a database connection.
     *
     * @return Whether the data connection is succesfully closed.
     */
    @Override
    public boolean close() {
        // Nothing to close
        return true;
    }

    /**
     * Retrieve a user {@link Profile} from the data storage.
     *
     * @param uuid The UUID of the user to retrieve the data from.
     * @return The stored {@link Profile} of the user.
     */
    @Override
    public CompletableFuture<Profile> retrieveProfile(UUID uuid) {

        return CompletableFuture.supplyAsync(() -> {

            try {

                File file = this.getFile(uuid);
                FileReader reader = new FileReader(file);
                return this.gson.fromJson(reader, Profile.class);

            } catch (FileNotFoundException exception) {
                return null;
            }

        });

    }

    /**
     * Update an existing {@link Profile} to the data storage.
     *
     * @param profile The {@link Profile} to update in the storage.
     * @return Whether the action has been successfully executed or not.
     */
    @Override
    public CompletableFuture<Boolean> updateProfile(Profile profile) {
        // Updating is the same process.
        return this.insertProfile(profile);
    }

    /**
     * Insert a new {@link Profile} in the data storage.
     *
     * @param profile The {@link Profile} to insert in the data storage.
     * @return Whether the action has been successfully executed or not.
     */
    @Override
    public CompletableFuture<Boolean> insertProfile(Profile profile) {

        return CompletableFuture.supplyAsync(() -> {

            try {

                String json = this.gson.toJson(profile);
                File file = this.getFile(profile.getUuid());

                if(!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(json);
                fileWriter.close();

                return true;

            } catch (Exception exception) {
                return false;
            }

        });

    }

    /**
     * Check if a {@link Profile} exists for a user.
     *
     * @param uuid The UUID of the user to check if {@link Profile} exists.
     * @return Whether a {@link Profile} exists for the user.
     */
    @Override
    public CompletableFuture<Boolean> existsProfile(UUID uuid) {

        return CompletableFuture.supplyAsync(() -> this.getFile(uuid).exists());

    }

    private File getFile(UUID uuid) {
        return new File(this.plugin.getDataFolder() + "/data/" + uuid + ".json");
    }

}
