package nl.themelvin.minetopiaeconomy.commands;

import nl.themelvin.minetopiaeconomy.Main;
import nl.themelvin.minetopiaeconomy.storage.StorageType;
import nl.themelvin.minetopiaeconomy.storage.database.MySQL;
import nl.themelvin.minetopiaeconomy.user.UserDataCache;
import nl.themelvin.minetopiaeconomy.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

public class CmdMoney implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0 || args.length > 1) {
                p.sendMessage(Logger.placeholderFormat(Main.messageFile.getData().getString("money-cmd"), p));
            } else if (args.length == 1) {

                if (p.hasPermission("minetopiaeconomy.balance")) {

                    String target = args[0];
                    Double money = 0D;
                    String realTargetName = target;

                    if(Bukkit.getPlayer(target) != null) {
                        realTargetName = Bukkit.getPlayer(target).getName();
                        money = UserDataCache.getInstance().get(Bukkit.getPlayer(target).getUniqueId()).money;
                        p.sendMessage(Logger.placeholderFormat(Main.messageFile.getData().getString("money-cmd-other"), p).replaceAll("%targetname%", realTargetName).replaceAll("%targetbalance%", NumberFormat.getNumberInstance(Locale.GERMAN).format(money)));
                        return true;
                    }

                    if (Main.storageType == StorageType.FILE) {
                        for(String key : Main.dataFile.getData().getConfigurationSection("players").getKeys(false)) {
                            if(Main.dataFile.getData().getString("players." + key + ".username").equalsIgnoreCase(target)) {
                                money =  Main.dataFile.getData().getDouble("players." + key + ".money");
                                realTargetName = Main.dataFile.getData().getString("players." + key + ".username");
                            }
                        }
                    }

                    if (Main.storageType == StorageType.MYSQL) {
                        try {
                            PreparedStatement pst = MySQL.getConnection().prepareStatement("SELECT * FROM `UserData` WHERE lower(`username`) = ?");
                            pst.setString(1, target.toLowerCase());
                            ResultSet res = pst.executeQuery();
                            if (res.next()) {
                                money = res.getDouble("money");
                                realTargetName = res.getString("username");
                            }
                        } catch (SQLException e) {
                            money = 0D;
                        }
                        MySQL.close();
                    }

                    p.sendMessage(Logger.placeholderFormat(Main.messageFile.getData().getString("money-cmd-other"), p).replaceAll("%targetname%", realTargetName).replaceAll("%targetbalance%", NumberFormat.getNumberInstance(Locale.GERMAN).format(money)));

                } else {
                    p.sendMessage(Logger.placeholderFormat(Main.messageFile.getData().getString("money-cmd"), p));
                }
            }
        } else {
            Logger.consoleOutput(Logger.InfoLevel.INFO, "Enkel een speler kan dit doen.");
        }
        return true;
    }

}
