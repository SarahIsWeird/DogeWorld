package com.sarahisweird.dogecraft.commands;

import com.sarahisweird.dogecraft.dbmanager.DBManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager {

    public static boolean executeCommand(CommandSender sender, Command command, String label, String[] args) {
        /* Commands independent of (sender instanceof Player) */

        if (command.getName().equalsIgnoreCase("db")) {
            return DatabaseCmds.execDBCmd(sender, args);
        }

        /* Commands with (sender instanceof Player) */

        if (!(sender instanceof Player)) {
            sender.sendMessage("/" + label + " can only be run as a player. Sorry!");
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

        return false;
    }
}
