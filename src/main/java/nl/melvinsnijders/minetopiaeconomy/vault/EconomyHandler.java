package nl.melvinsnijders.minetopiaeconomy.vault;

import lombok.Setter;
import lombok.SneakyThrows;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import nl.melvinsnijders.minetopiaeconomy.MinetopiaEconomy;
import nl.melvinsnijders.minetopiaeconomy.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class EconomyHandler implements Economy, NoAccountNameEconomy, NoBankEconomy, NoWorldNameEconomy {

    @Setter private static MinetopiaEconomy plugin;

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public String getName() {
        return plugin.getName();
    }

    /**
     * Some economy plugins round off after a certain number of digits.
     * This function returns the number of digits the plugin keeps
     * or -1 if no rounding occurs.
     *
     * @return Number of digits after the decimal point kept.
     */

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return NumberFormat.getNumberInstance(Locale.GERMAN).format(v);
    }

    @Override
    public String currencyNamePlural() {
        return "Euro";
    }

    @Override
    public String currencyNameSingular() {
        return "€";
    }

    /**
     * Checks if this player has an account on the server yet.
     * Developer is fully responsible for using this Async.
     *
     * @param playerName Player to check.
     * @return If the player has an account.
     */

    @Override
    public boolean hasAccount(String playerName) {

        return this.hasAccount(Bukkit.getOfflinePlayer(playerName));

    }

    @Override
    @SneakyThrows
    public boolean hasAccount(OfflinePlayer offlinePlayer) {

        if(offlinePlayer.isOnline()) {
            return true;
        }

        return plugin.getDataStorage().existsProfile(offlinePlayer.getUniqueId()).get();

    }

    @Override
    public double getBalance(String playerName) {
        return getBalance(Bukkit.getOfflinePlayer(playerName));
    }

    @Override
    @SneakyThrows
    public double getBalance(OfflinePlayer offlinePlayer) {

        Profile profile = Profile.getFromCache(offlinePlayer.getUniqueId());

        if(profile == null) {
            profile = plugin.getDataStorage().retrieveProfile(offlinePlayer.getUniqueId()).get();
        }

        return profile.getBalance();

    }

    @Override
    public boolean has(String playerName, double money) {
        return this.getBalance(playerName) >= money;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double money) {
        return this.getBalance(offlinePlayer) >= money;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double money) {
        return this.withdrawPlayer(Bukkit.getOfflinePlayer(playerName), money);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double money) {

        if (money < 0) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Je kan geen negatieve bedragen afnemen.");
        }

        Profile profile = Profile.getFromCache(offlinePlayer.getUniqueId());

        if(profile == null) {
            // Retrieve and save to data storage.
            plugin.getDataStorage().retrieveProfile(offlinePlayer.getUniqueId()).thenApply(dbProfile -> {
                dbProfile.withdraw(money);
                return dbProfile;
            }).thenComposeAsync(dbProfile -> plugin.getDataStorage().updateProfile(dbProfile));
        } else {
            // Just update cache.
            profile.withdraw(money);
            profile.save();
        }

        return new EconomyResponse(money, 0, EconomyResponse.ResponseType.SUCCESS, "");

    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double money) {
        return this.depositPlayer(Bukkit.getOfflinePlayer(playerName), money);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double money) {

        if (money < 0) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Je kan geen negatieve bedragen toeschrijven.");
        }

        Profile profile = Profile.getFromCache(offlinePlayer.getUniqueId());

        if(profile == null) {
            // Retrieve and save to data storage.
            plugin.getDataStorage().retrieveProfile(offlinePlayer.getUniqueId()).thenApply(dbProfile -> {
                dbProfile.deposit(money);
                return dbProfile;
            }).thenComposeAsync(dbProfile -> plugin.getDataStorage().updateProfile(dbProfile));
        } else {
            // Just update cache.
            profile.deposit(money);
            profile.save();
        }

        return new EconomyResponse(money, 0, EconomyResponse.ResponseType.SUCCESS, "");
    }

}
