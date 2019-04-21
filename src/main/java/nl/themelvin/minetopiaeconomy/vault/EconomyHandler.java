package nl.themelvin.minetopiaeconomy.vault;

import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import nl.themelvin.minetopiaeconomy.MinetopiaEconomy;

public class EconomyHandler extends AbstractEconomy implements NoBankEconomy, NoAccountNameEconomy {

    @Override
    public boolean isEnabled() {
        return MinetopiaEconomy.getPlugin().isEnabled();
    }

    @Override
    public String getName() {
        return MinetopiaEconomy.getPlugin().getName();
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return String.valueOf(v);
    }

    @Override
    public String currencyNamePlural() {
        return "Euro";
    }

    @Override
    public String currencyNameSingular() {
        return "€";
    }

    @Override
    public boolean hasAccount(String playerName) {
        return false;
    }

    @Override
    public double getBalance(String playerName) {
        return 0;
    }

    @Override
    public boolean has(String playerName, double money) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double money) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double money) {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

}
