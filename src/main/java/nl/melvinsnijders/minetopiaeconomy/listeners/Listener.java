package nl.melvinsnijders.minetopiaeconomy.listeners;

import nl.melvinsnijders.minetopiaeconomy.MinetopiaEconomy;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public abstract class Listener<T extends Event> implements org.bukkit.event.Listener {

    protected MinetopiaEconomy plugin;

    protected Listener(MinetopiaEconomy plugin) {

        this.plugin = plugin;

    }

    /**
     * Method executed on event call.
     *
     * @param event The event to execute.
     */

    public abstract void execute(T event);

}
