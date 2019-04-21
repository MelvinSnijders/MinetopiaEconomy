package nl.themelvin.minetopiaeconomy.listeners;

import nl.themelvin.minetopiaeconomy.models.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class LoginListener extends AbstractListener<AsyncPlayerPreLoginEvent> {

    public LoginListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    public CompletableFuture<Void> listen(AsyncPlayerPreLoginEvent event) {

        UUID uuid = event.getUniqueId();

        Profile profile = new Profile(uuid);
        profile.init();

        return completedFuture(null);

    }

}
