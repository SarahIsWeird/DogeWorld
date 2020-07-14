package com.sarahisweird.dogeworld.commands;

import com.sarahisweird.dogeworld.dbmanager.DBException;
import com.sarahisweird.dogeworld.dbmanager.DBManager;
import org.bukkit.entity.Player;

public class BalanceCmds {
    public static boolean execBalanceCmd(Player player, String[] args) {
        if (args.length == 0) {
            try {
                player.sendMessage("§6You have " + DBManager.getPlayerBalance(player) + " Doge.");
                return true;
            } catch (DBException e) {
                e.printStackTrace();
                return false;
            }
        } else if (args[0].equalsIgnoreCase("add")) {
            if (args.length != 3) {
                player.sendMessage("§eUsage: /balance add <player> <amount>");
                return true;
            }

            Player receivingPlayer = player.getServer().getPlayer(args[1]);

            if (receivingPlayer == null) {
                player.sendMessage("§cThat player couldn't be found.");
                return true;
            }

            try {
                DBManager.addBalance(receivingPlayer, Integer.parseInt(args[2]));
                player.sendMessage("§aSuccessfully added " + args[2] + " Doge to " + args[1] + "'s balance.");
            } catch (DBException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length != 3) {
                player.sendMessage("§eUsage: /balance remove <player> <amount>");
                return true;
            }

            Player receivingPlayer = player.getServer().getPlayer(args[1]);

            if (receivingPlayer == null) {
                player.sendMessage("§cThat player couldn't be found.");
                return true;
            }

            try {
                if (!DBManager.removeBalance(receivingPlayer, Integer.parseInt(args[2]))) {
                    player.sendMessage("§eThe player doesn't have enough money to remove that much!");
                    return true;
                }

                player.sendMessage("§aSuccessfully removed " + args[2] + " Doge from " + args[1] + "'s balance.");
            } catch (DBException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length != 3) {
                player.sendMessage("§eUsage: /balance set <player> <amount>");
                return true;
            }

            Player receivingPlayer = player.getServer().getPlayer(args[1]);

            if (receivingPlayer == null) {
                player.sendMessage("§cThat player couldn't be found.");
                return true;
            }

            try {
                DBManager.setPlayerBalance(receivingPlayer, Integer.parseInt(args[2]));
                player.sendMessage("§aSuccessfully set " + args[1] + "'s balance to " + args[2] + ".");
            } catch (DBException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        return false;
    }
}
