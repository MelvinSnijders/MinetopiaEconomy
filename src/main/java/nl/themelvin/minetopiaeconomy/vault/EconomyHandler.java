package nl.themelvin.minetopiaeconomy.vault;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import nl.themelvin.minetopiaeconomy.MinetopiaEconomy;
import nl.themelvin.minetopiaeconomy.models.Profile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class EconomyHandler implements Economy, NoBankEconomy, NoAccountNameEconomy, NoWorldNameEconomy {

    /**
     * Checks if economy method is enabled.
     *
     * @return Success or Failure.
     */

    @Override
    public boolean isEnabled() {
        return MinetopiaEconomy.getPlugin().isEnabled();
    }

    /**
     * Gets name of economy method.
     *
     * @return Name of Economy Method.
     */

    @Override
    public String getName() {
        return MinetopiaEconomy.getPlugin().getName();
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

    /**
     * Format amount into a human readable String This provides translation into
     * economy specific formatting to improve consistency between plugins.
     *
     * @param amount Amount to format.
     * @return Human readable string describing amount.
     */

    @Override
    public String format(double amount) {
        return NumberFormat.getNumberInstance(Locale.GERMAN).format(amount);
    }

    /**
     * Returns the name of the currency in plural form.
     * If the economy being used does not support currency names then an empty string will be returned.
     *
     * @return Name of the currency (plural).
     */

    @Override
    public String currencyNamePlural() {
        return "Euro";
    }

    /**
     * Returns the name of the currency in singular form.
     * If the economy being used does not support currency names then an empty string will be returned.
     *
     * @return Name of the currency (singular).
     */

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

    @Deprecated
    public boolean hasAccount(String playerName) {

        Player player = Bukkit.getPlayer(playerName);

        if (player != null) {
            return true;
        }

        Profile profile = new Profile(playerName);

        return profile.load(playerName).join();

    }

    /**
     * Checks if this player has an account on the server yet.
     *
     * @param offlinePlayer Player to check.
     * @return If the player has an account.
     **/

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {

        return offlinePlayer.hasPlayedBefore();

    }

    /**
     * Gets balance of a player.
     *
     * @param playerName Player to check.
     * @return Amount currently held in players account.
     */

    @Override
    public double getBalance(String playerName) {

        Player player = Bukkit.getPlayer(playerName);

        if (player != null) {

            Profile profile = new Profile(player.getUniqueId()).get();
            return profile.getMoney();

        }

        Profile profile = new Profile(playerName);
        profile.load(playerName).join();

        return profile.getMoney();

    }

    /**
     * Gets balance of a player.
     *
     * @param offlinePlayer Player to check.
     * @return Amount currently held in players account.
     */

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {

        if (offlinePlayer.isOnline()) {

            Profile profile = new Profile(offlinePlayer.getUniqueId()).get();
            return profile.getMoney();

        }

        Profile profile = new Profile(offlinePlayer.getUniqueId());
        profile.load().join();

        return profile.getMoney();

    }

    /**
     * Checks if the player account has the amount - DO NOT USE NEGATIVE AMOUNTS.
     *
     * @param playerName Player to check.
     * @param money      Amount to check for.
     * @return True if has amount, False else wise.
     */

    @Override
    public boolean has(String playerName, double money) {

        return this.getBalance(playerName) >= money;

    }

    /**
     * Checks if the player account has the amount - DO NOT USE NEGATIVE AMOUNTS.
     *
     * @param offlinePlayer Player to check.
     * @param money         Amount to check for.
     * @return True if has amount, False else wise.
     */

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double money) {

        return this.getBalance(offlinePlayer) >= money;

    }

    /**
     * Withdraw an amount from a player - DO NOT USE NEGATIVE AMOUNTS.
     *
     * @param playerName Player to withdraw from.
     * @param money      Amount to withdraw.
     * @return Detailed response of transaction.
     */

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double money) {

        if (money < 0) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Je kan geen negatieve bedragen afnemen.");
        }

        Player targetPlayer = Bukkit.getPlayer(playerName);

        if (targetPlayer != null) {

            Profile targetProfile = new Profile(targetPlayer.getUniqueId()).get();
            targetProfile.setMoney(targetProfile.getMoney() - money);
            return new EconomyResponse(money, targetProfile.getMoney(), EconomyResponse.ResponseType.SUCCESS, "");

        } else {

            CompletableFuture.runAsync(() -> {

                Profile targetProfile = new Profile(playerName);
                boolean exists = targetProfile.load(playerName).join();

                if (exists) {

                    targetProfile.setMoney(targetProfile.getMoney() - money);
                    targetProfile.update();

                }

            });

            return new EconomyResponse(money, 0, EconomyResponse.ResponseType.SUCCESS, "");

        }

    }

    /**
     * Withdraw an amount from a player - DO NOT USE NEGATIVE AMOUNTS.
     *
     * @param offlinePlayer Player to withdraw from.
     * @param money         Amount to withdraw.
     * @return Detailed response of transaction.
     */

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double money) {

        if (money < 0) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Je kan geen negatieve bedragen afnemen.");
        }

        if (offlinePlayer.isOnline()) {

            Profile targetProfile = new Profile(offlinePlayer.getUniqueId()).get();
            targetProfile.setMoney(targetProfile.getMoney() - money);
            return new EconomyResponse(money, targetProfile.getMoney(), EconomyResponse.ResponseType.SUCCESS, "");

        } else {

            CompletableFuture.runAsync(() -> {

                Profile targetProfile = new Profile(offlinePlayer.getUniqueId());
                boolean exists = targetProfile.load().join();

                if (exists) {

                    targetProfile.setMoney(targetProfile.getMoney() - money);
                    targetProfile.update();

                }

            });

            return new EconomyResponse(money, 0, EconomyResponse.ResponseType.SUCCESS, "");

        }

    }

    /**
     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS.
     *
     * @param playerName to deposit to.
     * @param money      Amount to deposit.
     * @return Detailed response of transaction.
     */

    @Override
    public EconomyResponse depositPlayer(String playerName, double money) {

        if (money < 0) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Je kan geen negatieve bedragen storten.");
        }

        Player targetPlayer = Bukkit.getPlayer(playerName);

        if (targetPlayer != null) {

            Profile targetProfile = new Profile(targetPlayer.getUniqueId()).get();
            targetProfile.setMoney(targetProfile.getMoney() + money);
            return new EconomyResponse(money, targetProfile.getMoney(), EconomyResponse.ResponseType.SUCCESS, "");

        } else {

            CompletableFuture.runAsync(() -> {

                Profile targetProfile = new Profile(playerName);
                boolean exists = targetProfile.load(playerName).join();

                if (exists) {

                    targetProfile.setMoney(targetProfile.getMoney() + money);
                    targetProfile.update();

                }

            });

            return new EconomyResponse(money, 0, EconomyResponse.ResponseType.SUCCESS, "");

        }

    }

    /**
     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS.
     *
     * @param offlinePlayer to deposit to.
     * @param money         Amount to deposit.
     * @return Detailed response of transaction.
     */

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double money) {

        if (money < 0) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Je kan geen negatieve bedragen storten.");
        }

        if (offlinePlayer.isOnline()) {

            Profile targetProfile = new Profile(offlinePlayer.getUniqueId()).get();
            targetProfile.setMoney(targetProfile.getMoney() + money);
            return new EconomyResponse(money, targetProfile.getMoney(), EconomyResponse.ResponseType.SUCCESS, "");

        } else {

            CompletableFuture.runAsync(() -> {

                Profile targetProfile = new Profile(offlinePlayer.getUniqueId());
                boolean exists = targetProfile.load().join();

                if (exists) {

                    targetProfile.setMoney(targetProfile.getMoney() + money);
                    targetProfile.update();

                }

            });

            return new EconomyResponse(money, 0, EconomyResponse.ResponseType.SUCCESS, "");

        }

    }

}
