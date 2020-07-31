package com.sarahisweird.dogeverse.commands;

import com.sarahisweird.dogeverse.dbmanager.DBException;
import com.sarahisweird.dogeverse.dbmanager.DBManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Random;

public class TeleportCmds {

    private static final int bound = 30000;

    public static boolean execRandomTeleportCmd(Player sender) {
        try {
            if (DBManager.getPlayerBalance(sender) < 5) {
                sender.sendMessage("§cYou need 10 Doge to execute /rtp.");
                return true;
            }

            DBManager.removeBalance(sender, 5);
        } catch (DBException e) {
            e.printStackTrace();
            return false;
        }


        Date now = new Date();
        Random rnd = new Random();

        rnd.setSeed(now.getTime());

        int x;
        int z;
        int highestBlockY;
        Material highestBlockType;

        World world = sender.getWorld();

        do {
            x = rnd.nextInt(bound * 2) - bound;
            z = rnd.nextInt(bound * 2) - bound;
            highestBlockType = world.getHighestBlockAt(x, z).getType();

        } while (highestBlockType.equals(Material.WATER) || highestBlockType.equals(Material.LAVA));

        highestBlockY = world.getHighestBlockYAt(x, z);

        sender.sendMessage("Teleporting to " + x + ", " + highestBlockY + ", " + z + "...");

        Location tpLocation = new Location(sender.getWorld(), x + 0.5, highestBlockY + 1, z + 0.5);

        sender.teleport(tpLocation);

        return true;
    }

    public static boolean execSpawnCmd(Player player) {
        Location tpLocation = new Location(player.getWorld(), 169.5, 68, 191.5);

        player.teleport(tpLocation);
        return true;
    }
}
