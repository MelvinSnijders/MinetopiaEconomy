package nl.themelvin.minetopiaeconomy.messaging.incoming;

import nl.themelvin.minetopiaeconomy.messaging.outgoing.PlayerMessage;
import nl.themelvin.minetopiaeconomy.models.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class PlayerMessageHandler extends AbstractMessageHandler<PlayerMessage> {

    @Override
    public CompletableFuture<Void> execute(PlayerMessage message) {

        Profile profile = new Profile(message.getUuid());
        Player player = Bukkit.getPlayer(message.getUuid());

        if(player != null) {
            return completedFuture(null);
        }

        if(message.isJoined()) {

            // Add to cache
            profile.init();

        } else {

            // Remove from cache
            profile.unCache();

        }

        return completedFuture(null);
    }

}
