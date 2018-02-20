package nl.themelvin.minetopiaeconomy.listeners;

import nl.themelvin.minetopiaeconomy.Main;
import nl.themelvin.minetopiaeconomy.storage.DataManager;
import nl.themelvin.minetopiaeconomy.user.UserDataCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DefaultPlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        DataManager.loadData(e.getUniqueId(), true);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        UserDataCache.getInstance().get(e.getPlayer().getUniqueId()).name = e.getPlayer().getName();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () ->  DataManager.saveCachedData(e.getPlayer().getUniqueId()));

    }

}
