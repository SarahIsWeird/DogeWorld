package com.sarahisweird.dogecraft.ranks;

import com.sarahisweird.dogecraft.dbmanager.DBException;
import com.sarahisweird.dogecraft.dbmanager.DBManager;
import org.bukkit.entity.Player;

public class RankManager {
    private static Rank errorRank = new Rank("ERROR", "ERROR", "ERROR");

    private static MemberRank memberRank = new MemberRank();

    private static Rank rankIdToRank(int id) {
        switch (id) {
            case 0:
                return memberRank;
            default:
                return errorRank;
        }
    }

    public static Rank getRank(Player player) {
        try {
            return rankIdToRank(DBManager.getPlayerRank(player));
        } catch (DBException e) {
            return errorRank;
        }
    }
}
