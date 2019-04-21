package nl.themelvin.minetopiaeconomy.models;

import lombok.Getter;
import lombok.Setter;
import nl.themelvin.minetopiaeconomy.MinetopiaEconomy;
import nl.themelvin.minetopiaeconomy.storage.Queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.ea.async.Async.await;
import static nl.themelvin.minetopiaeconomy.utils.Logger.*;

public class Profile extends Model<Profile> {

    // executeUpdate(): UPDATE, INSERT, DELETE
    // executeQuery(): SELECT

    @Getter
    private UUID uuid;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private double money = 0D;

    public Profile(UUID uuid) {
        this.uuid = uuid;
    }

    public Profile(String username) {
        this.username = username;
    }

    @Override
    public CompletableFuture<Boolean> create() {

        return CompletableFuture.supplyAsync(() -> {

            try {

                PreparedStatement preparedStatement = hikari.getConnection().prepareStatement(Queries.INSERT);
                preparedStatement.setString(1, this.uuid.toString());
                preparedStatement.setString(2, this.username);
                preparedStatement.setDouble(3, this.money);

                return preparedStatement.executeUpdate() == 1;

            } catch (SQLException e) {

                log(Severity.ERROR, "Er ging iets mis tijdens het invoegen van nieuwe data voor " + this.uuid);
                e.printStackTrace();

            }

            return false;

        });

    }

    @Override
    public CompletableFuture<Boolean> load(String value) {

        return CompletableFuture.supplyAsync(() -> {

            try {

                PreparedStatement preparedStatement = hikari.getConnection().prepareStatement(Queries.SELECT);
                preparedStatement.setString(1, value);
                preparedStatement.setString(2, value);

                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.

                return preparedStatement.executeUpdate() == 1;

            } catch (SQLException e) {

                log(Severity.ERROR, "Er ging iets mis tijdens het laden van data voor " + value);
                e.printStackTrace();

            }

        });

    }

    @Override
    public CompletableFuture<Boolean> update(String value) {
        return null;
    }

    @Override
    public CompletableFuture<Profile> init() {

        if(!await(this.load(this.uuid.toString())) && !await(this.create())) {




        }

        MinetopiaEconomy.getOnlineProfiles().put(this.uuid, this);

    }

}
