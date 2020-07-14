package com.sarahisweird.dogeworld.ranks;

import com.sarahisweird.dogeworld.config.Config;

public class DeveloperRank extends Rank {
    public DeveloperRank() {
        super("developer");
    }

    @Override
    public boolean canFormatMessages() {
        return Config.canFormatMessage("developer");
    }
}
