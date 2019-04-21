package nl.themelvin.minetopiaeconomy.storage;

public class Queries {

    public final static String CREATE_PROFILES_TABLE =
            "CREATE TABLE IF NOT EXISTS `profiles` (" +
            "  `uuid` VARCHAR(36) NOT NULL," +
            "  `username` VARCHAR(16) NULL," +
            "  `money` DOUBLE NULL," +
            "  PRIMARY KEY (`uuid`))";

    public final static String INSERT = "INSERT INTO `profiles` (uuid, username, money) VALUES (?, ?, ?)";

    public final static String SELECT = "SELECT * FROM `profiles` WHERE uuid = ? OR username = ?";

    public final static String UPDATE = "UPDATE `profiles` SET username = ?, money = ? WHERE uuid = ? OR username = ?";

}
