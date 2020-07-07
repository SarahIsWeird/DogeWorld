package com.sarahisweird.dogecraft.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Random;

public class Teleport implements CommandExecutor {

    private static final int bound = 30000;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("rtp")) {
            Date now = new Date();
            Random rnd = new Random();

            rnd.setSeed(now.getTime());

            int x;
            int z;
            int highestBlockY;
            Material highestBlockType;

            World world = player.getWorld();

            do {
                x = rnd.nextInt(bound * 2) - bound;
                z = rnd.nextInt(bound * 2) - bound;
                highestBlockType = world.getHighestBlockAt(x, z).getType();

            } while (highestBlockType.equals(Material.WATER) || highestBlockType.equals(Material.LAVA));

            highestBlockY = world.getHighestBlockYAt(x, z);

            player.sendMessage("Teleporting to " + x + ", " + highestBlockY + ", " + z + "...");

            Location tpLocation = new Location(player.getWorld(), x + 0.5, highestBlockY + 1, z + 0.5);

            player.teleport(tpLocation);

        } else if (command.getName().equalsIgnoreCase("spawn")) {
            Location tpLocation = new Location(player.getWorld(), 169.5, 68, 191.5);

            player.teleport(tpLocation);
        }

        return false;
    }
}
