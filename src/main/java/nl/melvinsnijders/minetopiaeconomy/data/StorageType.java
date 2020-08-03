package nl.melvinsnijders.minetopiaeconomy.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.melvinsnijders.minetopiaeconomy.data.types.JSON;
import nl.melvinsnijders.minetopiaeconomy.data.types.MySQL;

@AllArgsConstructor
public enum StorageType {

    MYSQL(MySQL.class, "mysql"),
    JSON(nl.melvinsnijders.minetopiaeconomy.data.types.JSON.class, null),
    MONGODB(null, null); // Not implemented yet

    @Getter Class<? extends IStorage> storageClass;
    @Getter String configKey;

}
