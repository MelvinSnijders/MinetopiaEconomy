package nl.themelvin.minetopiaeconomy.listeners;

import nl.themelvin.minetopiaeconomy.models.Profile;
import nl.themelvin.minetopiaeconomy.utils.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;

import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static nl.themelvin.minetopiaeconomy.utils.Logger.log;

public class QuitListener extends AbstractListener<PlayerQuitEvent> {

    public QuitListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    public CompletableFuture<Void> listen(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        Profile profile = new Profile(player.getUniqueId()).get();
        CompletableFuture<Boolean> updateFuture = profile.update();
        profile.unCache();

        if(!await(updateFuture)) {
            log(Logger.Severity.ERROR, "Kon het profiel van " + player.getName() + " niet updaten.");
        }

        return completedFuture(null);

    }

}
