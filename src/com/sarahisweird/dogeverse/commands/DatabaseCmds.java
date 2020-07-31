package com.sarahisweird.dogeverse.commands;

import com.sarahisweird.dogeverse.dbmanager.DBException;
import com.sarahisweird.dogeverse.dbmanager.DBManager;
import com.sarahisweird.dogeverse.towns.TownManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DatabaseCmds {
    private static String fmt(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static boolean execDBCmd(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Available subcommands: /db create, dump");
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) {
                sender.sendMessage(fmt("&eUse /db create <dbname> to create a database."));
                sender.sendMessage(fmt("&eAvailable database names are: &oplayers, towns"));
                return true;
            }

            /* Player database creation */
            if (args[1].equalsIgnoreCase("players")) {
                if (args.length < 3 || (!args[2].equalsIgnoreCase("confirm") && !args[2].equalsIgnoreCase("c"))) {
                    sender.sendMessage(fmt("&c&oAre you really sure you want to create a new player database? If it"
                            + " already exists, it will be deleted!"));
                    sender.sendMessage(fmt("&c&oIf you are sure, please type"
                            + " &c/db create players confirm &oto confirm."));
                    return true;
                }

                try {
                    DBManager.createPlayerDatabase();
                } catch (DBException e) {
                    sender.sendMessage(fmt("&4Failed to create the player database."));
                    return true;
                }

                sender.sendMessage(fmt("&aSuccessfully created the player database."));
                return true;
            }

            /* Town database creation */
            else if (args[1].equalsIgnoreCase("towns")) {
                if (args.length < 3
                    || (!args[2].equalsIgnoreCase("confirm") && !args[2].equalsIgnoreCase("c"))) {
                    sender.sendMessage(fmt("&c&oAre you really sure you want to create a new town database? If it"
                            + " already exists, it will be deleted!"));
                    sender.sendMessage(fmt("&c&oIf you are sure, please type"
                            + " &c/db create towns confirm &oto confirm."));
                    return true;
                }

                try {
                    DBManager.createTownsDatabase();
                } catch (DBException e) {
                    e.printStackTrace();
                    sender.sendMessage(fmt("&4Failed to create the town database."));
                    return true;
                }

                TownManager.load();

                sender.sendMessage(fmt("&aSuccessfully created the town database."));
                return true;
            }

            /* Unknown database */
            else {
                sender.sendMessage(fmt("&eUse /db create <dbname> to create a database."));
                sender.sendMessage(fmt("&eAvailable database names are: &oplayers, towns"));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("dump")) {
            if (args.length < 2) {
                sender.sendMessage(fmt("&eUse /db dump <db name> to view it's contents."));
                sender.sendMessage(fmt("&eAvailable databases: &oplayers, towns"));
                return true;
            }

            if (args[1].equalsIgnoreCase("players")) {
                try {
                    sender.sendMessage(fmt("&aDumping player database..."));
                    List<String> lines = DBManager.dumpPlayers();

                    for (String str : lines)
                        sender.sendMessage(str);
                } catch (DBException e) {
                    e.printStackTrace();
                    sender.sendMessage(fmt("&4Failed to dump the database."));
                }
            } else if (args[1].equalsIgnoreCase("towns")) {
                try {
                    sender.sendMessage(fmt("&aDumping town database..."));
                    List<String> lines = DBManager.dumpTowns();

                    for (String str : lines)
                        sender.sendMessage(str);
                } catch (DBException e) {
                    e.printStackTrace();
                    sender.sendMessage(fmt("&4Failed to dump the database."));
                }
            }

            return true;
        } else if (args[0].equalsIgnoreCase("save")) {
            TownManager.save();

            sender.sendMessage("§aSuccessfully saved the database.");
            return true;
        }

        return false;
    }
}
