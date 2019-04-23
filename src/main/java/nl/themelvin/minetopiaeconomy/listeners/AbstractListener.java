package nl.themelvin.minetopiaeconomy.listeners;

import nl.themelvin.minetopiaeconomy.MinetopiaEconomy;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractListener<T> implements Listener {

    protected final Plugin plugin;

    protected AbstractListener(Plugin plugin) {

        this.plugin = plugin;

    }

    public abstract CompletableFuture<Void> listen(T event);

    public void register() {

        MinetopiaEconomy.getPlugin().getServer().getPluginManager().registerEvents(this, MinetopiaEconomy.getPlugin());

    }

    public void deregister() {

        HandlerList.unregisterAll(this);

    }

}
