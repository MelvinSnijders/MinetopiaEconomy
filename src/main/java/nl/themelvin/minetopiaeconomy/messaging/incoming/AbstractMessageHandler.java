package nl.themelvin.minetopiaeconomy.messaging.incoming;

import nl.themelvin.minetopiaeconomy.messaging.AbstractMessage;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractMessageHandler<T extends AbstractMessage> {

    public abstract CompletableFuture<Void> execute(T message);

}
