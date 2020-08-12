package nl.melvinsnijders.minetopiaeconomy.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import nl.melvinsnijders.minetopiaeconomy.MinetopiaEconomy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

import static nl.melvinsnijders.minetopiaeconomy.utils.Logger.__;

public enum Messages {

    PLAYER_NOT_FOUND("&cDeze speler is niet gevonden."),
    NO_PERMISSION("&cJe hebt geen toestemming om dit te doen!"),
    MUST_BE_PLAYER("&cJe moet een speler zijn om dit te doen!"),
    OWN_MONEY("&6Je hebt op dit moment &f&l€{money} &6op je rekening.");

    @Getter String defaultMessage;
    private static FileConfiguration messages;
    @Setter private static MinetopiaEconomy plugin;

    Messages(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @SneakyThrows
    public static void init() {

        File messagesFile = new File(plugin.getDataFolder() + "/messages.yml");

        if(!messagesFile.exists()) {
            messagesFile.createNewFile();
        }

        messages = YamlConfiguration.loadConfiguration(messagesFile);

        for(Messages message : values()) {
            messages.addDefault(message.getConfigKey(), message.getDefaultMessage());
            messages.save(messagesFile);
        }

    }

    public String get() {
        return __(messages.getString(this.getConfigKey()));
    }

    public String getConfigKey() {
        return this.name().toLowerCase().replaceAll("_", "-");
    }

    public static String formatMoney(double money) {
        return NumberFormat.getNumberInstance(Locale.GERMAN).format(money);
    }

}
