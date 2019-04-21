package nl.themelvin.minetopiaeconomy.storage;

public class Queries {

    public final static String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS `profiles` (" +
            "  `uuid` VARCHAR(36) NOT NULL," +
            "  `username` VARCHAR(16) NULL," +
            "  `money` DOUBLE NULL," +
            "  PRIMARY KEY (`uuid`))";



}
