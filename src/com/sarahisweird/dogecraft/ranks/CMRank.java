package com.sarahisweird.dogecraft.ranks;

import com.sarahisweird.dogecraft.config.Config;

public class CMRank extends Rank {

    public CMRank() {
        super(Config.getRankPrefix("cm"), Config.getRankNameColor("cm"), Config.getMessageColor("cm"));
    }

    @Override
    public boolean canFormatMessages() {
        return Config.canFormatMessage("cm");
    }
}
