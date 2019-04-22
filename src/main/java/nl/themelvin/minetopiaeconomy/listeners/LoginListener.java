package nl.themelvin.minetopiaeconomy.listeners;

import nl.themelvin.minetopiaeconomy.models.Profile;
import nl.themelvin.minetopiaeconomy.utils.EssentialsData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static nl.themelvin.minetopiaeconomy.utils.Logger.*;

public class LoginListener extends AbstractListener<AsyncPlayerPreLoginEvent> {

    public LoginListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    public CompletableFuture<Void> listen(AsyncPlayerPreLoginEvent event) {

        UUID uuid = event.getUniqueId();

        Profile profile = new Profile(uuid);
        boolean exists = profile.load().join();
        profile.init();

        if(!exists) {

            double money = new EssentialsData(uuid).restore();

            if(money != 0) {

                profile.setMoney(money);
                log(Severity.INFO, "Oude data van " + uuid.toString() + " is hersteld.");

            }

        }


        return completedFuture(null);

    }

}
