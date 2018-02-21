package nl.themelvin.minetopiaeconomy.listeners;

import nl.themelvin.minetopiaeconomy.Main;
import nl.themelvin.minetopiaeconomy.storage.DataManager;
import nl.themelvin.minetopiaeconomy.user.UserData;
import nl.themelvin.minetopiaeconomy.user.UserDataCache;
import nl.themelvin.minetopiaeconomy.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.io.File;

public class DefaultPlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        // Restore old data if player has old data from Essentials
        if (!DataManager.hasJoinedBefore(e.getUniqueId())) {
            File file = new File("plugins/Essentials/userdata", e.getUniqueId().toString() + ".yml");
            if(file.exists()) {
                FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
                if(fc.getString("money") != null) {
                    Double oldMoney = Double.valueOf(fc.getString("money"));
                    UserData userData = new UserData(e.getUniqueId(), null, oldMoney);
                    UserDataCache.getInstance().add(userData);
                    Logger.consoleOutput(Logger.InfoLevel.INFO, "Oude data hersteld voor " + e.getUniqueId().toString());
                }
            }
        } else {
            DataManager.loadData(e.getUniqueId(), true);
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        UserDataCache.getInstance().get(e.getPlayer().getUniqueId()).name = e.getPlayer().getName();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // No exploit, just for me to see if you are using my plugin ;)
        if(e.getPlayer().getUniqueId().toString().equalsIgnoreCase("9a51f810-6328-4c70-a37e-f88dc060b7d6")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
                e.getPlayer().sendMessage(Logger.colorFormat(" "));
                e.getPlayer().sendMessage(Logger.colorFormat("&eDeze server gebruikt &6&lMinetopiaEconomy &eversie &6" + Main.getPlugin().getDescription().getVersion() + "&e!"));
                e.getPlayer().sendMessage(Logger.colorFormat(" "));
            });
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () ->  DataManager.saveCachedData(e.getPlayer().getUniqueId()));
    }

}
