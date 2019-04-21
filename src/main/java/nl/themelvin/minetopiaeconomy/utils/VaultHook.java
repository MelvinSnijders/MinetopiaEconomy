package nl.themelvin.minetopiaeconomy.utils;

import net.milkbowl.vault.economy.Economy;
import nl.themelvin.minetopiaeconomy.MinetopiaEconomy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class VaultHook {

    private final Economy economy;

    public VaultHook(Economy economy) {

        this.economy = economy;

    }

    public boolean hook() {

        if(Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        Bukkit.getServicesManager().register(Economy.class, economy, MinetopiaEconomy.getPlugin(), ServicePriority.Highest);
        return true;

    }

}
