package com.sarahisweird.dogeverse.ranks;

import com.sarahisweird.dogeverse.config.Config;

public abstract class Rank {
    private String name;
    private String prefix;
    private String nameColor;
    private String messageColor;
    private String rankAbbreviation;
    private String rankOrder;

    private boolean canFormatMessages;

    public Rank(String name) {
        this.name = name;
        this.prefix = Config.getRankPrefix(name);
        this.nameColor = Config.getRankNameColor(name);
        this.messageColor = Config.getMessageColor(name);
        this.rankAbbreviation = Config.getRankAbbreviation(name);
        this.rankOrder = Config.getRankOrder(name);

        this.canFormatMessages = Config.canFormatMessage(name);
    }

    public String getName() {
        return this.name;
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

    public String getOrder() {
        return this.rankOrder;
    }
}
