package nl.melvinsnijders.minetopiaeconomy.listeners;

import nl.melvinsnijders.minetopiaeconomy.MinetopiaEconomy;
import nl.melvinsnijders.minetopiaeconomy.profile.Profile;
import nl.melvinsnijders.minetopiaeconomy.utils.Cache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends Listener<PlayerJoinEvent> {

    public PlayerJoinListener(MinetopiaEconomy plugin) {
        super(plugin);
    }

    /**
     * Method executed on event call.
     *
     * @param event The event to execute.
     */
    @EventHandler
    public void execute(PlayerJoinEvent event) {

        // Retrieve Player profile and update username.
        Player player = event.getPlayer();
        Profile profile = Profile.getFromCache(player.getUniqueId());

        if(profile.getUserName() == null || !profile.getUserName().equals(player.getName())) {
            profile.setUserName(player.getName());
            profile.save(true);
        }

    }

}
