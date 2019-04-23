package nl.themelvin.minetopiaeconomy.messaging;

import com.google.gson.Gson;

public abstract class AbstractMessage {

    public String serialize() {

        Gson gson = new Gson();
        return gson.toJson(this);

    }

}
