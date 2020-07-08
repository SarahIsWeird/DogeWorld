package com.sarahisweird.dogecraft.commands;

import com.sarahisweird.dogecraft.dbmanager.DBException;
import com.sarahisweird.dogecraft.dbmanager.DBManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DatabaseCmds {
    private static String fmt(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static boolean execDBCmd(CommandSender sender, String[] args, DBManager dbManager) {
        if (args.length == 0) {
            sender.sendMessage("Available subcommands: /db create, dump");
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2 || !args[1].equalsIgnoreCase("confirm")) {
                sender.sendMessage(fmt("&c&oAre you really sure you want to create a new database? If a database"
                        + "already exists, it will be nuked!"));
                sender.sendMessage(fmt("&c&oIf you're sure, type &c/db create confirm&o."));
                return true;
            }

            try {
                dbManager.createPlayerDatabase();
            } catch (DBException e) {
                e.printStackTrace();
                sender.sendMessage(fmt("&4The database couldn't be created. See the logs for more info."));
                return true;
            }

            sender.sendMessage(fmt("&aSuccessfully created the database."));
            return true;
        } else if (args[0].equalsIgnoreCase("dump")) {
            try {
                sender.sendMessage("Dumping database...");
                List<String> lines = dbManager.dump();

                for (String str : lines) {
                    sender.sendMessage(str);
                }
            } catch (DBException e) {
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }
}
