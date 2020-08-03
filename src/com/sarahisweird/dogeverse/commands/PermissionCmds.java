package com.sarahisweird.dogeverse.commands;

import com.sarahisweird.dogeverse.Dogeverse;
import com.sarahisweird.dogeverse.permissions.PermissionManager;
import com.sarahisweird.dogeverse.ranks.Rank;
import com.sarahisweird.dogeverse.ranks.RankManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionCmds {
    public static boolean onPermissionCmd(CommandSender sender, String[] args) {
        if (sender.getServer().getConsoleSender() != sender)
            if (!PermissionManager.hasAnySubPermission((Player) sender, "dogeverse.permissions"))
                return CommandManager.fakeHelp(sender);

        if (args.length == 0) {
            sender.sendMessage("Available subcommands: /perm list, plist");
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            if (sender.getServer().getConsoleSender() != sender)
                if (!sender.isPermissionSet("dogeverse.permissions.list"))
                    return CommandManager.fakeHelp(sender);

            if (args.length < 2) {
                sender.sendMessage("Usage: /perm list <rank>");
                return true;
            }

            Rank rank = RankManager.rankNameToRank(args[1]);

            if (rank == null) {
                sender.sendMessage("That rank doesn't exist.");
                return true;
            }

            for (String node : PermissionManager.getPermissions(rank)) {
                sender.sendMessage(": " + node);
            }

            return true;
        } else if (args[0].equalsIgnoreCase("plist")) {
            if (sender.getServer().getConsoleSender() != sender)
                if (!sender.isPermissionSet("dogeverse.permissions.plist"))
                    return CommandManager.fakeHelp(sender);

            if (args.length < 2) {
                sender.sendMessage("Usage: /perm plist <player>");
                return true;
            }

            Player player = Dogeverse.plugin.getServer().getPlayer(args[1]);

            if (player == null) {
                sender.sendMessage("That player isn't online or doesn't exist.");
                return true;
            }

            for (String node : PermissionManager.getPermissions(player)) {
                sender.sendMessage(": " + node);
            }

            return true;
        } else {
            return onPermissionCmd(sender, new String[1]); // Print usage string
        }
    }
}
