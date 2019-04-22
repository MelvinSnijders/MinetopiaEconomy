package nl.themelvin.minetopiaeconomy.commands;

import nl.themelvin.minetopiaeconomy.models.Profile;
import nl.themelvin.minetopiaeconomy.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class MoneyCommand extends AbstractCommand {

    public MoneyCommand(CommandSender sender, String alias, String[] args) {
        super(sender, alias, args);
    }

    @Override
    public CompletableFuture<Void> execute() {

        if(!this.senderHasPermissions("minetopiaeconomy.balance") && !this.senderHasPermissions("minetopiaeconomy.balance.other")) {

            this.sender.sendMessage(new Message("no-perm").get());
            return completedFuture(null);

        }

        if(args.length < 1 || !this.senderHasPermissions("minetopiaeconomy.balance.other")) {

            if(!this.senderIsPlayer()) {

                this.sender.sendMessage(new Message("money-cmd-usage-console").get("cmd", this.alias));
                return completedFuture(null);

            }

            Player player = (Player) this.sender;
            Profile profile = new Profile(player.getUniqueId()).get();
            String formattedMoney = NumberFormat.getNumberInstance(Locale.GERMAN).format(profile.getMoney());

            player.sendMessage(new Message("money-cmd").get("balance", formattedMoney));

            return completedFuture(null);

        }

        String targetString = this.args[0];
        Player player = Bukkit.getPlayer(targetString);

        Profile targetProfile;

        if(player != null) {

            targetProfile = new Profile(player.getUniqueId()).get();

        } else {

            targetProfile = new Profile(targetString);
            await(targetProfile.load(targetString));

        }

        String formattedMoney = NumberFormat.getNumberInstance(Locale.GERMAN).format(targetProfile.getMoney());

        this.sender.sendMessage(new Message("money-cmd-other").get("name", targetProfile.getUsername(), "balance", formattedMoney));

        return completedFuture(null);

    }

}
