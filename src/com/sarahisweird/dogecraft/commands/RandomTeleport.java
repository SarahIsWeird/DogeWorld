package com.sarahisweird.dogecraft.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Random;
import java.util.Vector;

public class RandomTeleport implements CommandExecutor {

    private static final int bound = 30000;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //noinspection IfStatementWithIdenticalBranches
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("rtp")) {
            Date now = new Date();
            Random rnd = new Random();

            rnd.setSeed(now.getTime());

            int x = 0, z = 0;
            int highestBlockY = 0;
            Block highestBlock;
            Material highestBlockType = Material.AIR;

            while (!highestBlockType.equals(Material.GRASS_BLOCK)) {
                x = rnd.nextInt(bound * 2) - bound;
                z = rnd.nextInt(bound * 2) - bound;

                highestBlock = player.getWorld().getHighestBlockAt(x, z);
                highestBlockType = highestBlock.getType();
                highestBlockY = player.getWorld().getHighestBlockYAt(x, z);
            }

            player.chat("Teleporting to " + Integer.toString(x) + ", " + Integer.toString(highestBlockY) + ", " + Integer.toString(z) + "...");

            Location tpLocation = new Location(player.getWorld(), x, highestBlockY, z);

        } else if (command.getName().equalsIgnoreCase("spawn")) {
            Location tpLocation = new Location(player.getWorld(), (double) 169, (double) 68, (double) 191);

            player.teleport(tpLocation);
        }

        return false;
    }
}
