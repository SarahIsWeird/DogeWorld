package com.sarahisweird.dogeverse.commands;

import com.sarahisweird.dogeverse.inventories.TownGui;
import com.sarahisweird.dogeverse.towns.Town;
import com.sarahisweird.dogeverse.towns.TownManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TownCmds {
    public static final TownGui townGui = new TownGui();

    private static String fmt(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static boolean execTownCmd(Player player, String[] args) {
        if (args.length == 0) {
            townGui.openInventory(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            player.sendMessage(fmt("&eAvailable subcommands: /town &ohelp, create"));
            return true;

        } else if (args[0].equalsIgnoreCase("create")) {
            if (!TownManager.initTownSetup(player)) {
                player.sendMessage(fmt("&cYou are already in the town setup process."));
                return true;
            }

        } else if (args[0].equalsIgnoreCase("leave")) {
            Town town = TownManager.getTown(player);
            if (!TownManager.removeMember(TownManager.getTown(player).name, player)) {
                player.sendMessage(fmt("&cYou're not a member of a town!"));
            }

            player.sendMessage(fmt("&aYou left &l" + town.name + "&a."));

            return true;
        }

        return true;
    }
}
