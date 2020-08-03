package nl.melvinsnijders.minetopiaeconomy.data.migration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MigrationType {

    NONE("", null),
    ESSENTIALS("Essentials", EssentialsMigration.class);

    @Getter String pluginName;
    @Getter Class<? extends Migration> migrationClass;

}
