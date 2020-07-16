package com.sarahisweird.dogeverse.towns;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TownSetup {
    public final Player player;
    private int progress = 0;
    private String townName;
    private String townPrefix;
    private boolean cancelled = false;

    private String fmt(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    private boolean finalizeTown() {
        Town town = new Town(this.townName, this.townPrefix, this.player.getUniqueId().toString());
        town.addMember(this.player);

        TownManager.addTown(town);

        this.cancelled = true;

        this.player.sendMessage(fmt("&aSuccessfully created your town! Type /info towns for more information on "
                + "how to get your town going!"));

        return true;
    }

    public TownSetup(Player player) {
        this.player = player;

        this.queryForName();
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isDone() {
        return cancelled;
    }

    public void queryForName() {
        this.player.sendMessage(fmt("&bWhat should be your town's name? Type 'cancel' to cancel."));
    }

    public void queryForPrefix() {
        this.player.sendMessage(fmt("&bWhat should be your town's prefix? Type 'cancel' to cancel."));
    }

    public void onInput(String message) {
        if (message.equalsIgnoreCase("cancel")) {
            this.cancelled = true;
            this.player.sendMessage(fmt("&cCancelled the town creation."));

            return;
        }

        switch (progress) {
            /* Name setup */
            case 0:
                if (TownManager.isTownNameTaken(message)) {
                    this.player.sendMessage(fmt("&cThis name is already taken. Please choose another one."));
                    break;
                }

                this.player.sendMessage(fmt("&aOkay, your town will be called &l" + message + "&a."));

                this.townName = message;
                this.progress = 1;

                this.queryForPrefix();

                break;

            /* Prefix setup */
            case 1:
                if (message.length() > 4) {
                    this.player.sendMessage(fmt("&cYour prefix can only be four characters long. "
                            + "Please try again."));
                    break;
                }

                if (TownManager.isTownPrefixTaken(message)) {
                    this.player.sendMessage(fmt("&cThis prefix is already taken. Please choose another one."));
                    break;
                }

                this.player.sendMessage(fmt("&aOkay, your town's prefix will be &o" + message + "&a."));

                this.townPrefix = message;

                this.finalizeTown();

                break;
        }
    }
}
