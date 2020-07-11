package com.sarahisweird.dogecraft.dbmanager;

import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static Connection playersConn;
    private static Statement playersStmt;

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

    public static void disable() {
        try {
            playersStmt.close();

            playersConn.commit();
            playersConn.close();
        } catch (SQLException e) {
            System.err.println("Failed to properly close the SQLite database.");
        }
    }

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

    public static void setPlayerFlying(Player player, boolean flying) throws DBException {
        String uuid = player.getUniqueId().toString();

        try {
            playersStmt.execute("UPDATE players SET flying = " + (flying ? 1 : 0) + " WHERE UUID = '" + uuid + "'");
        } catch (SQLException e) {
            throw new DBException();
        }
    }

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

    public static void setPlayerRank(Player player, int rankId) throws DBException {
        String uuid = player.getUniqueId().toString();

        try {
            playersStmt.execute("UPDATE players SET rank = " + rankId + " WHERE UUID = '" + uuid + "'");
        } catch (SQLException e) {
            throw new DBException();
        }
    }

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

    public static void setPlayerNick(Player player, String nickname) throws DBException {
        String uuid = player.getUniqueId().toString();

        try {
            playersStmt.execute("UPDATE players SET nickname = '" + nickname + "' WHERE UUID = '" + uuid + "'");
        } catch (SQLException e) {
            throw new DBException();
        }
    }
}
