package com.sarahisweird.dogeworld.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UtilityCmds {
    private static String fmt(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static boolean execColorsCmd(CommandSender sender) {
        String colorMessage = "";
        for (int i = 0; i < 16; i++) {
            colorMessage += fmt("&" + Integer.toHexString(i)) + "&" + Integer.toHexString(i) + fmt("&r ");
        }

        sender.sendMessage("Colors: " + colorMessage);

        sender.sendMessage("§k&k§r §l&l§r §m&m§r §n&n§r §o&o§r");

        return true;
    }

    public static boolean execBroadcastCommand(CommandSender sender, String[] args) {
        if (args.length == 0)
            return false;

        String message = "";

        for (String str : args)
            message += str + " ";

        sender.getServer().broadcastMessage(fmt(message));

        return true;
    }
}
