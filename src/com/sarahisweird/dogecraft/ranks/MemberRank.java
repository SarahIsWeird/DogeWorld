package com.sarahisweird.dogecraft.ranks;

import com.sarahisweird.dogecraft.config.Config;

public class MemberRank extends Rank {

    public MemberRank() {
        super(Config.getRankPrefix("member"), Config.getRankNameColor("member"),
                Config.getMessageColor("member"));
    }
}
