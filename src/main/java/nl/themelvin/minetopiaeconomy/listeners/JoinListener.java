package nl.themelvin.minetopiaeconomy.listeners;

import nl.themelvin.minetopiaeconomy.models.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class JoinListener extends AbstractListener<PlayerJoinEvent> {

    public JoinListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    public CompletableFuture<Void> listen(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        Profile profile = new Profile(player.getUniqueId()).get();
        profile.setUsername(player.getName());

        return completedFuture(null);

    }

}
