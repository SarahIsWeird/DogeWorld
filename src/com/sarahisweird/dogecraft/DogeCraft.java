package com.sarahisweird.dogecraft;

import com.sarahisweird.dogecraft.commands.Teleport;
import com.sarahisweird.dogecraft.config.Config;
import com.sarahisweird.dogecraft.dbmanager.DBException;
import com.sarahisweird.dogecraft.dbmanager.DBManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class DogeCraft extends JavaPlugin implements Listener {

    Config config = new Config(this);

    DBManager dbManager;

    Teleport teleportExec;

    // Alias for brevity
    private String fmt(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    @Override
    public void onEnable() {
        config.load();

        this.getServer().getPluginManager().registerEvents(this, this);

        this.getCommand("rtp").setExecutor(teleportExec);
        this.getCommand("spawn").setExecutor(teleportExec);

        dbManager = new DBManager();

        System.out.println("Successfully connected to the database.");

        System.out.print("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        config.save();

        System.out.print("Plugin disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("heal")) {
            player.setFoodLevel(20);
            player.setHealth(20);
            player.setSaturation(20);

            player.sendMessage("Suddenly you feel okay again.");
        } else if (command.getName().equalsIgnoreCase("fly")) {
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
        } else if (command.getName().equalsIgnoreCase("db")) {
            if (args.length == 0) {
                player.sendMessage("Available subcommands: /db create, dump");
                return true;
            }

            if (args[0].equalsIgnoreCase("create")) {
                if (args.length < 2 || !args[1].equalsIgnoreCase("confirm")) {
                    player.sendMessage(fmt("&c&oAre you really sure you want to create a new database? If a database"
                            + "already exists, it will be nuked!"));
                    player.sendMessage(fmt("&c&oIf you're sure, type &c/db create confirm&o."));
                    return true;
                }

                try {
                    dbManager.createPlayerDatabase();
                } catch (DBException e) {
                    e.printStackTrace();
                    player.sendMessage(fmt("&4The database couldn't be created. See the logs for more info."));
                    return true;
                }

                player.sendMessage(fmt("&aSuccessfully created the database."));
                return true;
            } else if (args[0].equalsIgnoreCase("dump")) {
                try {
                    player.sendMessage("Dumping database...");
                    List<String> lines = dbManager.dump();

                    for (String str : lines) {
                        player.sendMessage(str);
                    }
                } catch (DBException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean flying = false;

        try {
            dbManager.loadPlayer(player);
            flying = dbManager.isPlayerFlying(player);
        } catch (DBException e) {
            e.printStackTrace();
        }

        player.setAllowFlight(flying);
        player.setFlying(flying);
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        event.setMessage(fmt(event.getMessage()));
    }
}
