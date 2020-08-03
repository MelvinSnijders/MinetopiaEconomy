package nl.melvinsnijders.minetopiaeconomy.data.migration;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class EssentialsMigration extends Migration {

    public EssentialsMigration(UUID uuid) {
        super(uuid);
    }

    @Override
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
