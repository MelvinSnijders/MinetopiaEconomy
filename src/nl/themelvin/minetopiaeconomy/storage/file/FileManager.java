package nl.themelvin.minetopiaeconomy.storage.file;

import nl.themelvin.minetopiaeconomy.Main;
import nl.themelvin.minetopiaeconomy.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class FileManager {

    private String directory;
    private String fileName;
    private File file;
    private FileConfiguration fileConfiguration;

    public FileManager(String directory, String fileName) {
        this.directory = directory;
        this.fileName = fileName;
    }

    public FileConfiguration getData() {
        return fileConfiguration;
    }

    public FileConfiguration loadFile() {

        if(!Main.getPlugin().getDataFolder().exists()) {
            Main.getPlugin().getDataFolder().mkdir();
        }

        File file = new File("plugins/MinetopiaEconomy" + directory, fileName);
        this.file = file;
        if(file.exists()) {
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
            return fileConfiguration;
        } else {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                Logger.consoleOutput(Logger.InfoLevel.ERROR, "Er ging iets fout bij het aanmaken van het bestand " + fileName + ", plugin uitgeschakeld!");
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(Main.getPlugin());
                return null;
            }
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
            return fileConfiguration;
        }
    }

    public void saveFile() {
        try {
            fileConfiguration.save(this.file);
        } catch (IOException e) {
            Logger.consoleOutput(Logger.InfoLevel.ERROR, "Er ging iets fout bij het opslaan van een bestand.");
            e.printStackTrace();
        }
    }

}
