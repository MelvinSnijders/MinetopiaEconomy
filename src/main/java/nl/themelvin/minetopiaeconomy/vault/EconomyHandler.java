package nl.themelvin.minetopiaeconomy.vault;

import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import nl.themelvin.minetopiaeconomy.MinetopiaEconomy;

public class EconomyHandler extends AbstractEconomy implements NoBankEconomy {

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
    public boolean hasAccount(String s) {
        return false;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    @Override
    public double getBalance(String s) {
        return 0;
    }

    @Override
    public double getBalance(String s, String s1) {
        return 0;
    }

    @Override
    public boolean has(String s, double v) {
        return false;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

}
