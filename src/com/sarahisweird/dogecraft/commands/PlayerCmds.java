package com.sarahisweird.dogecraft.commands;

import com.sarahisweird.dogecraft.dbmanager.DBException;
import com.sarahisweird.dogecraft.dbmanager.DBManager;
import org.bukkit.entity.Player;

public class PlayerCmds {
    public static boolean execHealCmd(Player player) {
        player.setFoodLevel(20);
        player.setHealth(20);
        player.setSaturation(20);

        player.sendMessage("Suddenly you feel okay again.");

        return true;
    }

    public static boolean execFlyCmd(Player player, DBManager dbManager) {
        boolean flying = false;

        try {
            flying = !dbManager.isPlayerFlying(player);
            dbManager.setPlayerFlying(player, flying);
        } catch (DBException e) {
            e.printStackTrace();
        }

        player.setAllowFlight(flying);
        player.setFlying(flying);

        if (flying) {
            player.sendMessage("Suddenly wings start to grow...");
        } else {
            player.sendMessage("Your wings seem to have disappeared again.");
        }

        return true;
    }
}
