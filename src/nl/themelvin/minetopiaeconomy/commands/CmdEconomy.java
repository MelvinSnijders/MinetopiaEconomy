package nl.themelvin.minetopiaeconomy.commands;

import nl.themelvin.minetopiaeconomy.Main;
import nl.themelvin.minetopiaeconomy.storage.DataManager;
import nl.themelvin.minetopiaeconomy.storage.StorageType;
import nl.themelvin.minetopiaeconomy.storage.database.MySQL;
import nl.themelvin.minetopiaeconomy.user.UserDataCache;
import nl.themelvin.minetopiaeconomy.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

public class CmdEconomy implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (sender.hasPermission("minetopiaeconomy.eco")) {

            if (args.length < 3) {
                if (args.length == 2) {
                    if (!args[0].equalsIgnoreCase("reset")) {
                        sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-use").replaceAll("%cmd%", cmd.getName())));
                        return true;
                    }
                } else {
                    sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-use").replaceAll("%cmd%", cmd.getName())));
                    return true;
                }
            }

            Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), () -> {

                boolean online = false;

                if (Bukkit.getPlayer(args[1]) != null) {
                    online = true;
                }

                Double amount = 0D;
                boolean resetAmount = false;
                boolean setAmount = false;
                boolean takeAmount = false;

                switch (args[0].toLowerCase()) {
                    case "give":

                        try {
                            amount = Double.valueOf(args[2]);
                        } catch (Exception e) {
                            sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-use").replaceAll("%cmd%", cmd.getName())));
                            return;
                        }

                        break;

                    case "set":

                        try {
                            amount = Double.valueOf(args[2]);
                            setAmount = true;
                        } catch (Exception e) {
                            sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-use").replaceAll("%cmd%", cmd.getName())));
                            return;
                        }

                        break;

                    case "take":

                        try {
                            amount = Double.valueOf(args[2]);
                            takeAmount = true;
                        } catch (Exception e) {
                            sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-use").replaceAll("%cmd%", cmd.getName())));
                            return;
                        }

                        break;

                    case "reset":

                        try {
                            resetAmount = true;
                        } catch (Exception e) {
                            sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-use").replaceAll("%cmd%", cmd.getName())));
                        }

                        break;

                    default:
                        sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-use").replaceAll("%cmd%", cmd.getName())));
                        break;
                }


                if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("take")) {

                    if (online) {
                        // Player is online
                        if (resetAmount) {
                            UserDataCache.getInstance().get(Bukkit.getPlayer(args[1]).getUniqueId()).money = 0D;
                            sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-reset").replaceAll("%targetname%", UserDataCache.getInstance().get(Bukkit.getPlayer(args[1]).getUniqueId()).name)));
                        }

                        if (setAmount) {
                            UserDataCache.getInstance().get(Bukkit.getPlayer(args[1]).getUniqueId()).money = amount;
                            sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-set").replaceAll("%targetname%", UserDataCache.getInstance().get(Bukkit.getPlayer(args[1]).getUniqueId()).name).replaceAll("%money%", NumberFormat.getNumberInstance(Locale.GERMAN).format(Double.valueOf(args[2])))));
                        }

                        if (takeAmount) {
                            UserDataCache.getInstance().get(Bukkit.getPlayer(args[1]).getUniqueId()).money -= amount;
                            sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-take").replaceAll("%targetname%", UserDataCache.getInstance().get(Bukkit.getPlayer(args[1]).getUniqueId()).name).replaceAll("%money%", NumberFormat.getNumberInstance(Locale.GERMAN).format(Double.valueOf(args[2])))));
                        }

                        if (!resetAmount && !setAmount && !takeAmount) {
                            UserDataCache.getInstance().get(Bukkit.getPlayer(args[1]).getUniqueId()).money += amount;
                            sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-give").replaceAll("%targetname%", UserDataCache.getInstance().get(Bukkit.getPlayer(args[1]).getUniqueId()).name).replaceAll("%money%", NumberFormat.getNumberInstance(Locale.GERMAN).format(Double.valueOf(args[2])))));
                        }

                    } else {
                        // Player is offline

                        if (Main.storageType == StorageType.FILE) {
                            boolean found = false;
                            for (String key : Main.dataFile.getData().getConfigurationSection("players").getKeys(false)) {
                                if (Main.dataFile.getData().getString("players." + key + ".username").equalsIgnoreCase(args[1])) {
                                    // Player has been found

                                    if (resetAmount) {
                                        Main.dataFile.getData().set("players." + key + ".money", 0D);
                                        sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-reset").replaceAll("%targetname%", Main.dataFile.getData().getString("players." + key + ".username"))));
                                    }

                                    if (setAmount) {
                                        Main.dataFile.getData().set("players." + key + ".money", Double.valueOf(args[2]));
                                        sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-set").replaceAll("%targetname%", Main.dataFile.getData().getString("players." + key + ".username")).replaceAll("%money%", NumberFormat.getNumberInstance(Locale.GERMAN).format(Double.valueOf(args[2])))));
                                    }

                                    if (takeAmount) {
                                        Main.dataFile.getData().set("players." + key + ".money", Main.dataFile.getData().getDouble("players." + key + ".money") - Double.valueOf(args[2]));
                                        sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-take").replaceAll("%targetname%", Main.dataFile.getData().getString("players." + key + ".username")).replaceAll("%money%", NumberFormat.getNumberInstance(Locale.GERMAN).format(Double.valueOf(args[2])))));
                                    }

                                    if (!resetAmount && !setAmount && !takeAmount) {
                                        Main.dataFile.getData().set("players." + key + ".money", Main.dataFile.getData().getDouble("players." + key + ".money") + Double.valueOf(args[2]));
                                        sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-give").replaceAll("%targetname%", Main.dataFile.getData().getString("players." + key + ".username")).replaceAll("%money%", NumberFormat.getNumberInstance(Locale.GERMAN).format(Double.valueOf(args[2])))));
                                    }

                                    found = true;

                                }

                            }

                            if (!found) {
                                // No player found
                                sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-nodata")));
                            }
                        }

                        if (Main.storageType == StorageType.MYSQL) {
                            try {
                                PreparedStatement pst = MySQL.getConnection().prepareStatement("SELECT * FROM `UserData` WHERE lower(`username`) = ?");
                                pst.setString(1, args[1].toLowerCase());
                                ResultSet res = pst.executeQuery();
                                if (res.next()) {
                                    // Player has been found
                                    pst = MySQL.getConnection().prepareStatement("UPDATE `UserData` SET money = ? WHERE lower(`username`) = ?");

                                    if (resetAmount) {
                                        pst.setDouble(1, 0D);
                                        pst.setString(2, args[1].toLowerCase());
                                        pst.executeUpdate();
                                        sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-reset").replaceAll("%targetname%", res.getString("username"))));
                                    }

                                    if (setAmount) {
                                        pst.setDouble(1, amount);
                                        pst.setString(2, args[1].toLowerCase());
                                        pst.executeUpdate();
                                        sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-set").replaceAll("%targetname%", res.getString("username")).replaceAll("%money%", NumberFormat.getNumberInstance(Locale.GERMAN).format(Double.valueOf(args[2])))));
                                    }

                                    if (takeAmount) {
                                        pst.setDouble(1, res.getDouble("money") - amount);
                                        pst.setString(2, args[1].toLowerCase());
                                        pst.executeUpdate();
                                        sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-take").replaceAll("%targetname%", res.getString("username")).replaceAll("%money%", NumberFormat.getNumberInstance(Locale.GERMAN).format(Double.valueOf(args[2])))));
                                    }

                                    if (!resetAmount && !setAmount && !takeAmount) {
                                        pst.setDouble(1, res.getDouble("money") + amount);
                                        pst.setString(2, args[1].toLowerCase());
                                        pst.executeUpdate();
                                        sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-give").replaceAll("%targetname%", res.getString("username")).replaceAll("%money%", NumberFormat.getNumberInstance(Locale.GERMAN).format(Double.valueOf(args[2])))));
                                    }

                                } else {
                                    // No player found
                                    sender.sendMessage(Logger.colorFormat(Main.messageFile.getData().getString("eco-nodata")));
                                }
                            } catch (SQLException e) {
                            }
                            MySQL.close();
                        }
                    }
                }

            });


            return true;

        }

        return false;
    }
}
