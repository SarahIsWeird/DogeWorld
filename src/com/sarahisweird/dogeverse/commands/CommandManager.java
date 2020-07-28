package com.sarahisweird.dogeverse.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager {

    /**
     * Calls the specific command. Must be called in the onCommand method of the main class.
     *
     * This is a workaround to make command calling tidier. CommandExecutors don't seem to work yet in 1.15+ for whatever reason.
     * @param sender The command sender.
     * @param command The command.
     * @param label The command label.
     * @param args The arguments passed.
     * @return Whether the command successfully ran. True will not show the usage message, false will. Must be returned from the onCommand method.
     */
    public static boolean executeCommand(CommandSender sender, Command command, String label, String[] args) {
        /* Commands independent of (sender instanceof Player) */

        if (command.getName().equalsIgnoreCase("db")) {
            return DatabaseCmds.execDBCmd(sender, args);
        }

        else if (command.getName().equalsIgnoreCase("rank")) {
            return RankCmds.execRankCmd(sender, args);
        }

        else if (command.getName().equalsIgnoreCase("nick")) {
            return PlayerCmds.execNickCmd(sender, args);
        }

        else if (command.getName().equalsIgnoreCase("colors")) {
            return UtilityCmds.execColorsCmd(sender);
        } else if (command.getName().equalsIgnoreCase("broadcast")
                || command.getName().equalsIgnoreCase("bc")
                || command.getName().equalsIgnoreCase("say")) {
            return UtilityCmds.execBroadcastCommand(sender, args);
        }

        /* Commands with (sender instanceof Player) */

        if (!(sender instanceof Player)) {
            sender.sendMessage("§c/" + label + " can only be run as a player. Sorry!");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("rtp")) {
            return TeleportCmds.execRandomTeleportCmd(player);
        } else if (command.getName().equalsIgnoreCase("spawn")) {
            return TeleportCmds.execSpawnCmd(player);
        }

        else if (command.getName().equalsIgnoreCase("heal")) {
            return PlayerCmds.execHealCmd(player);
        } else if (command.getName().equalsIgnoreCase("fly")) {
            return PlayerCmds.execFlyCmd(player);
        }

        else if (command.getName().equalsIgnoreCase("town")) {
            return TownCmds.execTownCmd(player, args);
        }

        else if (command.getName().equalsIgnoreCase("balance")
                || command.getName().equalsIgnoreCase("bal")
                || command.getName().equalsIgnoreCase("doge")
                || command.getName().equalsIgnoreCase("b")
                || command.getName().equalsIgnoreCase("d")) {
            return BalanceCmds.execBalanceCmd(player, args);
        }

        return false;
    }
}