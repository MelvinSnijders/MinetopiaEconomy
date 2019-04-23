package nl.themelvin.minetopiaeconomy.vault;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

public interface NoAccountNameEconomy extends Economy {

    @Override
    default boolean hasAccount(String playerName, String accountName) {
        return hasAccount(playerName);
    }

    @Override
    default double getBalance(String playerName, String accountName) {
        return getBalance(playerName);
    }

    @Override
    default boolean has(String playerName, String accountName, double amount) {
        return has(playerName, amount);
    }

    @Override
    default EconomyResponse withdrawPlayer(String playerName, String accountName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    default EconomyResponse depositPlayer(String playerName, String accountName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    default boolean createPlayerAccount(String playerName, String accountName) {
        return createPlayerAccount(playerName);
    }

    @Override
    default boolean createPlayerAccount(String playerName) {
        return true;
    }

    @Override
    default boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return true;
    }

}
