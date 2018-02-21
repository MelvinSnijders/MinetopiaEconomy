package nl.themelvin.minetopiaeconomy.user;

import java.util.HashMap;
import java.util.UUID;

/**
 * Class for caching UserData, reducing lag
 * @author TheMelvinNL
 */
public class UserDataCache {

    private static UserDataCache instance;

    public HashMap<UUID, UserData> dataCache = new HashMap<>();

    /**
     * Get instance of the class, creates new when none exists
     * @return The instance of UserDataCache
     */
    public static UserDataCache getInstance() {
        if(instance == null) {
            instance = new UserDataCache();
        }
        return instance;
    }

    /**
     * Get UserData from cache
     * @param uuid The UUID of the player to get data from
     * @return UserData Object
     */
    public UserData get(UUID uuid) { return dataCache.get(uuid); }

    /**
     * Remove UserData from cache
     * @param uuid The UUID of the player to remove data from
     */
    public void remove(UUID uuid) { dataCache.remove(uuid); }

    /**
     * Add UserData to cache
     * @param userData The UserData to add
     */
    public void add(UserData userData) { dataCache.put(userData.uuid, userData); }

}
