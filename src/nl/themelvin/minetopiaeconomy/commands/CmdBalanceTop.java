package nl.themelvin.minetopiaeconomy.commands;

import nl.themelvin.minetopiaeconomy.Main;
import nl.themelvin.minetopiaeconomy.storage.StorageType;
import nl.themelvin.minetopiaeconomy.storage.database.MySQL;
import nl.themelvin.minetopiaeconomy.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.*;

public class CmdBalanceTop implements CommandExecutor {

    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<>(
                (e2, e1) -> {
                    int res = e1.getValue().compareTo(e2.getValue());
                    return res != 0 ? res : 1;
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender.hasPermission("minetopiaeconomy.baltop")) {

            commandSender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("baltop-calculate")));

            Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () -> {

                if(Main.storageType == StorageType.FILE) {

                    Map<String, Double> map = new TreeMap<>();

                    for(String key : Main.dataFile.getData().getConfigurationSection("players").getKeys(false)) {
                        map.put(Main.dataFile.getData().getString("players." + key + ".username"), Main.dataFile.getData().getDouble("players." + key + ".money"));
                    }

                    int i = 0;

                    for(Map.Entry<String, Double> entry : entriesSortedByValues(map)) {
                        if(i < 9) {
                            commandSender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("baltop-result")).replaceAll("%number%", String.valueOf(i + 1)).replaceAll("%resultname%", entry.getKey()).replaceAll("%resultmoney%", NumberFormat.getNumberInstance(Locale.GERMAN).format(entry.getValue())));
                        }
                        i++;
                    }

                }

                if(Main.storageType == StorageType.MYSQL) {
                    try {
                        PreparedStatement pst = MySQL.getConnection().prepareStatement("SELECT * FROM `UserData` ORDER BY `money` DESC LIMIT 8;");
                        ResultSet res = pst.executeQuery();
                        int i = 1;
                        while(res.next()) {
                            commandSender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("baltop-result")).replaceAll("%number%", String.valueOf(i)).replaceAll("%resultname%", res.getString("username")).replaceAll("%resultmoney%", NumberFormat.getNumberInstance(Locale.GERMAN).format(res.getDouble("money"))));
                            i++;
                        }
                    } catch (Exception e) {
                        commandSender.sendMessage(Logger.colorFormat("&cEr ging iets fout tijdens het ophalen van de data."));
                    }
                    MySQL.close();
                }

            });


        } else {
            commandSender.sendMessage(Logger.noPermission());
        }

        return false;
    }

}
