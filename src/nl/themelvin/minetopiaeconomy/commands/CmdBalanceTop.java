package nl.themelvin.minetopiaeconomy.commands;

import nl.themelvin.minetopiaeconomy.Main;
import nl.themelvin.minetopiaeconomy.storage.StorageType;
import nl.themelvin.minetopiaeconomy.utils.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.TreeMap;

public class CmdBalanceTop implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender.hasPermission("minetopiaeconomy.baltop")) {

            commandSender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("baltop-calculate")));

            if(Main.storageType == StorageType.FILE) {

               

            }

        } else {
            commandSender.sendMessage(Logger.noPermission());
        }

        return false;
    }

}
