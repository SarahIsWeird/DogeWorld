package com.sarahisweird.dogecraft.ranks;

public abstract class Rank {
    private String prefix;
    private String nameColor;
    private String messageColor;

    public Rank(String prefix, String nameColor, String messageColor) {
        this.prefix = prefix;
        this.nameColor = nameColor;
        this.messageColor = messageColor;
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

    public abstract boolean canFormatMessages();
}
