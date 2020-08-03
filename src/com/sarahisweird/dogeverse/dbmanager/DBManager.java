package com.sarahisweird.dogeverse.dbmanager;

import com.sarahisweird.dogeverse.Dogeverse;
import com.sarahisweird.dogeverse.config.Config;
import com.sarahisweird.dogeverse.towns.Town;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static Connection playersConn;
    private static Statement playersStmt;

    /**
     * Loads the database. Must be called before any requests, or it won't work. (Static constructor)
     */
    public static void loadDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");

            playersConn = DriverManager.getConnection("jdbc:sqlite:server.db");
            playersStmt = playersConn.createStatement();
        } catch (Exception e) {
            Dogeverse.logger.severe("Failed to connect to the SQLite database.");
            e.printStackTrace();
        }
    }

    /**
     * Safely unloads the database. If this isn't called, data might be lost.
     */
    public static void disable() {
        try {
            playersStmt.closeOnCompletion();
            playersConn.close();
            Dogeverse.logger.info("Database disabled.");
        } catch (SQLException e) {
            e.printStackTrace();

            Dogeverse.logger.severe("Failed to properly close the SQLite database.");
        }
    }

    /**
     * Creates the "players" table in the database. Will delete any previous table named "players".
     * @throws DBException
     */
    public static void createPlayerDatabase() throws DBException {
        try {
            playersStmt.execute("DROP TABLE IF EXISTS players");
            playersStmt.execute("CREATE TABLE players (" +
                    "UUID     CHAR(36) PRIMARY KEY NOT NULL, " +
                    "flying   TINYINT              NOT NULL, " +
                    "rank     TINYINT              NOT NULL, " +
                    "nickname TEXT                         , " +
                    "balance  INT2                 NOT NULL)");
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    /**
     * Creates the "towns" table in the database. Will delete any previous table named "towns".
     * @throws DBException
     */
    public static void createTownsDatabase() throws DBException {
        try {
            playersStmt.execute("DROP TABLE IF EXISTS towns");
            playersStmt.execute("CREATE TABLE towns (" +
                    "town_id  INTEGER PRIMARY KEY  AUTOINCREMENT, " +
                    "name     TEXT                 NOT NULL, " +
                    "prefix   TEXT                 NOT NULL, " +
                    "owner    TEXT                 NOT NULL, " +
                    "members  TEXT                 NOT NULL, " +
                    "balance  INT4                 NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }
    }

    /**
     * Dumps the player database contents.
     * @return A list of the contents. Formatted, each line is a row in the table.
     * @throws DBException Only thrown if the database couldn't be accessed.
     */
    public static List<String> dumpPlayers() throws DBException {
        List<String> lines = new ArrayList<>();

        try {
            ResultSet resultSet = playersStmt.executeQuery("SELECT * FROM players");

            while (resultSet.next()) {
                lines.add(resultSet.getString("UUID") + ": "
                        + "flying(" + resultSet.getInt("flying") + "), "
                        + "rank(" + resultSet.getInt("rank") + "), "
                        + "nickname(" + resultSet.getString("nickname") + "), "
                        + "balance(" + resultSet.getInt("balance") + ")");
            }
        } catch (SQLException e) {
            throw new DBException();
        }

        return lines;
    }

    /**
     * Dumps the town database contents.
     * @return A list of the contents. Formatted, each line is a row in the table.
     * @throws DBException Only thrown if the database couldn't be accessed.
     */
    public static List<String> dumpTowns() throws DBException {
        List<String> lines = new ArrayList<>();

        try {
            ResultSet resultSet = playersStmt.executeQuery("SELECT * FROM towns");

            while (resultSet.next()) {
                lines.add(resultSet.getInt("town_id") + ": "
                        + "name(" + resultSet.getString("name") + "), "
                        + "prefix(" + resultSet.getString("prefix") + "), "
                        + "owner(" + resultSet.getString("owner") + "), "
                        + "members(" + resultSet.getString("members") + "), "
                        + "balance(" + resultSet.getInt("balance") + ")");
            }
        } catch (SQLException e) {
            throw new DBException();
        }

        return lines;
    }

    /**
     * Prepares future calls for a player to the database.
     * Must be called before any requests for a player are made, or silent errors might creep up.
     * @param player The player to load.
     * @return Whether new data for the player was created.
     * @throws DBException Thrown if the player couldn't be loaded, possibly because loadDatabase() wasn't called.
     */
    public static boolean loadPlayer(Player player) throws DBException {
        String uuid = player.getUniqueId().toString();

        try {
            ResultSet resultSet = playersStmt.executeQuery("SELECT COUNT(UUID) FROM players " +
                    "WHERE UUID = '" + uuid + "'");
            resultSet.next();

            if (resultSet.getInt(1) == 1) {
                resultSet.close();

                return false;
            }

            playersStmt.execute("INSERT INTO players (UUID, flying, rank, nickname, balance) "
                    + "VALUES ('" + uuid + "', 0, 0, '', 20000);");

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }
    }

    /**
     * Checks if a player was flying before they logged off.
     * @param player The player in question.
     * @return Whether they were flying.
     * @throws DBException Only thrown if the query failed, possibly because loadDatabase() wasn't called. Never thrown because of the value not previously being set.
     */
    public static boolean isPlayerFlying(Player player) throws DBException {
        String uuid = player.getUniqueId().toString();
        boolean returnVal;

        try {
            ResultSet resultSet = playersStmt.executeQuery("SELECT flying FROM players WHERE UUID='" + uuid + "'");
            resultSet.next(); // UUID = unique -> no while loop

            returnVal = resultSet.getBoolean("flying");
        } catch (SQLException e) {
            Dogeverse.logger.severe(e.getMessage());
            e.printStackTrace();
            throw new DBException();
        }

        return returnVal;
    }

    /**
     * Changes the flying flag in the database for a specific player.
     * @param player The player to be modified.
     * @param flying The value to be set.
     * @throws DBException Only thrown if the update failed, possibly because loadDatabase() wasn't called.
     */
    public static void setPlayerFlying(Player player, boolean flying) throws DBException {
        String uuid = player.getUniqueId().toString();

        try {
            playersStmt.execute("UPDATE players SET flying = " + (flying ? 1 : 0) + " WHERE UUID = '" + uuid + "'");
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    /**
     * Fetches a player's rank ID from the database.
     * @param player The player to be checked.
     * @return The rank ID.
     * @throws DBException Only thrown if the query failed, possibly because loadDatabase() wasn't called, never because the value wasn't previously set.
     */
    public static int getPlayerRank(Player player) throws DBException {
        String uuid = player.getUniqueId().toString();

        int retVal;

        try {
            ResultSet resultSet = playersStmt.executeQuery("SELECT rank FROM players WHERE UUID='" + uuid + "'");
            resultSet.next();

            retVal = resultSet.getInt("rank");
        } catch (SQLException e) {
            throw new DBException();
        }

        return retVal;
    }

    /**
     * Sets the rank for a player.
     * @param player The player to be modified.
     * @param rankId The rank ID that's supposed to be set.
     * @throws DBException Only thrown if the update failed, possibly because loadDatabase() wasn't called.
     */
    public static void setPlayerRank(Player player, int rankId) throws DBException {
        String uuid = player.getUniqueId().toString();

        try {
            playersStmt.execute("UPDATE players SET rank = " + rankId + " WHERE UUID = '" + uuid + "'");
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    /**
     * Fetches the nickname of a player from the database.
     * @param player The player in question.
     * @return The nickname, formatted with an ampersand (&) as the formatting character.
     * @throws DBException Only thrown if the query failed, possibly because loadDatabase() wasn't called, never because it wasn't previously set.
     */
    public static String getPlayerNickname(Player player) throws DBException {
        String uuid = player.getUniqueId().toString();
        String playerNick = "";

        try {
            ResultSet resultSet = playersStmt.executeQuery("SELECT nickname FROM players "
                    + "WHERE UUID = '" + uuid + "'");
            resultSet.next();

            playerNick = resultSet.getString("nickname");
        } catch (SQLException e) {
            throw new DBException();
        }

        return playerNick;
    }

    /**
     * Sets or updates a player's nickname in the database.
     * @param player The player to be modified.
     * @param nickname The nickname to be set. Null will remove any nickname, equivalent to "".
     * @throws DBException Only thrown if the update failed, possibly because loadDatabase() wasn't called.
     */
    public static void setPlayerNick(Player player, @Nullable String nickname) throws DBException {
        String uuid = player.getUniqueId().toString();

        if (nickname == null)
            nickname = "";

        try {
            playersStmt.execute("UPDATE players SET nickname = '" + nickname + "' WHERE UUID = '" + uuid + "'");
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    /**
     * Loads every town from the database. This is a heavy function, only use this on startup and cache the towns.
     * @return A list of every town.
     * @throws DBException Only thrown if the lookup failed, possibly because loadDatabase() wasn't called.
     */
    public static List<Town> loadTowns() throws DBException {
        List<Town> towns = new ArrayList<>();

        try {
            ResultSet resultSet = playersStmt.executeQuery("SELECT * FROM towns");

            while (resultSet.next()) {
                towns.add(new Town(resultSet.getString("name"), resultSet.getString("prefix"),
                        resultSet.getString("owner"), resultSet.getInt("balance"))
                        .deserializeMembers(resultSet.getString("members")));
            }
        } catch (SQLException e) {
            throw new DBException();
        }

        return towns;
    }

    /**
     * Adds a town to the database, or, if it exists, updates it.
     * @throws DBException Only thrown if the addition/update failed, possibly because loadDatabase() wasn't called.
     */
    public static void addTown(Town town) throws DBException {
        String townName = town.name;
        String townPrefix = town.prefix;
        String townOwner = town.owner;
        String townMembers = town.serializeMembers();
        int townBalance = town.getBalance();

        try {
            ResultSet resultSet = playersStmt.executeQuery("SELECT COUNT(town_id) FROM towns WHERE name = '"
                    + townName + "';");
            resultSet.next();

            if (resultSet.getInt(1) == 1) {
                playersStmt.execute("UPDATE towns SET owner = '" + townOwner + "', members = '" + townMembers
                        + "', balance = " + townBalance + " WHERE name = '" + townName + "';");
            } else {
                playersStmt.execute("INSERT INTO towns (town_id, name, prefix, owner, members, balance) "
                        + "VALUES (NULL, '" + townName + "', '" + townPrefix + "', '"
                        + townOwner + "', '" + townMembers + "', " + townBalance + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }
    }

    /**
     * Removes a town from the database.
     * @throws DBException Thrown if the town doesn't exist.
     */
    public static void removeTown(Town town) throws DBException {
        String townName = town.name;

        try {
            playersStmt.execute("DELETE FROM towns WHERE name = '" + townName + "'");
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    /**
     * Fetches a player's balance from the database.
     * @param player The player to be checked.
     * @throws DBException Thrown if a player doesn't exist in the database.
     */
    @NotNull
    public static float getPlayerBalance(Player player) throws DBException {
        String uuid = player.getUniqueId().toString();

        try {
            ResultSet resultSet = playersStmt.executeQuery("SELECT balance FROM players WHERE "
                    + "UUID = '" + uuid + "'");

            resultSet.next();

            return (float) resultSet.getInt("balance") / (float) Config.getPupsInDoge();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }
    }

    /**
     * Sets a player's balance in the database.
     * @param player The player to be modified.
     * @param amount The amount to set.
     * @throws DBException Thrown if a player doesn't exist in the database.
     */
    public static void setPlayerBalance(Player player, float amount) throws DBException {
        String uuid = player.getUniqueId().toString();

        int pups = (int) (amount * Config.getPupsInDoge());

        try {
            playersStmt.execute("UPDATE players SET balance = " + pups + " WHERE UUID = '" + uuid + "'");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }
    }

    /**
     * Adds a specific amount to a player's balance.
     * @param player The player to add to.
     * @param amount The amount to add.
     * @throws DBException Thrown if the player doesn't exist.
     */
    public static void addBalance(Player player, float amount) throws DBException {
        String uuid = player.getUniqueId().toString();

        try {
            float newBalance = getPlayerBalance(player) + amount;
            int pups = (int) (newBalance * Config.getPupsInDoge());

            playersStmt.execute("UPDATE players SET balance = " + pups + " WHERE UUID = '" + uuid + "'");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }
    }

    /**
     * Removes a specific amount from a player's balance.
     * @param player The player to remove from.
     * @param amount The amount to remove.
     * @return If this function returns false, the player would go into negative when completing this transaction, therefore it is not executed.
     * @throws DBException Thrown if the player doesn't exist.
     */
    public static boolean removeBalance(Player player, float amount) throws DBException{
        String uuid = player.getUniqueId().toString();

        try {
            float newBalance = getPlayerBalance(player) - amount;

            if (newBalance < 0) {
                return false;
            }

            int pups = (int) (newBalance * Config.getPupsInDoge());

            playersStmt.execute("UPDATE players SET balance = " + pups + " WHERE UUID = '" + uuid + "'");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException();
        }

        return true;
    }
}
