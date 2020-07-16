package com.sarahisweird.dogeverse.towns;

import com.sarahisweird.dogeverse.Dogeverse;
import com.sarahisweird.dogeverse.dbmanager.DBException;
import com.sarahisweird.dogeverse.dbmanager.DBManager;
import com.sarahisweird.dogeverse.tasks.RemoveTownSetupNextTick;
import com.sun.istack.internal.Nullable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TownManager {
    private static List<Town> towns;
    private static List<TownSetup> townsInSetup;

    private static List<Town> addedTowns;
    private static List<Town> removedTowns;

    /**
     * Loads the towns from the database. Must be called before any other method calls to the class.
     */
    public static void load() {
        townsInSetup = new ArrayList<>();
        addedTowns = new ArrayList<>();
        removedTowns = new ArrayList<>();

        try {
            towns = DBManager.loadTowns();
        } catch (DBException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Saves town data to the database. Must be called on shutdown, preferably more often than that.
     */
    public static void save() {
        for (Town town : addedTowns) {
            try {
                DBManager.addTown(town);
            } catch (DBException e) {
                e.printStackTrace();
            }
        }

        for (Town town : removedTowns) {
            try {
                DBManager.removeTown(town);
            } catch (DBException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a fully initialized town to the database.
     * @param town The town to be added.
     */
    public static void addTown(Town town) {
        towns.add(town);
        addedTowns.add(town);
    }

    /**
     * Adds a member to a town.
     * @param townName The name of the town the player should be added to.
     * @param player The player to be added.
     * @return Whether the update was successful.
     */
    public static boolean addMember(String townName, Player player) {
        for (Town town : towns) {
            if (town.name == townName) {
                town.addMember(player);
                return true;
            }
        }

        return false;
    }

    /**
     * Removes a member from a town.
     * @param townName The name of the town the player should be removed from.
     * @param player The player to be removed.
     * @return Whether the update was successful.
     */
    public static boolean removeMember(String townName, Player player) {
        for (Town town : towns) {
            if (town.name == townName) {
                town.removeMember(player);

                if (town.getMembers().size() == 0) {
                    removedTowns.add(town);
                    towns.remove(town);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Gets the town the player is in.
     * @param player The player to be checked.
     * @return The town they are in. If they aren't in any town, this returns null.
     */
    @Nullable
    public static Town getTown(Player player) {
        for (Town town : towns) {
            if (town.isMember(player))
                return town;
        }

        return null;
    }

    /**
     * Gets the town prefix from a player.
     * @param player The player to be checked.
     * @return The town prefix of the town they are in. Null if they are in no town.
     */
    @Nullable
    public static String getTownPrefix(Player player) {
        Town town = getTown(player);

        if (town != null) {
            return town.prefix;
        } else {
            return "";
        }
    }

    /**
     * Initializes a town setup.
     * @param player The player that started the town setup.
     * @return Whether the initialization was successful. False if there already is a town being setup by this player.
     */
    public static boolean initTownSetup(Player player) {
        if (isPlayerInTownCreation(player))
            return false;

        try {
            if (DBManager.getPlayerBalance(player) < 500) {
                player.sendMessage("§cYou need 500 Doge to create a town!");
                return true;
            }
        } catch (DBException e) {
            e.printStackTrace();
            return true;
        }

        townsInSetup.add(new TownSetup(player));

        return true;
    }

    /**
     * Checks whether a player is in the town setup process.
     * @param player The player to be checked.
     * @return Whether the player is in the process.
     */
    public static boolean isPlayerInTownCreation(Player player) {
        for (TownSetup townSetup : townsInSetup)
            if (townSetup.getPlayer() == player)
                return true;

        return false;
    }

    /**
     * Checks whether a town name is already in use.
     * @param townName The town name in question.
     * @return Whether it is already in use.
     */
    public static boolean isTownNameTaken(String townName) {
        for (Town town : towns)
            if (town.name.equalsIgnoreCase(townName))
                return true;

        return false;
    }

    /**
     * Checks whether a town prefix is already in use.
     * @param townPrefix The town prefix in question.
     * @return Whether it is already in use.
     */
    public static boolean isTownPrefixTaken(String townPrefix) {
        for (Town town : towns)
            if (town.prefix.equalsIgnoreCase(townPrefix))
                return true;

        return false;
    }

    /**
     * Calls the onInput function of the correct town setup.
     * @param message The full message sent to the chat.
     */
    public static void onInput(Player player, String message, Dogeverse dogeverse) {
        for (Iterator<TownSetup> iterator = townsInSetup.iterator(); iterator.hasNext(); ) {
            TownSetup townSetup = iterator.next();
            if (townSetup.player == player) {
                townSetup.onInput(message);

                if (townSetup.isDone()) {
                    (new RemoveTownSetupNextTick(townsInSetup, townSetup)).runTaskLater(dogeverse, 1L);
                }

                return;
            }
        }
    }
}
