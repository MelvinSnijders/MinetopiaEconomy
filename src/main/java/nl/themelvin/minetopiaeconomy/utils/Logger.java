package nl.themelvin.minetopiaeconomy.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {

    public static String __(String string) {

        return ChatColor.translateAlternateColorCodes('&', string);

    }

    /**
     * Send a error message to the console.
     * @param severity Severity level of the message.
     */


    public static void log(Severity severity, String string) {

        Bukkit.getServer().getConsoleSender().sendMessage(__(severity.prefix + " &r") + string);

    }

    /**
     * Enum of severity levels.
     */

    public enum Severity {

        INFO("&2[MineNationXL INFO]"), WARNING("&6[MineNationXL WARNING]"), ERROR("&4[MineNationXL ERROR]"), DEBUG("&5[MineNationXL DEBUG]");

        String prefix;

        Severity(String prefix) {

            this.prefix = prefix;

        }

    }

}
