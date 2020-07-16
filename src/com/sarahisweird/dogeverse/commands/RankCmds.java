package com.sarahisweird.dogeverse.commands;

import com.sarahisweird.dogeverse.ranks.Rank;
import com.sarahisweird.dogeverse.ranks.RankManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCmds {
    public static boolean execRankCmd(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§7Available subcommands: /rank set");
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (args.length != 3) {
                sender.sendMessage("§cUsage: /rank set <player> <rank>");
                return true;
            }

            Rank newRank = RankManager.rankNameToRank(args[2]);

            if (newRank == null) {
                sender.sendMessage("§cYou have entered an invalid rank name.");
                sender.sendMessage("§eAvailable ranks: §omember, donator, juniorhelper, helper, "
                        + "juniormod, moderator, developer, cm, owner§r");
                return true;
            }

            Player newRankPlayer;

            try {
                newRankPlayer = sender.getServer().getPlayer(args[1]);
            } catch (Exception e) {
                sender.sendMessage("§cThat player doesn't exist or isn't online.");
                return true;
            }

            newRankPlayer.getServer().getScoreboardManager().getMainScoreboard().getTeam(newRank.getOrder())
                    .addEntry(newRankPlayer.getName());

            if (RankManager.setRank(newRankPlayer, newRank)) {
                sender.sendMessage("§aSuccessfully set " + args[1] + "'s rank.");
            } else {
                sender.sendMessage("§cCouldn't set " + args[1] + "'s rank.");
            }

            return true;
        }

        return true;
    }
}
