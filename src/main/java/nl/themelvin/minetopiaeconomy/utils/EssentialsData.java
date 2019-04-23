package nl.themelvin.minetopiaeconomy.utils;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class EssentialsData {

    private UUID uuid;

    public EssentialsData(UUID uuid) {

        this.uuid = uuid;

    }

    public double restore() {

        File file = new File("plugins/Essentials/userdata", this.uuid.toString() + ".yml");

        if(!file.exists()) {
            return 0D;
        }

        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        String moneyString = fileConfiguration.getString("money");

        if(moneyString == null) {
            return 0D;
        }

        return NumberUtils.toDouble(moneyString);

    }


}
