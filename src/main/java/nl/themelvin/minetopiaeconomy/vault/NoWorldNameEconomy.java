package nl.themelvin.minetopiaeconomy.vault;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

public interface NoWorldNameEconomy extends Economy {

    @Override
    default boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
        return hasAccount(offlinePlayer);
    }

    @Override
    default double getBalance(OfflinePlayer offlinePlayer, String worldName) {
        return getBalance(offlinePlayer);
    }

    @Override
    default boolean has(OfflinePlayer offlinePlayer, String worldName, double money) {
        return has(offlinePlayer, money);
    }

    @Override
    default EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String worldName, double money) {
        return withdrawPlayer(offlinePlayer, money);
    }

    @Override
    default EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String worldName, double money) {
        return depositPlayer(offlinePlayer, money);
    }

    @Override
    default boolean createPlayerAccount(OfflinePlayer offlinePlayer, String worldName) {
        return false;
    }

}
