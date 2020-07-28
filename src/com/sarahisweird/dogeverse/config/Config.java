package com.sarahisweird.dogeverse.config;

import com.sarahisweird.dogeverse.Dogeverse;

public class Config {
    private static Dogeverse plugin;

    private static double[] minTownSize;
    private static int townSetupCost;
    private static int maxTownSetupSize;

    /**
     * Loads the plugin's config. Must be called before any other calls to this class.
     * @param plugin The main class of the plugin.
     */
    public static void load(Dogeverse plugin) {
        Config.plugin = plugin;

        Config.plugin.saveDefaultConfig();


        minTownSize = new double[2];

        minTownSize[0] = Config.plugin.getConfig().getDouble("towns.size_min.x");
        minTownSize[1] = Config.plugin.getConfig().getDouble("towns.size_min.y");

        townSetupCost = Config.plugin.getConfig().getInt("towns.setup_cost");
        maxTownSetupSize = Config.plugin.getConfig().getInt("towns.setup_size_max");
    }

    /**
     * Saves the configuration file to disk. Must be called on shutdown.
     */
    public static void save() {
        Config.plugin.saveConfig();
    }

    /**
     * Checks whether a specific rank is the default rank.
     * @param rankName The rank to be checked.
     * @return Whether the rank is the default.
     */
    public static boolean getRankIsDefault(String rankName) {
        return Config.plugin.getConfig().getBoolean("ranks." + rankName + ".default");
    }

    /**
     * Fetches the prefix for a rank. This could be, for example, "[Member]".
     * @param rankName The rank to be checked.
     * @return The prefix for the rank, formatted with an ampersand (&) as the formatting character.
     */
    public static String getRankPrefix(String rankName) {
        return Config.plugin.getConfig().getString("ranks." + rankName + ".prefix");
    }

    /**
     * Fetches the username color for a rank.
     * @param rankName The rank to be checked.
     * @return The username color, prefixed with an ampersand (&).
     */
    public static String getRankNameColor(String rankName) {
        return Config.plugin.getConfig().getString("ranks." + rankName + ".name_color");
    }

    /**
     * Fetches the message color for a rank.
     * @param rankName The rank to be checked.
     * @return The message color, prefixed with an ampersand (&).
     */
    public static String getMessageColor(String rankName) {
        return Config.plugin.getConfig().getString("ranks." + rankName + ".message_color");
    }

    /**
     * Fetches the rank abbreviation.
     * @param rankName The rank to be checked.
     * @return The abbreviation, formatted with an ampersand (&).
     */
    public static String getRankAbbreviation(String rankName) {
        return Config.plugin.getConfig().getString("ranks." + rankName + ".abbreviation");
    }

    /**
     * Checks whether a rank is allowed to use formatting in a message.
     * @param rankName The rank to be checked.
     * @return Whether the rank is allowed to use formatting.
     */
    public static boolean canFormatMessage(String rankName) {
        return Config.plugin.getConfig().getBoolean("ranks." + rankName + ".colored_messages");
    }

    /**
     * Fetches the ordering letter of a rank.
     * @param rankName The rank to be checked.
     * @return The ordering letter.
     */
    public static String getRankOrder(String rankName) {
        return Config.plugin.getConfig().getString("ranks." + rankName + ".order");
    }

    /** Fetches the smallest possible transaction unit, also known as a Pup.
     * @return A Doge, in terms of pup. (100....0 pups = 1 doge)
     */
    public static int getPupsInDoge() {
        /* Rounded to prevent possible double inaccuracies from creeping up. */
        return (int) Math.round(1 / Config.plugin.getConfig().getDouble("economy.pup"));
    }

    /**
     * Fetches the minimum town size from the config.
     * @return The minimum town size.
     */
    public static double[] getMinimumTownSize() {
        return Config.minTownSize;
    }

    /**
     * Fetches the town setup cost from the config.
     * @return The town setup cost.
     */
    public static int getTownSetupCost() {
        return townSetupCost;
    }

    /**
     * Fetches the maximum (2d) blocks a town is allowed to possess upon creation.
     * @return The maximum size.
     */
    public static int getMaxTownSetupSize() {
        return maxTownSetupSize;
    }
}
