package nl.themelvin.minetopiaeconomy;

import lombok.Getter;
import nl.themelvin.minetopiaeconomy.models.Model;
import nl.themelvin.minetopiaeconomy.models.Profile;
import nl.themelvin.minetopiaeconomy.utils.Configuration;
import nl.themelvin.minetopiaeconomy.utils.Logger;
import nl.themelvin.minetopiaeconomy.utils.VaultHook;
import nl.themelvin.minetopiaeconomy.vault.EconomyHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

import static nl.themelvin.minetopiaeconomy.utils.Logger.*;

public class MinetopiaEconomy extends JavaPlugin {

    @Getter
    private static Plugin plugin;

    private static Configuration messages;
    private static Configuration config;

    @Getter
    private static HashMap<UUID, Profile> onlineProfiles = new HashMap<>();

    @Override
    public void onEnable() {

        plugin = this;

        // Log start
        log(Severity.INFO, "MinetopiaEconomy v" + this.getDescription().getVersion() + " wordt nu gestart.");
        long millis = System.currentTimeMillis();

        // Setup config
        config = new Configuration("config.yml");
        messages = new Configuration("messages.yml");

        // Setup MySQL / SQLite
        String storageType = config.getString("storage");

        if (!storageType.equalsIgnoreCase("mysql") && !storageType.equalsIgnoreCase("sqlite")) {
            log(Severity.ERROR, "Je hebt geen geldig opslagtype ingevuld, plugin wordt uitgeschakeld.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!Model.startPool()) {
            Logger.log(Severity.ERROR, "Kon geen stabiele verbinding maken met de database, plugin wordt uitgeschakeld");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Hook in Vault
        VaultHook vault = new VaultHook(new EconomyHandler());

        if (!vault.hook()) {
            log(Severity.WARNING, "Vault is niet in deze server geïnstalleerd. Deze plugin werkt ook zonder Vault, maar dat is niet aangeraden.");
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
