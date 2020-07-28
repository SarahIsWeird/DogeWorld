package com.sarahisweird.dogeverse.ranks;

import com.sarahisweird.dogeverse.dbmanager.DBException;
import com.sarahisweird.dogeverse.dbmanager.DBManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RankManager {
    private static List<Rank> ranks;

    private static Rank defaultRank;

    public static void load() {
        ranks = new ArrayList<>();

        ranks.add(0, new MemberRank());
        ranks.add(1, new DonatorRank());
        ranks.add(2, new JuniorHelperRank());
        ranks.add(3, new HelperRank());
        ranks.add(4, new JuniorModeratorRank());
        ranks.add(5, new ModeratorRank());
        ranks.add(6, new CMRank());
        ranks.add(7, new DeveloperRank());
        ranks.add(8, new OwnerRank());

        defaultRank = ranks.get(0);
    }

    /**
     * Converts a rank ID into an instance of the rank.
     * @param id The rank ID.
     * @return The rank instance.
     */
    @Nullable
    private static Rank rankIdToRank(int id) {
        return ranks.get(id);
    }

    /**
     * Converts a rank instance to the corresponding rank ID.
     * @param rank The rank instance.
     * @return The rank ID.
     */
    private static int rankToRankId(Rank rank) {
        System.out.println(ranks.indexOf(rank));
        return ranks.indexOf(rank);
    }

    /**
     * Fetches the rank of a player.
     * @param player The player in question.
     * @return The rank of the player.
     */
    public static Rank getRank(Player player) {
        try {
            return rankIdToRank(DBManager.getPlayerRank(player));
        } catch (DBException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets a player's rank. Will also update the database.
     * @param player The player to be modified.
     * @param rank The rank to be set.
     * @return Whether the update was successful or not.
     */
    public static boolean setRank(Player player, Rank rank) {
        try {
            DBManager.setPlayerRank(player, rankToRankId(rank));

            return true;
        } catch (DBException e) {
            return false;
        }
    }

    /** Formats a message based on the player's rank.
     * For example: A member's message won't be formatted, while one from a donator will be.
     * @param player The calling player.
     * @param message The message to be formatted.
     * @return The (un)formatted message.
     */
    public static String formatMessage(Player player, String message) {
        if (getRank(player).canFormatMessages()) {
            return ChatColor.translateAlternateColorCodes('&', message);
        } else {
            return message;
        }
    }

    /**
     * Converts a rank's name into the corresponding class instance.
     * @param name The rank's name.
     * @return The class instance.
     */
    @Nullable
    public static Rank rankNameToRank(String name) {
        for (Rank rank : ranks) {
            if (rank.getName().equals(name))
                return rank;
        }

        return null;
    }
}
