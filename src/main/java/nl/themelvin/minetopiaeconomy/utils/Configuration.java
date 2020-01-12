package nl.themelvin.minetopiaeconomy.utils;

import nl.themelvin.minetopiaeconomy.MinetopiaEconomy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

import static nl.themelvin.minetopiaeconomy.utils.Logger.*;

public class Configuration extends YamlConfiguration {

    private final File file;
    private final String fileName;

    public Configuration(String file) {

        this.file = new File(MinetopiaEconomy.getPlugin().getDataFolder() + "/" + file);
        this.fileName = file;

        this.saveDefault();
        this.load();

    }

    private void saveDefault() {

        if(!this.file.exists()) {

            try {

                InputStream stream = MinetopiaEconomy.getPlugin().getResource(this.fileName);
                byte[] buffer = new byte[stream.available()];
                stream.read(buffer);

                this.file.getParentFile().mkdirs();
                this.file.createNewFile();

                OutputStream outStream = new FileOutputStream(this.file);
                outStream.write(buffer);

            } catch (Exception e) {

                log(Severity.ERROR, "Kon configuratie niet opslaan (standaard), plugin uitgeschakeld.");
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(MinetopiaEconomy.getPlugin());

            }

        }

    }

    private void load() {

        try {

            this.load(this.file);

        } catch (IOException | InvalidConfigurationException e) {

            e.printStackTrace();

        }

    }

}
