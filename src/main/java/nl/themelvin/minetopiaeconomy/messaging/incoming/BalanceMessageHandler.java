package nl.themelvin.minetopiaeconomy.messaging.incoming;

import nl.themelvin.minetopiaeconomy.messaging.outgoing.BalanceMessage;
import nl.themelvin.minetopiaeconomy.models.Profile;

import java.util.concurrent.CompletableFuture;

import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class BalanceMessageHandler extends AbstractMessageHandler<BalanceMessage> {

    @Override
    public CompletableFuture<Void> execute(BalanceMessage message) {

        Profile profile = new Profile(message.getUuid()).get();

        if(profile == null) {
           profile = await(new Profile(message.getUuid()).init());
        }

        profile.setMoney(message.getBalance(), false);

        return completedFuture(null);

    }

}
