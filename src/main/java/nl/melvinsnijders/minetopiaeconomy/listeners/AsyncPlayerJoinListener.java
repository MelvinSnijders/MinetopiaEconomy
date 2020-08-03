package nl.melvinsnijders.minetopiaeconomy.listeners;

import lombok.SneakyThrows;
import nl.melvinsnijders.minetopiaeconomy.MinetopiaEconomy;
import nl.melvinsnijders.minetopiaeconomy.data.IStorage;
import nl.melvinsnijders.minetopiaeconomy.data.migration.Migration;
import nl.melvinsnijders.minetopiaeconomy.data.migration.MigrationType;
import nl.melvinsnijders.minetopiaeconomy.profile.Profile;
import nl.melvinsnijders.minetopiaeconomy.utils.Cache;
import nl.melvinsnijders.minetopiaeconomy.utils.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AsyncPlayerJoinListener extends Listener<AsyncPlayerPreLoginEvent> {

    public AsyncPlayerJoinListener(MinetopiaEconomy plugin) {
        super(plugin);
    }

    /**
     * Method executed on event call.
     *
     * @param event The event to execute.
     */
    @EventHandler
    public void execute(AsyncPlayerPreLoginEvent event) {

        UUID uuid = event.getUniqueId();
        IStorage dataStorage = this.plugin.getDataStorage();
        Cache<UUID, Profile> cache = this.plugin.getProfileCache();

        dataStorage.existsProfile(uuid).thenCompose((exists) -> {
            if(!exists) {
                Profile profile = new Profile(uuid);
                cache.put(uuid, profile);
                return dataStorage.insertProfile(profile);
            }
            return CompletableFuture.completedFuture(false);
        }).thenComposeAsync(inserted -> {
            if(inserted) {
                Logger.log(Logger.Severity.DEBUG, "Inserted new profile with uuid " + uuid + " in data storage.");
                return null;
            } else {
                return dataStorage.retrieveProfile(uuid);
            }
        }).thenAcceptAsync(profile -> {
            if(profile != null) {
                cache.put(uuid, profile);
            } else {
                profile = cache.get(uuid);

                // Try migrating old data to this plugin.
                double restored = this.migrateData(uuid);
                profile.setBalance(restored);
                profile.save(true);
            }
        });

    }

    @SneakyThrows
    private double migrateData(UUID uuid) {

        if(this.plugin.getMigrationType() == MigrationType.NONE) {
            return 0D;
        }

        Constructor<Migration> constructor = Migration.class.getConstructor(UUID.class);
        Migration migration = constructor.newInstance(uuid);

        return migration.restore();

    }

}
