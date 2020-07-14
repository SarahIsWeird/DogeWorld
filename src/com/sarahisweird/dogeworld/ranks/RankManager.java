package com.sarahisweird.dogeworld.ranks;

import com.sarahisweird.dogeworld.dbmanager.DBException;
import com.sarahisweird.dogeworld.dbmanager.DBManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RankManager {
    public static Rank errorRank = new Rank("ERROR") {
        @Override
        public boolean canFormatMessages() {
            return false;
        }
    };

    private static MemberRank memberRank = new MemberRank();
    private static DonatorRank donatorRank = new DonatorRank();
    private static DeveloperRank developerRank = new DeveloperRank();
    private static CMRank cmRank = new CMRank();
    private static OwnerRank ownerRank = new OwnerRank();
    private static JuniorModeratorRank juniorModeratorRank = new JuniorModeratorRank();
    private static ModeratorRank moderatorRank = new ModeratorRank();
    private static JuniorHelperRank juniorHelperRank = new JuniorHelperRank();
    private static HelperRank helperRank = new HelperRank();

    private static Rank rankIdToRank(int id) {
        switch (id) {
            case 0:
                return memberRank;
            case 1:
                return donatorRank;
            case 2:
                return developerRank;
            case 3:
                return cmRank;
            case 4:
                return ownerRank;
            case 5:
                return juniorModeratorRank;
            case 6:
                return moderatorRank;
            case 7:
                return juniorHelperRank;
            case 8:
                return helperRank;
            default:
                return errorRank;
        }
    }

    private static int rankToRankId(Rank rank) {
        if (rank instanceof MemberRank) {
            return 0;
        } else if (rank instanceof DonatorRank) {
            return 1;
        } else if (rank instanceof DeveloperRank) {
            return 2;
        } else if (rank instanceof CMRank) {
            return 3;
        } else if (rank instanceof OwnerRank) {
            return 4;
        } else if (rank instanceof JuniorModeratorRank) {
            return 5;
        } else if (rank instanceof ModeratorRank) {
            return 6;
        } else if (rank instanceof JuniorHelperRank) {
            return 7;
        } else if (rank instanceof HelperRank) {
            return 8;
        }

        return -1;
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
            return errorRank;
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
    public static Rank rankNameToRank(String name) {
        if (name.equalsIgnoreCase("member")) {
            return memberRank;
        } else if (name.equalsIgnoreCase("donator")) {
            return donatorRank;
        } else if (name.equalsIgnoreCase("developer")) {
            return developerRank;
        } else if (name.equalsIgnoreCase("cm")) {
            return cmRank;
        } else if (name.equalsIgnoreCase("owner")) {
            return ownerRank;
        } else if (name.equalsIgnoreCase("juniormod")) {
            return juniorModeratorRank;
        } else if (name.equalsIgnoreCase("moderator")) {
            return moderatorRank;
        } else if (name.equalsIgnoreCase("juniorhelper")) {
            return juniorHelperRank;
        } else if (name.equalsIgnoreCase("helper")) {
            return helperRank;
        } else {
            return errorRank;
        }
    }
}
