package com.sarahisweird.dogeverse.towns;

import com.sarahisweird.dogeverse.dbmanager.DBException;
import com.sarahisweird.dogeverse.dbmanager.DBManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Town {
    public final String name;
    public final String owner;
    public final String prefix;

    /* A list of UUIDs. */
    private List<String> members;

    private int balance;

    /**
     * Creates the town instance.
     * @param name The name of the town.
     * @param prefix The prefix of the town.
     * @param owner The owner of the town, provided as an UUID.
     */
    public Town(String name, String prefix, String owner, int balance) {
        this.name = name;
        this.owner = owner;
        this.prefix = prefix;

        this.members = new ArrayList<>();

        this.balance = balance;
    }

    /**
     * Returns a database-friendly string with a list of all town members.
     * @return The database-friendly string.
     */
    public String serializeMembers() {
        String retString = "";

        for (String str : this.members) {
            retString += str + ",";
        }

        return retString.substring(0, retString.length() - 1); // Remove the last comma
    }

    /**
     * Turns the provided database-friendly string into the internal member list.
     * @return Itself.
     */
    public Town deserializeMembers(String serializedMembers) {
        this.members = new ArrayList<>(Arrays.asList(serializedMembers.split(",")));
        return this;
    }

    /**
     * Adds a member to the town.
     * @param uuid The UUID of the player.
     */
    public void addMember(String uuid) {
        this.members.add(uuid);
    }

    /**
     * Adds a member to the town.
     * @param player The UUID of the player.
     */
    public void addMember(Player player) {
        this.members.add(player.getUniqueId().toString());
    }

    /**
     * Removes a member from the town. Doesn't fail if the player isn't present.
     * @param uuid The UUID of the player.
     */
    public void removeMember(String uuid) {
        this.members.remove(uuid);
    }

    /**
     * Removes a member from the town. Doesn't fail if the player isn't present.
     * @param player The UUID of the player.
     */
    public void removeMember(Player player) {
        this.members.remove(player.getUniqueId().toString());
    }

    /**
     * Gets a list of all players in the town, provided via the UUIDs.
     * @return The list of UUIDs.
     */
    public List<String> getMembers() {
        return this.members;
    }

    /**
     * Checks whether a player is part of the town.
     * @param uuid The player in question's uuid.
     * @return Whether they belong to the town.
     */
    public boolean isMember(String uuid) {
        return this.members.contains(uuid);
    }

    /**
     * Checks whether a player is part of the town.
     * @param player The player in question.
     * @return Whether they belong to the town.
     */
    public boolean isMember(Player player) {
        return this.members.contains(player.getUniqueId().toString());
    }

    public int getBalance() {
        return this.balance;
    }

    public void setBalance(int newBalance) {
        this.balance = newBalance;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public boolean removeBalance(int amount) {
        int newAmount = this.balance - amount;

        if (newAmount < 0)
            return false;

        this.setBalance(newAmount);
        return true;
    }
}
