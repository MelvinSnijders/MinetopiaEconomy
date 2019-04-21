package nl.themelvin.minetopiaeconomy.commands;

import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public abstract class AbstractCommand {

    protected CommandSender sender;
    protected String alias;
    protected String[] args;

    protected boolean senderIsPlayer() {

        return this.sender instanceof Player;

    }

    protected boolean senderHasPermissions(String... permissions) {

        if (!this.senderIsPlayer()) {

            return true;

        }

        for (String permission : permissions) {

            if (this.sender.hasPermission(permission)) {

                return true;

            }

        }

        return false;

    }

    public abstract CompletableFuture<Void> execute();

}
