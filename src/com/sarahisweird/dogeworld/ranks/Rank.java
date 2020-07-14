package com.sarahisweird.dogeworld.ranks;

import com.sarahisweird.dogeworld.config.Config;

public abstract class Rank {
    private String prefix;
    private String nameColor;
    private String messageColor;
    private String rankAbbreviation;
    private boolean canFormatMessages;

    public Rank(String name) {
        this.prefix = Config.getRankPrefix(name);
        this.nameColor = Config.getRankNameColor(name);
        this.messageColor = Config.getMessageColor(name);
        this.rankAbbreviation = Config.getRankAbbreviation(name);
        this.canFormatMessages = Config.canFormatMessage(name);
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getNameColor() {
        return this.nameColor;
    }

    public String getMessageColor() {
        return this.messageColor;
    }

    public String getRankAbbreviation() {
        return this.rankAbbreviation;
    }

    public boolean canFormatMessages() {
        return this.canFormatMessages;
    }
}
