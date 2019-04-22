package nl.themelvin.minetopiaeconomy.commands;

import nl.themelvin.minetopiaeconomy.models.Model;
import nl.themelvin.minetopiaeconomy.storage.Queries;
import nl.themelvin.minetopiaeconomy.utils.Message;
import org.bukkit.command.CommandSender;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static nl.themelvin.minetopiaeconomy.utils.Logger.*;

public class BalancetopCommand extends AbstractCommand {

    public BalancetopCommand(CommandSender sender, String alias, String[] args) {
        super(sender, alias, args);
    }

    @Override
    public CompletableFuture<Void> execute() {

        if(!this.senderHasPermissions("minetopiaeconomy.balancetop")) {

            this.sender.sendMessage(new Message("no-perm").get());
            return completedFuture(null);

        }

        this.sender.sendMessage(new Message("baltop-calculate").get());

        try {

            Statement statement = Model.hikari.getConnection().createStatement();
            CompletableFuture<ResultSet> resultFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return statement.executeQuery(Queries.ORDER_MONEY);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            });

            ResultSet result = await(resultFuture);

            int currentScore = 1;

            while(result.next()) {

                String formattedMoney = NumberFormat.getNumberInstance(Locale.GERMAN).format(result.getDouble("money"));
                this.sender.sendMessage(new Message("baltop-result").get("number", String.valueOf(currentScore), "name", result.getString("username"), "money", formattedMoney));
                currentScore++;

            }

        } catch (SQLException e) {

            this.sender.sendMessage(new Message("sql-error").get());
            log(Severity.ERROR, "Fout tijdens het verkrijgen van de 10 rijkste personen. (/balancetop)");
            e.printStackTrace();

        }

        return completedFuture(null);

    }

}
