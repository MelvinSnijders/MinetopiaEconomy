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
import static java.util.concurrent.CompletableFuture.completedFuture;
import static nl.themelvin.minetopiaeconomy.utils.Logger.*;

public class Profile extends Model<Profile> {

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

    public Profile get() {

        return MinetopiaEconomy.getOnlineProfiles().get(this.uuid);

    }

    public void unCache() {

        MinetopiaEconomy.getOnlineProfiles().remove(this.uuid);

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

                if(!resultSet.next()) {
                    return false;
                }

                this.username = resultSet.getString("username");
                this.money = resultSet.getDouble("money");

                return true;

            } catch (SQLException e) {

                log(Severity.ERROR, "Er ging iets mis tijdens het laden van data voor " + value);
                e.printStackTrace();

            }

            return false;

        });

    }

    @Override
    public CompletableFuture<Boolean> load() {

        return this.load(this.uuid.toString());

    }

    @Override
    public CompletableFuture<Boolean> update(String value) {

        return CompletableFuture.supplyAsync(() -> {

            try {

                PreparedStatement preparedStatement = hikari.getConnection().prepareStatement(Queries.UPDATE);
                preparedStatement.setString(1, this.username);
                preparedStatement.setDouble(2, this.money);
                preparedStatement.setString(3, value);
                preparedStatement.setString(4, value);

                return preparedStatement.executeUpdate() == 1;

            } catch (SQLException e) {

                log(Severity.ERROR, "Er ging iets mis tijdens het updaten van data voor " + value);
                e.printStackTrace();

            }

            return false;

        });

    }

    @Override
    public CompletableFuture<Boolean> update() {

        return this.update(this.uuid.toString());

    }

    @Override
    public CompletableFuture<Profile> init() {

        if(!await(this.load()) && !await(this.create())) {

            this.money = 0;

        }

        MinetopiaEconomy.getOnlineProfiles().put(this.uuid, this);
        return completedFuture(this);

    }

}
