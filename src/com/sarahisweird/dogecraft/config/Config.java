package com.sarahisweird.dogecraft.config;

import com.sarahisweird.dogecraft.DogeCraft;

public class Config {
    private static DogeCraft plugin;

    public static void load(DogeCraft plugin) {
        Config.plugin = plugin;

        Config.plugin.saveDefaultConfig();
    }

    public static void save() {
        Config.plugin.saveConfig();
    }

    public static boolean getRankIsDefault(String rankName) {
        return Config.plugin.getConfig().getBoolean("ranks." + rankName + ".default");
    }

    public static String getRankPrefix(String rankName) {
        return Config.plugin.getConfig().getString("ranks." + rankName + ".prefix");
    }

    public static String getRankNameColor(String rankName) {
        return Config.plugin.getConfig().getString("ranks." + rankName + ".name_color");
    }

    public static String getMessageColor(String rankName) {
        return Config.plugin.getConfig().getString("ranks." + rankName + ".message_color");
    }
}
