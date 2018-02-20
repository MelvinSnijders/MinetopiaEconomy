package nl.themelvin.minetopiaeconomy.user;

import java.util.UUID;

/**
 * Class for storing UserData in object
 * @author TheMelvinNL
 */
public class UserData {

    public UUID uuid;
    public String name;
    public Double money;

    public UserData(UUID uuid, String name, Double money) {
        this.uuid = uuid;
        this.name = name;
        this.money = money;
    }

}
