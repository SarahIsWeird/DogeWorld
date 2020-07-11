package com.sarahisweird.dogeworld.config;

import com.sarahisweird.dogeworld.DogeWorld;

public class Config {
    private static DogeWorld plugin;

    /**
     * Loads the plugin's config. Must be called before any other calls to this class.
     * @param plugin The main class of the plugin.
     */
    public static void load(DogeWorld plugin) {
        Config.plugin.saveDefaultConfig();
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
     * Checks whether a rank is allowed to use formatting in a message.
     * @param rankName The rank to be checked.
     * @return Whether the rank is allowed to use formatting.
     */
    public static boolean canFormatMessage(String rankName) {
        return Config.plugin.getConfig().getBoolean("ranks." + rankName + ".colored_messages");
    }
}
