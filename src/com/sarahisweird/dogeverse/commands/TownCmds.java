package com.sarahisweird.dogeverse.commands;

import com.sarahisweird.dogeverse.Dogeverse;
import com.sarahisweird.dogeverse.dbmanager.DBException;
import com.sarahisweird.dogeverse.dbmanager.DBManager;
import com.sarahisweird.dogeverse.guis.GUIManager;
import com.sarahisweird.dogeverse.guis.InventoryTypes;
import com.sarahisweird.dogeverse.towns.Town;
import com.sarahisweird.dogeverse.towns.TownManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TownCmds {
    private static String fmt(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static boolean execTownCmd(Player player, String[] args) {
        if (args.length == 0) {
            GUIManager.openInventory(player, InventoryTypes.MAIN);
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (TownManager.getTown(player) != null) {
                player.sendMessage("§cYou are already in a town!");
                return true;
            }

            try {
                if (!TownManager.initTownSetup(player)) {
                    player.sendMessage(fmt("&cYou are already in the town setup process."));
                    return true;
                }

                if (DBManager.getPlayerBalance(player) < 500) {
                    player.sendMessage("§cYou need 500 Doge to create a town!");
                    return true;
                }
            } catch (DBException e) {
                e.printStackTrace();
            }

            return true;
        } else if (args[0].equalsIgnoreCase("leave")) {
            Town town = TownManager.getTown(player);

            if (town == null) {
                player.sendMessage(fmt("&cYou're not a member of a town."));
                return true;
            }

            town.removeMember(player);

            if (town.getMembers().size() == 0) {
                TownManager.removeTown(town);
            }

            TownManager.save();

            player.sendMessage(fmt("&aYou left &l" + town.name + "&a."));

            return true;
        } else if (args[0].equalsIgnoreCase("softleave")) {
            Player leavingPlayer;

            if (args.length > 1) {
                leavingPlayer = Dogeverse.plugin.getServer().getPlayer(args[1]);

                if (leavingPlayer == null) {
                    player.sendMessage("§cThat player isn't online or doesn't exist.");
                    return true;
                }
            } else {
                leavingPlayer = player;
            }

            Town town = TownManager.getTown(leavingPlayer);

            if (town == null) {
                player.sendMessage("§c" + (leavingPlayer == player ? "You're not" : leavingPlayer.getName() + " isn't")
                        + " a member of a town.");
                return true;
            }

            town.removeMember(leavingPlayer);

            TownManager.save();

            player.sendMessage("§aYou soft-left §l" + town.name + "§a.");

            return true;
        } else if (args[0].equalsIgnoreCase("join")) {
            String townName = "";

            for (int i = 1; i < args.length; i++) {
                townName += args[i] + " ";
            }

            townName = townName.substring(0, townName.length() - 1);

            Town town = TownManager.getTown(townName);

            if (town == null) {
                player.sendMessage("§cThat town doesn't exist.");
                return true;
            }

            town.addMember(player);

            TownManager.save();

            player.sendMessage("§aYou have joined §l" + townName + "§a.");
            return true;
        } else if (args[0].equalsIgnoreCase("forcejoin")) {
            String playerName = args[1];

            Player forcePlayer = Dogeverse.plugin.getServer().getPlayer(playerName);

            if (forcePlayer == null) {
                player.sendMessage("§cThat player isn't online or doesn't exist.");
                return true;
            }

            String townName = "";

            for (int i = 2; i < args.length; i++) {
                townName += args[i] + " ";
            }

            townName = townName.substring(0, townName.length() - 1);

            Town town = TownManager.getTown(townName);

            if (town == null) {
                player.sendMessage("§cThat town doesn't exist!");
                return true;
            }

            town.addMember(forcePlayer);

            TownManager.save();

            player.sendMessage("§a§l" + forcePlayer.getName() + " §ahas joined §l" + townName + "§a.");
            return true;
        }

        player.sendMessage(fmt("&eAvailable subcommands: /town help, create, "
                + "leave, softleave, join, forcejoin"));

        return true;
    }
}
