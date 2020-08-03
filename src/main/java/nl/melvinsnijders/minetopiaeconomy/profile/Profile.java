package nl.melvinsnijders.minetopiaeconomy.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.melvinsnijders.minetopiaeconomy.MinetopiaEconomy;
import nl.melvinsnijders.minetopiaeconomy.data.IStorage;
import nl.melvinsnijders.minetopiaeconomy.utils.Cache;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Profile {

    @Setter private static MinetopiaEconomy plugin;

    @Getter private UUID uuid;
    @Getter @Setter private String userName;
    @Getter @Setter private double balance = 0D;

    public Profile(UUID uuid) {
        this.uuid = uuid;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public static Profile getFromCache(UUID uuid) {

        Cache<UUID, Profile> cache = plugin.getProfileCache();
        return cache.get(uuid);

    }

    public CompletableFuture<Boolean> save(boolean toDatabase) {

        Cache<UUID, Profile> cache = plugin.getProfileCache();
        cache.put(this.uuid, this);

        if(toDatabase) {

            return this.saveToDatabase();

        } else {

            return CompletableFuture.completedFuture(true);

        }

    }

    public CompletableFuture<Boolean> saveToDatabase() {
        IStorage storage = plugin.getDataStorage();
        return storage.updateProfile(this);
    }

    public CompletableFuture<Boolean> save() {
        return this.save(false);
    }

}
