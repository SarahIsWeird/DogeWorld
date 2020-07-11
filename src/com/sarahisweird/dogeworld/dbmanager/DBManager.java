package com.sarahisweird.dogeworld.dbmanager;

import com.sun.istack.internal.Nullable;
import org.bukkit.entity.Player;

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
            System.err.println("Failed to connect to the SQLite database.");
            e.printStackTrace();
        }
    }

    /**
     * Safely unloads the database. If this isn't called, data might be lost.
     */
    public static void disable() {
        try {
            playersStmt.close();

            playersConn.commit();
            playersConn.close();
        } catch (SQLException e) {
            System.err.println("Failed to properly close the SQLite database.");
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
                    "nickname TEXT                          )");
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    /**
     * Dumps the database contents.
     * @return A list of the contents. Formatted, each line is a row in the table.
     * @throws DBException Only thrown if the database couldn't be created.
     */
    public static List<String> dump() throws DBException {
        List<String> lines = new ArrayList<>();

        try {
            ResultSet resultSet = playersStmt.executeQuery("SELECT * FROM players");

            while (resultSet.next()) {
                lines.add(resultSet.getString("UUID") + ": "
                        + "flying(" + resultSet.getInt("flying") + "), "
                        + "rank(" + resultSet.getInt("rank") + "), "
                        + "nickname(" + resultSet.getString("nickname") + ")");
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
     * @throws DBException Thrown if the player couldn't be loaded, possibly because loadDatabase() wasn't called.
     */
    public static void loadPlayer(Player player) throws DBException {
        String uuid = player.getUniqueId().toString();

        try {
            ResultSet resultSet = playersStmt.executeQuery("SELECT COUNT(UUID) FROM players " +
                    "WHERE UUID = '" + uuid + "'");
            resultSet.next();

            if (resultSet.getInt(1) == 1) {
                resultSet.close();

                return;
            }

            playersStmt.execute("INSERT INTO players (UUID, flying, rank, nickname) "
                    + "VALUES ('" + uuid + "', 0, 0, '');");
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
            System.err.println(e.getMessage());
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

            retVal = resultSet.getInt(1);
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
}
