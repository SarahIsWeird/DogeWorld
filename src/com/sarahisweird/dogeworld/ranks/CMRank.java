package com.sarahisweird.dogeworld.ranks;

import com.sarahisweird.dogeworld.config.Config;

public class CMRank extends Rank {

    public CMRank() {
        super(Config.getRankPrefix("cm"), Config.getRankNameColor("cm"), Config.getMessageColor("cm"));
    }

    @Override
    public boolean canFormatMessages() {
        return Config.canFormatMessage("cm");
    }
}
