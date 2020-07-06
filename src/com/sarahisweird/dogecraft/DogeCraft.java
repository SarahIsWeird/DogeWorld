package com.sarahisweird.dogecraft;

import com.sarahisweird.dogecraft.config.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.Random;

public class DogeCraft extends JavaPlugin {

    private static final int bound = 30000;

    boolean fly = false;

    Config config = new Config(this);

    @Override
    public void onEnable() {
        System.out.print("Plugin enabled.");

    }

    @Override
    public void onDisable() {
        System.out.print("Plugin disabled.");
    }

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
            Material highestBlockType = Material.WATER;

            World world = player.getWorld();

            while (true) {
                x = rnd.nextInt(bound * 2) - bound;
                z = rnd.nextInt(bound * 2) - bound;
                highestBlockType = world.getHighestBlockAt(x, z).getType();

                if (!highestBlockType.equals(Material.WATER) && !highestBlockType.equals(Material.LAVA))
                    break;
            }

            highestBlockY = world.getHighestBlockYAt(x, z);

            player.sendMessage("Teleporting to " + Integer.toString(x) + ", " + Integer.toString(highestBlockY) + ", " + Integer.toString(z) + "...");

            Location tpLocation = new Location(player.getWorld(), x + 0.5, highestBlockY + 1, z + 0.5);

            player.teleport(tpLocation);

        } else if (command.getName().equalsIgnoreCase("spawn")) {
            Location tpLocation = new Location(player.getWorld(), 169.5, 68, 191.5);

            player.teleport(tpLocation);
        } else if (command.getName().equalsIgnoreCase("heal")) {
            player.setFoodLevel(20);
            player.setHealth(20);
            player.setSaturation(20);

            player.sendMessage("Suddenly you feel okay again.");
        } else if (command.getName().equalsIgnoreCase("fly")) {
            fly = !fly;

            player.setAllowFlight(true);
            player.setFlying(fly);

            if (fly) {
                player.sendMessage("Suddenly wings start to grow...");
            } else {
                player.sendMessage("Your wings seem to have disappeared again.");
            }
        }

        return true;
    }
}
