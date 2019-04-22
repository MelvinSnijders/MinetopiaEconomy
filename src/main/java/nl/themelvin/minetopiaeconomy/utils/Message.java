package nl.themelvin.minetopiaeconomy.utils;

import lombok.Getter;
import nl.themelvin.minetopiaeconomy.MinetopiaEconomy;

import static nl.themelvin.minetopiaeconomy.utils.Logger.__;

public class Message {

    @Getter private String id;

    public Message(String id) {

        this.id = id;

    }

    public String get() {

        return __(MinetopiaEconomy.messages().getString(this.id));

    }

    public String get(String replace, String with) {

        return __(MinetopiaEconomy.messages().getString(this.id)
                .replaceAll("%" + replace + "%", with)
        );

    }

    public String get(String replace1, String with1, String replace2, String with2) {

        return __(MinetopiaEconomy.messages().getString(this.id)
                .replaceAll("%" + replace1 + "%", with1)
                .replaceAll("%" + replace2 + "%", with2)
        );

    }

    public String get(String replace1, String with1, String replace2, String with2, String replace3, String with3) {

        return __(MinetopiaEconomy.messages().getString(this.id)
                .replaceAll("%" + replace1 + "%", with1)
                .replaceAll("%" + replace2 + "%", with2)
                .replaceAll("%" + replace3 + "%", with3)
        );

    }

}
