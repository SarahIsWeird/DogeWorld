package com.sarahisweird.dogeworld.ranks;

import com.sarahisweird.dogeworld.config.Config;

public class DonatorRank extends Rank {
    public DonatorRank() {
        super(Config.getRankPrefix("donator"), Config.getRankNameColor("donator"),
                Config.getMessageColor("donator"));
    }

    @Override
    public boolean canFormatMessages() {
        return Config.canFormatMessage("donator");
    }
}
