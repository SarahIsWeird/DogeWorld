package com.sarahisweird.dogeworld.ranks;

import com.sarahisweird.dogeworld.config.Config;

public class MemberRank extends Rank {

    public MemberRank() {
        super(Config.getRankPrefix("member"), Config.getRankNameColor("member"),
                Config.getMessageColor("member"));
    }

    @Override
    public boolean canFormatMessages() {
        return Config.canFormatMessage("member");
    }
}
