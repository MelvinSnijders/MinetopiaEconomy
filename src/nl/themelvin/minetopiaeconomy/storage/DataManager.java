package nl.themelvin.minetopiaeconomy.storage;

import nl.themelvin.minetopiaeconomy.Main;
import nl.themelvin.minetopiaeconomy.storage.database.MySQL;
import nl.themelvin.minetopiaeconomy.user.UserData;
import nl.themelvin.minetopiaeconomy.user.UserDataCache;
import nl.themelvin.minetopiaeconomy.utils.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Class / Util for saving and loading data
 */
public class DataManager {

    /**
     * Reads UserData from database or file storage
     * @param player The UUID to read the data from
     * @param cache Whether the plugin should cache the data or not
     * @return The UserData of the player
     */
    public static UserData loadData(UUID player, boolean cache) {

        if(Main.storageType == StorageType.FILE) {
            // TODO file storage type
        }

        if(Main.storageType == StorageType.MYSQL) {
            try {
                PreparedStatement pst = MySQL.getConnection().prepareStatement("SELECT * FROM `UserData` WHERE `UUID` = ?;");
                pst.setString(1, player.toString());
                ResultSet res = pst.executeQuery();
                if(res.next()) {
                    // User has account, load from database
                    UserData userData = new UserData(player, res.getString("username"), res.getDouble("money"));
                    if(cache) {
                        UserDataCache.getInstance().add(userData);
                    }
                    MySQL.close();
                    return userData;
                } else {
                    // User doesn't have account, create new cache (if cache = true), saving will occur on leave
                    UserData userData = new UserData(player, null, 0D);
                    if(cache) {
                        UserDataCache.getInstance().add(userData);
                    }
                    MySQL.close();
                    return userData;
                }
            } catch(SQLException e) {
                Logger.consoleOutput(Logger.InfoLevel.ERROR, "Er ging iets fout tijdens het laden van de data voor " + player.toString());
            }
        }
        return null;
    }

    /**
     * Saves cached data
     * @param player The UUID for the UserData to save
     */
    public static void saveCachedData(UUID player) {

        if(Main.storageType == StorageType.FILE) {
            // TODO file storage type
        }

        if(Main.storageType == StorageType.MYSQL) {
            try {
                PreparedStatement pst = MySQL.getConnection().prepareStatement("SELECT * FROM `UserData` WHERE `UUID` = ?;");
                pst.setString(1, player.toString());
                ResultSet res = pst.executeQuery();
                UserData cachedData = UserDataCache.getInstance().get(player);
                if(res.next()) {
                    // User has account, update guery
                    pst = MySQL.getConnection().prepareStatement("UPDATE `UserData` SET `username` = ?, `money` = ? WHERE `UUID` = ?;");
                    pst.setString(1, cachedData.name);
                    pst.setDouble(2, cachedData.money);
                    pst.setString(3, player.toString());
                    pst.executeUpdate();
                    UserDataCache.getInstance().remove(player);
                    MySQL.close();
                } else {
                    // User doesn't have account, insert query
                    pst = MySQL.getConnection().prepareStatement("INSERT INTO `UserData` (`UUID`, `username`, `money`) VALUES (?, ?, ?);");
                    pst.setString(1, player.toString());
                    pst.setString(2, cachedData.name);
                    pst.setDouble(3, cachedData.money);
                    pst.executeUpdate();
                    UserDataCache.getInstance().remove(player);
                    MySQL.close();
                }
            } catch(SQLException e) {
                Logger.consoleOutput(Logger.InfoLevel.ERROR, "Er ging iets fout tijdens het opslaan van de data voor " + player.toString());
            }
        }

    }

}
