package nl.melvinsnijders.minetopiaeconomy.listeners;

import nl.melvinsnijders.minetopiaeconomy.MinetopiaEconomy;
import nl.melvinsnijders.minetopiaeconomy.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends Listener<PlayerQuitEvent> {

    public PlayerQuitListener(MinetopiaEconomy plugin) {
        super(plugin);
    }

    /**
     * Method executed on event call.
     *
     * @param event The event to execute.
     */
    @EventHandler
    public void execute(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        Profile profile = Profile.getFromCache(player.getUniqueId());
        profile.save(true);

        this.plugin.getProfileCache().remove(player.getUniqueId());

    }

}
