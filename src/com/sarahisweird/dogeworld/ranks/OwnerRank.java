package com.sarahisweird.dogeworld.ranks;

import com.sarahisweird.dogeworld.config.Config;

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
