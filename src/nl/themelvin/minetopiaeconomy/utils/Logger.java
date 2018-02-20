package nl.themelvin.minetopiaeconomy.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Utility to sendMessages and formats
 */
public class Logger {

    /**
     * Format a String with color codes
     * @param message Message to format
     * @return Formatted string
     */
    public static String colorFormat(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }


    /**
     * Get message when someone has no permission
     * @return No permission message as String
     */
    public static String noPermission() {
        return colorFormat("&cJe hebt geen permissie om dit te doen.");
    }

    /**
     * Send an formatted message to the console with an provided info level
     * Logger info levels are provided by {@link InfoLevel}
     * @param infoLevel info level of the message
     * @param message Message to output in the console
     */
    public static void consoleOutput(InfoLevel infoLevel, String message) {
        String prefix = "";
        switch (infoLevel) {
            case INFO:
                prefix = "&a[MinetopiaEconomy Info] ";
                break;
            case WARNING:
                prefix = "&e[MinetopiaEconomy Warning] ";
                break;
            case ERROR:
                prefix = "&c[MinetopiaEconomy Error] ";
                break;
            case DEBUG:
                prefix = "&b[MinetopiaEconomyDebug] ";
                break;
        }
        Bukkit.getConsoleSender().sendMessage(colorFormat(prefix + "&f" + message));
    }

    /**
     * Info level for information providing messages
     * @link {@link Logger}
     */
    public enum InfoLevel {

        INFO,

        WARNING,

        ERROR,

        DEBUG

    }

}
