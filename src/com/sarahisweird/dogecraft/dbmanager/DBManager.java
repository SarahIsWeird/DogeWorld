package com.sarahisweird.dogecraft.dbmanager;

import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static Connection playersConn;

    public DBManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            playersConn = DriverManager.getConnection("jdbc:sqlite:server.db");
        } catch (Exception e) {
            System.err.println("Failed to connect to the SQLite database.");
            e.printStackTrace();
        }
    }

    public void disable() {
        try {
            playersConn.commit();
            playersConn.close();
        } catch (SQLException e) {
            System.err.println("Failed to properly close the SQLite database.");
        }
    }

    public void createPlayerDatabase() throws DBException {
        try {
            Statement statement = playersConn.createStatement();

            statement.execute("DROP TABLE IF EXISTS players");
            statement.execute("CREATE TABLE players (" +
                    "UUID   CHAR(36) PRIMARY KEY NOT NULL, " +
                    "flying TINYINT              NOT NULL)");

            statement.close();
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    private void createPlayerData(Player player) throws DBException {
        String uuid = player.getUniqueId().toString();

        String query = "INSERT INTO players (UUID, flying) VALUES (" + uuid + ", 0);";

        try {
            Statement statement = playersConn.createStatement();
            statement.executeQuery(query);
            statement.close();
        } catch (SQLException e) {
            throw new DBException();
        }
    }

    private boolean playerDataExists(Player player) {
        String uuid = player.getUniqueId().toString();

        String query = "SELECT COUNT(UUID) FROM players WHERE UUID='" + uuid + "'";

        try {
            Statement statement = playersConn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                statement.close();
                return true;
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String> dump() throws DBException {
        List<String> lines = new ArrayList<>();

        try {
            Statement statement = playersConn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM players");

            while (resultSet.next()) {
                lines.add(resultSet.getString("UUID") + ": "
                        + "flying(" + resultSet.getInt("flying") + ")");
            }

            statement.close();
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
            Statement statement = playersConn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT flying FROM players WHERE UUID='" + uuid + "'");
            resultSet.next(); // UUID = unique -> no while loop

            returnVal = resultSet.getBoolean("flying");

            statement.close();
        } catch (SQLException e) {
            throw new DBException();
        }

        return returnVal;
    }

    public void setPlayerFlying(Player player, boolean flying) throws DBException {
        String uuid = player.getUniqueId().toString();

        try {
            Statement statement = playersConn.createStatement();
            statement.execute("UPDATE players SET flying = " + (flying ? 1 : 0) + " WHERE UUID = '" + uuid + "'");
            statement.close();
        } catch (SQLException e) {
            throw new DBException();
        }
    }
}
