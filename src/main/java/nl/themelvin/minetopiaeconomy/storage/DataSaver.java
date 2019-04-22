package nl.themelvin.minetopiaeconomy.storage;

import nl.themelvin.minetopiaeconomy.MinetopiaEconomy;
import nl.themelvin.minetopiaeconomy.models.Profile;

import java.util.HashMap;
import java.util.UUID;

public class DataSaver implements Runnable {

    @Override
    public void run() {

        HashMap<UUID, Profile> profiles = MinetopiaEconomy.getOnlineProfiles();

        for(Profile profile : profiles.values()) {

            profile.update();

        }

    }

}
