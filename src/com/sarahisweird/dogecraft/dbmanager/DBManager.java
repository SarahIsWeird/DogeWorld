package com.sarahisweird.dogecraft.dbmanager;

import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static Connection playersConn;
    private static Statement playersStmt;

    public DBManager() {
        try {
            Class.forName("org.sqlite.JDBC");

            playersConn = DriverManager.getConnection("jdbc:sqlite:server.db");
            playersStmt = playersConn.createStatement();
        } catch (Exception e) {
            System.err.println("Failed to connect to the SQLite database.");
            e.printStackTrace();
        }
    }

    public void disable() {
        try {
            playersStmt.close();

            playersConn.commit();
            playersConn.close();
        } catch (SQLException e) {
            System.err.println("Failed to properly close the SQLite database.");
        }
    }

    public void createPlayerDatabase() throws DBException {
        try {
            playersStmt.execute("DROP TABLE IF EXISTS players");
            playersStmt.execute("CREATE TABLE players (" +
                    "UUID   CHAR(36) PRIMARY KEY NOT NULL, " +
                    "flying TINYINT              NOT NULL)");
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    private void createPlayerData(Player player) throws DBException {
        String uuid = player.getUniqueId().toString();

        String query = "INSERT INTO players (UUID, flying) VALUES (" + uuid + ", 0);";

        try {
            playersStmt.executeQuery(query);
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    private boolean playerDataExists(Player player) {
        String uuid = player.getUniqueId().toString();

        String query = "SELECT COUNT(UUID) FROM players WHERE UUID='" + uuid + "'";

        try {
            ResultSet resultSet = playersStmt.executeQuery(query);

            if (resultSet.next()) {
                playersStmt.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String> dump() throws DBException {
        List<String> lines = new ArrayList<>();

        try {
            ResultSet resultSet = playersStmt.executeQuery("SELECT * FROM players");

            while (resultSet.next()) {
                lines.add(resultSet.getString("UUID") + ": "
                        + "flying(" + resultSet.getInt("flying") + ")");
            }
        } catch (SQLException e) {
            throw new DBException();
        }

        return lines;
    }

    public void loadPlayer(Player player) throws DBException {
        if (!playerDataExists(player)) {
            createPlayerData(player);
        }
    }

    public boolean isPlayerFlying(Player player) throws DBException {
        String uuid = player.getUniqueId().toString();
        boolean returnVal;

        this.loadPlayer(player);

        try {
            ResultSet resultSet = playersStmt.executeQuery("SELECT flying FROM players WHERE UUID='" + uuid + "'");
            resultSet.next(); // UUID = unique -> no while loop

            returnVal = resultSet.getBoolean("flying");
        } catch (SQLException e) {
            throw new DBException();
        }

        return returnVal;
    }

    public void setPlayerFlying(Player player, boolean flying) throws DBException {
        String uuid = player.getUniqueId().toString();

        try {
            playersStmt.execute("UPDATE players SET flying = " + (flying ? 1 : 0) + " WHERE UUID = '" + uuid + "'");
        } catch (SQLException e) {
            throw new DBException();
        }
    }
}
