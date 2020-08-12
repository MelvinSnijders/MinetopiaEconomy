package nl.melvinsnijders.minetopiaeconomy.commands;

import nl.melvinsnijders.minetopiaeconomy.MinetopiaEconomy;
import nl.melvinsnijders.minetopiaeconomy.profile.Profile;
import nl.melvinsnijders.minetopiaeconomy.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class Command {

    protected MinetopiaEconomy plugin;
    protected String alias;
    protected CommandSender sender;
    protected String[] args;

    protected Command(MinetopiaEconomy plugin, CommandSender sender, String alias, String[] args) {

        this.plugin = plugin;
        this.alias = alias;
        this.sender = sender;
        this.args = args;

    }

    public abstract void execute();

    @SuppressWarnings("SameParameterValue")
    protected final boolean validatePermission(String permission) {

        if (this.sender instanceof ConsoleCommandSender) {

            return true;

        }

        if (!this.sender.hasPermission(permission)) {

            this.sender.sendMessage(Messages.NO_PERMISSION.get());

        }

        return this.sender.hasPermission(permission);

    }

    protected final Profile getProfile() {

        Player player = (Player) this.sender;
        return Profile.getFromCache(player.getUniqueId());

    }

    public boolean senderIsPlayer() {

        return this.sender instanceof Player;

    }

}
