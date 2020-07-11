package com.sarahisweird.dogecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UtilityCommands {
    private static String fmt(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static boolean execColorsCmd(Player player) {
        String colorMessage = "";
        for (int i = 0; i < 16; i++) {
            colorMessage += fmt("&" + Integer.toHexString(i)) + "&" + Integer.toHexString(i) + fmt("&r ");
        }

        player.sendMessage("Colors: " + colorMessage);

        player.sendMessage("§k&k§r §l&l§r §m&m§r §n&n§r §o&o§r");

        return true;
    }
}
