package com.sarahisweird.dogeverse.commands;

import com.sarahisweird.dogeverse.dbmanager.DBException;
import com.sarahisweird.dogeverse.dbmanager.DBManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCmds {
    public static boolean execHealCmd(Player player) {


        player.setFoodLevel(20);
        player.setHealth(20);
        player.setSaturation(20);

        player.sendMessage("Suddenly you feel okay again.");

        return true;
    }

    public static boolean execFlyCmd(Player player) {
        if (!player.isPermissionSet("dogeverse.fly.own")) {
            return CommandManager.fakeHelp(player);
        }

        if (player.getGameMode() != GameMode.SURVIVAL) {
            player.sendMessage("§eYou don't really want to change your ability to fly, do you?");
            return true;
        }

        boolean flying = false;

        try {
            flying = !DBManager.isPlayerFlying(player);
            DBManager.setPlayerFlying(player, flying);
        } catch (DBException e) {
            e.printStackTrace();
        }

        player.setAllowFlight(flying);
        player.setFlying(flying);

        if (flying) {
            player.sendMessage("Suddenly wings start to grow...");
        } else {
            player.sendMessage("Your wings seem to have disappeared again.");
        }

        return true;
    }

    public static boolean execNickCmd(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        } else if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cYou can only give a nickname to a player! Duh...");
                return true;
            }

            Player player = (Player) sender;

            try {
                DBManager.setPlayerNick(player, args[0]);
            } catch (DBException e) {
                player.sendMessage("§cFailed to change your nickname. Please contact a staff member.");
                return true;
            }

            player.sendMessage("§aSuccessfully changed your nickname to "
                    + ChatColor.translateAlternateColorCodes('&', args[0] + "&r") + ".");
            return true;
        } else if (args.length == 2) {
            Player player;

            try {
                player = sender.getServer().getPlayer(args[0]);
            } catch (Exception e) {
                sender.sendMessage("§cThat player doesn't exist or isn't online.");
                return true;
            }

            if (player == null) {
                sender.sendMessage("§cThat player doesn't exist.");
                return true;
            }

            try {
                DBManager.setPlayerNick(player, args[1]);
            } catch (DBException e) {
                sender.sendMessage("§cFailed to change " + args[0] + "'s nickname. Please contact a staff member.");
                return true;
            }

            sender.sendMessage("§aSuccessfully changed " + args[0] + "'s nickname to "
                    + ChatColor.translateAlternateColorCodes('&', args[1] + "&r") + ".");
            return true;
        } else {
            return false;
        }
    }
}
