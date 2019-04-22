package nl.themelvin.minetopiaeconomy.commands;

import nl.themelvin.minetopiaeconomy.models.Profile;
import nl.themelvin.minetopiaeconomy.utils.Message;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;

@SuppressWarnings("Duplicates")
public class EconomyCommand extends AbstractCommand {

    public EconomyCommand(CommandSender sender, String alias, String[] args) {
        super(sender, alias, args);
    }

    @Override
    public CompletableFuture<Void> execute() {

        if (!this.senderHasPermissions("minetopiaeconomy.economy")) {

            this.sender.sendMessage(new Message("no-perm").get());
            return completedFuture(null);

        }

        if (this.args.length < 2) {

            this.sender.sendMessage(new Message("eco-use").get("cmd", this.alias));
            return completedFuture(null);

        }

        switch (this.args[0].toLowerCase()) {

            case "give": {

                if (!this.senderHasPermissions("minetopiaeconomy.economy.give")) {

                    this.sender.sendMessage(new Message("no-perm").get());
                    return completedFuture(null);

                }

                if (this.args.length < 3) {

                    this.sender.sendMessage(new Message("eco-use").get("cmd", this.alias));
                    return completedFuture(null);

                }

                String moneyString = this.args[2];

                if (!NumberUtils.isNumber(moneyString)) {

                    this.sender.sendMessage(new Message("eco-nonumber").get());
                    return completedFuture(null);

                }

                double money = NumberUtils.toDouble(moneyString);
                Profile targetProfile = this.getProfile();

                if(targetProfile == null) {
                    return completedFuture(null);
                }

                targetProfile.setMoney(targetProfile.getMoney() + money);

                if(Bukkit.getPlayer(this.args[1]) == null) {
                    targetProfile.update();
                }

                String formattedMoney = NumberFormat.getNumberInstance(Locale.GERMAN).format(money);
                this.sender.sendMessage(new Message("eco-give").get("name", targetProfile.getUsername(), "money", formattedMoney));

                break;

            }

            case "take": {

                if (!this.senderHasPermissions("minetopiaeconomy.economy.take")) {

                    this.sender.sendMessage(new Message("no-perm").get());
                    return completedFuture(null);

                }

                if (this.args.length < 3) {

                    this.sender.sendMessage(new Message("eco-use").get("cmd", this.alias));
                    return completedFuture(null);

                }

                String moneyString = this.args[2];

                if (!NumberUtils.isNumber(moneyString)) {

                    this.sender.sendMessage(new Message("eco-nonumber").get());
                    return completedFuture(null);

                }

                double money = NumberUtils.toDouble(moneyString);
                Profile targetProfile = this.getProfile();

                if(targetProfile == null) {
                    return completedFuture(null);
                }

                if(targetProfile.getMoney() < money) {

                    this.sender.sendMessage(new Message("eco-toomuch").get());
                    return completedFuture(null);

                }

                targetProfile.setMoney(targetProfile.getMoney() - money);

                if(Bukkit.getPlayer(this.args[1]) == null) {
                    targetProfile.update();
                }

                String formattedMoney = NumberFormat.getNumberInstance(Locale.GERMAN).format(money);
                this.sender.sendMessage(new Message("eco-take").get("name", targetProfile.getUsername(), "money", formattedMoney));

                break;

            }

            case "set": {

                if (!this.senderHasPermissions("minetopiaeconomy.economy.set")) {

                    this.sender.sendMessage(new Message("no-perm").get());
                    return completedFuture(null);

                }

                if (this.args.length < 3) {

                    this.sender.sendMessage(new Message("eco-use").get("cmd", this.alias));
                    return completedFuture(null);

                }

                String moneyString = this.args[2];

                if (!NumberUtils.isNumber(moneyString)) {

                    this.sender.sendMessage(new Message("eco-nonumber").get());
                    return completedFuture(null);

                }

                double money = NumberUtils.toDouble(moneyString);

                Profile result = this.setMoney(money);

                if (result != null) {

                    String formattedMoney = NumberFormat.getNumberInstance(Locale.GERMAN).format(result.getMoney());
                    this.sender.sendMessage(new Message("eco-set").get("name", result.getUsername(), "money", formattedMoney));

                }

                break;

            }

            case "reset": {

                if (!this.senderHasPermissions("minetopiaeconomy.economy.reset")) {

                    this.sender.sendMessage(new Message("no-perm").get());
                    return completedFuture(null);

                }

                Profile result = this.setMoney(0D);

                if (result != null) {

                    this.sender.sendMessage(new Message("eco-reset").get("name", result.getUsername()));

                }

                break;

            }

        }

        return completedFuture(null);

    }

    private Profile setMoney(double money) {

        Player targetPlayer = Bukkit.getPlayer(this.args[1]);

        Profile targetProfile = this.getProfile();
        targetProfile.setMoney(0D);

        if(targetPlayer == null) {

            targetProfile.update();

        }

        return targetProfile;

    }

    private Profile getProfile() {

        String targetString = this.args[1];
        Player targetPlayer = Bukkit.getPlayer(this.args[1]);

        Profile targetProfile;

        if (targetPlayer != null) {

            targetProfile = new Profile(targetPlayer.getUniqueId()).get();

        } else {

            targetProfile = new Profile(targetString);
            boolean exists = await(targetProfile.load(targetString));

            if (!exists) {

                this.sender.sendMessage(new Message("eco-nodata").get());
                return null;

            }

        }

        return targetProfile;

    }


}
