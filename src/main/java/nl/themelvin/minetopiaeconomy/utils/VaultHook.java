package nl.themelvin.minetopiaeconomy.utils;

import net.milkbowl.vault.economy.Economy;
import nl.themelvin.minetopiaeconomy.MinetopiaEconomy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class VaultHook {

    private final Class<? extends Economy> economyClass;

    public VaultHook(Class<? extends Economy> economyClass) {

        this.economyClass = economyClass;

    }

    public void hook() {

        try {
            Bukkit.getServicesManager().register(Economy.class, economyClass.newInstance(), MinetopiaEconomy.getPlugin(), ServicePriority.Highest);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
