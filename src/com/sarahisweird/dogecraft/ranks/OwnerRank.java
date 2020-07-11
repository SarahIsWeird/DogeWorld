package com.sarahisweird.dogecraft.ranks;

import com.sarahisweird.dogecraft.config.Config;

public class OwnerRank extends Rank {

    public OwnerRank() {
        super(Config.getRankPrefix("owner"), Config.getRankNameColor("owner"),
                Config.getMessageColor("owner"));
    }

    @Override
    public boolean canFormatMessages() {
        return Config.canFormatMessage("owner");
    }
}
