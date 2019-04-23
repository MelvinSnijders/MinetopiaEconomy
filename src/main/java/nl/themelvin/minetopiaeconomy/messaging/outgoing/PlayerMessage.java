package nl.themelvin.minetopiaeconomy.messaging.outgoing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.themelvin.minetopiaeconomy.messaging.AbstractMessage;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class PlayerMessage extends AbstractMessage {

    private UUID uuid;
    private boolean joined;

}
