package nl.themelvin.minetopiaeconomy.storage.file;

import nl.themelvin.minetopiaeconomy.Main;
import nl.themelvin.minetopiaeconomy.utils.Logger;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class FileManager {

    public static FileConfiguration loadFile(String directory, String fileName) {

        if(!Main.getPlugin().getDataFolder().exists()) {
            Main.getPlugin().getDataFolder().mkdir();
        }

        File file = new File(Main.getPlugin().getDataFolder() + directory, fileName);

        if(file.exists()) {
            return YamlConfiguration.loadConfiguration(file);
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Logger.consoleOutput(Logger.InfoLevel.ERROR, "Er ging iets fout bij het aanmaken van het bestand " + fileName + ", plugin uitgeschakeld!");
                Bukkit.getPluginManager().disablePlugin(Main.getPlugin());
                return null;
            }
            return YamlConfiguration.loadConfiguration(file);
        }
    }

    public static void saveFile(FileConfiguration file) {
        try {
            file.save(new File(file.getCurrentPath()));
        } catch (IOException e) {
            Logger.consoleOutput(Logger.InfoLevel.ERROR, "Er ging iets fout bij het opslaan van een bestand.");
        }
    }

}
