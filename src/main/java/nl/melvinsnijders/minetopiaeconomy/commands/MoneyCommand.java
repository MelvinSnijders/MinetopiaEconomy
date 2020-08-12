package nl.melvinsnijders.minetopiaeconomy.commands;

import nl.melvinsnijders.minetopiaeconomy.MinetopiaEconomy;
import nl.melvinsnijders.minetopiaeconomy.profile.Profile;
import nl.melvinsnijders.minetopiaeconomy.utils.Logger;
import nl.melvinsnijders.minetopiaeconomy.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class MoneyCommand extends Command {

    public MoneyCommand(MinetopiaEconomy plugin, CommandSender sender, String alias, String[] args) {
        super(plugin, sender, alias, args);
    }

    @Override
    public void execute() {

        if (!this.validatePermission("minetopiaeconomy.money")) {
            return;
        }

        if (this.args.length < 1 || !this.sender.hasPermission("minetopiaeconomy.money.other")) {

            if (!this.senderIsPlayer()) {
                Logger.log(Logger.Severity.WARNING, Messages.MUST_BE_PLAYER.get());
                return;
            }

            Player player = (Player) this.sender;
            Profile profile = Profile.getFromCache(player.getUniqueId());

            player.sendMessage(Messages.OWN_MONEY.get().replaceAll("\\{money}", Messages.formatMoney(profile.getBalance())));
            return;

        }

        String targetString = this.args[0];

        CompletableFuture.supplyAsync(() -> Bukkit.getOfflinePlayer(targetString)).thenComposeAsync(player -> {
            if(player == null) {
                return null;
            } else if(player.isOnline()) {
                return CompletableFuture.completedFuture(Profile.getFromCache(player.getUniqueId()));
            } else {
                return this.plugin.getDataStorage().retrieveProfile(player.getUniqueId());
            }
        }).thenAccept(profile -> {
            if(profile == null) {
                this.sender.sendMessage(Messages.PLAYER_NOT_FOUND.get());
                return;
            }
            this.sender.sendMessage("geld " + profile.getBalance());
        });

    }

}
