package nl.themelvin.minetopiaeconomy.models;

import com.zaxxer.hikari.HikariDataSource;
import nl.themelvin.minetopiaeconomy.MinetopiaEconomy;
import nl.themelvin.minetopiaeconomy.storage.Queries;
import nl.themelvin.minetopiaeconomy.utils.Configuration;
import nl.themelvin.minetopiaeconomy.utils.Logger;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

public abstract class Model<T> {

    public static HikariDataSource hikari;

    public abstract CompletableFuture<Boolean> create();

    public abstract CompletableFuture<Boolean> load(String value);
    public abstract CompletableFuture<Boolean> load();

    public abstract CompletableFuture<Boolean> update(String value);
    public abstract CompletableFuture<Boolean> update();

    public abstract CompletableFuture<T> init();

    public static boolean startPool() {

        hikari = new HikariDataSource();

        Configuration config = MinetopiaEconomy.configuration();
        String storageType = MinetopiaEconomy.configuration().getString("storage");

        switch (storageType.toLowerCase()) {

            case "mysql":
                hikari.setDriverClassName("com.mysql.jdbc.Driver");
                hikari.setJdbcUrl("jdbc:mysql://" + config.getString("database.host") + ":" + config.getInt("database.port") + "/" + config.getString("database.database"));
                hikari.setUsername(config.getString("database.username"));
                hikari.setPassword(config.getString("database.password"));
                break;

            case "sqlite":

                hikari.setDriverClassName("org.sqlite.JDBC");
                hikari.setJdbcUrl("jdbc:sqlite:plugins/MinetopiaEconomy/data/economy.db");
                break;

        }

        hikari.setConnectionTestQuery("SELECT 1");
        hikari.setMaximumPoolSize(48);
        hikari.setMaxLifetime(60000);

        try {

            Statement statement = hikari.getConnection().createStatement();
            statement.executeUpdate(Queries.CREATE_PROFILES_TABLE);

        } catch (SQLException e) {

            Logger.log(Logger.Severity.ERROR, "Kon geen standaard 'profiles' tabel aanmaken.");
            e.printStackTrace();
            return false;

        }

        return hikari.isRunning();

    }

    public static void closePool() {

        hikari.close();

    }

}
