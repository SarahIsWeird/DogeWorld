package com.sarahisweird.dogecraft.ranks;

import com.sarahisweird.dogecraft.config.Config;

public class DeveloperRank extends Rank {
    public DeveloperRank() {
        super(Config.getRankPrefix("developer"), Config.getRankNameColor("developer"),
                Config.getMessageColor("developer"));
    }

    @Override
    public boolean canFormatMessages() {
        return Config.canFormatMessage("developer");
    }
}
