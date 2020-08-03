package nl.melvinsnijders.minetopiaeconomy.data.migration;

import java.util.UUID;

public abstract class Migration {

    protected UUID uuid;

    public Migration(UUID uuid) {
        this.uuid = uuid;
    }

    public abstract double restore();

}
