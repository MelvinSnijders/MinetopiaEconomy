package nl.themelvin.minetopiaeconomy;

import lombok.Getter;
import nl.themelvin.minetopiaeconomy.utils.Configuration;
import nl.themelvin.minetopiaeconomy.utils.VaultHook;
import nl.themelvin.minetopiaeconomy.vault.EconomyHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static nl.themelvin.minetopiaeconomy.utils.Logger.*;

public class MinetopiaEconomy extends JavaPlugin {

    @Getter private static Plugin plugin;

    private static Configuration messages;
    private static Configuration config;

    @Override
    public void onEnable() {

        plugin = this;

        // Log start
        log(Severity.INFO, "MinetopiaEconomy v" + this.getDescription().getVersion() + " wordt nu gestart.");
        long millis = System.currentTimeMillis();

        // Setup config
        config = new Configuration("config.yml");
        messages = new Configuration("messages.yml");

        VaultHook vault = new VaultHook(new EconomyHandler());

        if(!vault.hook()) {
            // TODO disable plugin
            return;
        }

        // Log end
        log(Severity.INFO, "Klaar! Bedankt voor het gebruiken van deze plugin, het duurde " + (System.currentTimeMillis() - millis) + "ms.");

    }

    public static Configuration messages() {

        return messages;

    }

    public static Configuration configuration() {

        return config;

    }

}
